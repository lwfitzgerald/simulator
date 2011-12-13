package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.executionstage.ALU;
import com.fitzgerald.simulator.executionstage.BranchUnit;
import com.fitzgerald.simulator.executionstage.LoadStoreUnit;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.pipeline.FetchStage;
import com.fitzgerald.simulator.pipeline.WritebackStage;

public class Processor {
    
    // Constants
    
    /**
     * Number of registers
     */
    public static final int NUM_REGISTERS = 16;
    
    /**
     * Number of reservation stations
     */
    public static final int NUM_RESERVATION_STATIONS = 12;
    
    /**
     * Maximum number of reorder buffer entries
     * 
     * TODO: Is this needed?
     */
    public static final int MAX_REORDER_BUFFER_SIZE = 16;
    
    /**
     * Number of each type of execution unit
     */
    public static final int NUM_ALUS = 3;
    public static final int NUM_LOAD_STORE_UNITS = 2;
    public static final int NUM_BRANCH_UNITS = 1;
    
    /**
     * Program counter register
     */
    public static final int PC_REG = 15;
    
    // End constants

    protected Program program;
    protected RegisterFile registerFile;
    protected MemoryController memoryController;
    protected Scoreboard scoreboard;
    protected ReservationStation[] reservationStations;
    protected ReorderBuffer reorderBuffer;
    protected BranchPredictor branchPredictor;
    
    /*
     * Pipeline stages
     */
    protected FetchStage fetchStage;
    protected DecodeStage decodeStage;
    protected ExecuteStage executeStage;
    protected WritebackStage writebackStage;
    
    /*
     * Execution units
     */
    protected ALU[] alus;
    protected LoadStoreUnit[] lsUnits;
    protected BranchUnit[] branchUnits;
    
    protected int cycleCount = -1;
    
    /*
     * Speculation status
     */
    protected boolean speculating = false;
    
    /**
     * Holds the address to set the PC to if
     * speculation was incorrect
     */
    protected Integer speculateFailAddr;
    
    /**
     * Create a new processor
     * @param program Program to execute
     * @param memory Memory space to use
     */
    public Processor(Program program, Memory memory) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memoryController = new MemoryController(memory);
        this.scoreboard = new Scoreboard();
        
        // Initialise reservation stations
        initReservationStations();
        
        this.reorderBuffer = new ReorderBuffer();
        this.branchPredictor = new BranchPredictor();
        
        // Initialise pipeline stages
        this.fetchStage = new FetchStage(this);
        this.decodeStage = new DecodeStage(this);
        this.executeStage = new ExecuteStage(this);
        this.writebackStage = new WritebackStage(this);
        
        // Initialise execution units
        initExecutionUnits();
        
        // Set program counter to 0
        registerFile.getRegister(PC_REG).setValue(0);
    }
    
    /**
     * Performs one simulation step
     * @return false if program has finished executing
     */
    public boolean step() {
        /*
         * Perform writeback first to ensure
         * instructions completing are not
         * written back in the same cycle
         */
        writebackStage.step();

        /*
         * Execute and decode in reverse order
         * to ensure instructions issued are not
         * executed in the same cycle 
         */
        executeStage.step();
        decodeStage.step();
        
        fetchStage.step(program);
        
        // Check if the program has finished
        if (checkProgramFinished()) {
            return false;
        }
        
        // Copy instructions from fetch to decode if needed
        fetchStage.finishStep(decodeStage);
        
        return true;
    }
    
    /**
     * Initialise the reservation stations
     */
    protected void initReservationStations() {
        this.reservationStations = new ReservationStation[NUM_RESERVATION_STATIONS];
        
        for (int i=0; i < NUM_RESERVATION_STATIONS; i++) {
            this.reservationStations[i] = new ReservationStation(this);
        }
    }
    
    /**
     * Get a free reservation station
     * @return A free reservation station or null if
     * none are free
     */
    public ReservationStation getFreeReservationStation() {
        for (ReservationStation rs : reservationStations) {
            if (rs.isEmpty()) {
                return rs;
            }
        }
        
        return null;
    }
    
    /**
     * Get a ready reservation station for the given
     * instruction type
     * @param instructionType Desired instruction type
     * @return A ready reservation station or null if
     * none are ready
     */
    public ReservationStation getReadyReservationStation(
            InstructionType instructionType) {
        for (ReservationStation rs : reservationStations) {
            if (rs.isReadyForDispatch()
                    && rs.getRequiredExecutionType() == instructionType) {
                return rs;
            }
        }
        
        return null;
    }
    
    /**
     * Initialise execution units
     */
    protected void initExecutionUnits() {
        alus = new ALU[NUM_ALUS];
        
        for (int i=0; i < NUM_ALUS; i++) {
            alus[i] = new ALU(this);
        }
        
        lsUnits = new LoadStoreUnit[NUM_LOAD_STORE_UNITS];
        
        for (int i=0; i < NUM_LOAD_STORE_UNITS; i++) {
            lsUnits[i] = new LoadStoreUnit(this);
        }
        
        branchUnits = new BranchUnit[NUM_BRANCH_UNITS];
        
        for (int i=0; i < NUM_BRANCH_UNITS; i++) {
            branchUnits[i] = new BranchUnit(this);
        }
    }
    
    /**
     * Flush all pipeline stages
     */
    public void flushPipeline() {
        fetchStage.flush();
        decodeStage.flush();
        
        for (ALU alu : alus) {
            alu.flush();
        }
        
        for (LoadStoreUnit lsUnit : lsUnits) {
            lsUnit.flush();
        }
        
        for (BranchUnit branchUnit : branchUnits) {
            branchUnit.flush();
        }
    }
    
    /**
     * Check if the program has finished
     * executing (all stages empty)
     * @return True if finished
     */
    protected boolean checkProgramFinished() {
        if (!fetchStage.isEmpty()) {
            return false;
        }
        
        if (!decodeStage.isEmpty()) {
            return false;
        }
        
        for (ALU alu : alus) {
            if (!alu.isIdle()) {
                return false;
            }
        }
        
        for (LoadStoreUnit lsUnit : lsUnits) {
            if (!lsUnit.isIdle()) {
                return false;
            }
        }
        
        for (BranchUnit branchUnit : branchUnits) {
            if (!branchUnit.isIdle()) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Get whether speculating
     * @return True if speculating
     */
    public boolean getSpeculating() {
        return speculating;
    }
    
    /**
     * Start speculating
     * @param speculateFailAddr Fail address to branch
     * to if speculation incorrect
     */
    public void startSpeculating(int speculateFailAddr) {
        this.speculating = true;
        this.speculateFailAddr = speculateFailAddr;
    }
    
    /**
     * Stop speculating
     */
    public void stopSpeculating() {
        this.speculating = false;
        this.speculateFailAddr = null;
    }
    
    /**
     * Return speculate fail address
     * @return Speculate fail address
     */
    public int getSpeculateFailAddress() {
        return speculateFailAddr;
    }
    
    /**
     * Get a reference to the register file
     * @return Reference to register file
     */
    public RegisterFile getRegisterFile() {
        return registerFile;
    }
    
    /**
     * Get a reference to the scoreboard
     * @return Reference to scoreboard
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
    
    /**
     * Get a reference to the reorder buffer
     * @return Reference to reorder buffer
     */
    public ReorderBuffer getReorderBuffer() {
        return reorderBuffer;
    }
    
    /**
     * Get a reference to the branch predictor
     * @return Reference to branch predictor
     */
    public BranchPredictor getBranchPredictor() {
        return branchPredictor;
    }
    
    /**
     * Get a reference to the ALUs
     * @return Reference to ALUs
     */
    public ALU[] getALUs() {
        return alus;
    }
    
    /**
     * Get a reference to the Load store units
     * @return Reference to Load store units
     */
    public LoadStoreUnit[] getLoadStoreUnits() {
        return lsUnits;
    }
    
    /**
     * Get a reference to the Branch units
     * @return Reference to branch units
     */
    public BranchUnit[] getBranchUnits() {
        return branchUnits;
    }
    
}
