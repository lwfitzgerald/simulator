package com.fitzgerald.simulator.processor;

import java.util.Queue;

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
     */
    public static final int MAX_REORDER_BUFFER_SIZE = 16;
    
    /**
     * Number of instructions fetched/decoded
     * per cycle
     */
    public static final int FETCH_DECODE_WIDTH = 3;
    
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
    protected Memory memory;
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
    
    /*
     * Counters
     */
    protected int cycleCount = 0;
    protected int executedCount = 0;
    protected int incorrectDirectionCount = 0;
    
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
     * Holds the address of the branch
     * we're speculating over
     */
    protected Integer speculateBranchAddr;
    
    /**
     * Create a new processor
     * @param program Program to execute
     * @param branchTable Whether or not to use the branch
     * table prediction mechanism
     */
    public Processor(Program program, boolean branchTable) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memory = new Memory();
        this.scoreboard = new Scoreboard();
        
        // Initialise reservation stations
        initReservationStations();
        
        this.reorderBuffer = new ReorderBuffer();
        this.branchPredictor = new BranchPredictor(branchTable);
        
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
        cycleCount++;
        
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
        
        printState();
        
        // Copy instructions from fetch to decode if needed
        fetchStage.finishStep(decodeStage);
        
        // Check if the program has finished
        if (checkProgramFinished()) {
            printSummary();
            return false;
        }
        
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
     * @param previousFetches Set of already fetched RSs
     * @return A ready reservation station or null if
     * none are ready
     */
    public ReservationStation getReadyReservationStation(
            InstructionType instructionType, Queue<ReservationStation> previousFetches) {
        
        outerLoop:
        for (ReservationStation rs : reservationStations) {
            if (rs.isReadyForDispatch()
                    && rs.getRequiredExecutionType() == instructionType) {
                
                for (ReservationStation previousFetch : previousFetches) {
                    // Don't return previously fetched RSs
                    if (rs == previousFetch) {
                        continue outerLoop;
                    }
                }
                
                if (instructionType != InstructionType.LOADSTORE) {
                    return rs;
                }
                
                // Special behaviour for load stores
                if (rs.isLoadStoreReady(reorderBuffer)) {
                    return rs;
                }
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
     * Approve all speculative instructions
     * in the pipeline
     */
    public void approveSpeculative() {
        fetchStage.approveSpeculative();
        decodeStage.approveSpeculative();
        
        reorderBuffer.approveSpeculative();
    }
    
    /**
     * Flush all pipeline stages
     */
    public void flushPipeline() {
        fetchStage.flush();
        decodeStage.flush();
        scoreboard.flush();
        
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
        
        if (!reorderBuffer.isEmpty()) {
            return false;
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
    public void startSpeculating(int speculateFailAddr, int speculateBranchAddr) {
        this.speculating = true;
        this.speculateFailAddr = speculateFailAddr;
        this.speculateBranchAddr = speculateBranchAddr;
    }
    
    /**
     * Stop speculating
     */
    public void stopSpeculating() {
        this.speculating = false;
        this.speculateFailAddr = null;
        this.speculateBranchAddr = null;
    }
    
    /**
     * Return speculate fail address
     * @return Speculate fail address
     */
    public int getSpeculateFailAddress() {
        return speculateFailAddr;
    }
    
    /**
     * Return speculate branch address
     * @return Speculate branch address
     */
    public int getSpeculateBranchAddr() {
        return speculateBranchAddr;
    }
    
    /**
     * Increment the counter for the number
     * of executed instructions
     */
    public void incrementExecutedCount() {
        executedCount++;
    }
    
    /**
     * Increment the counter for the number
     * of incorrect branch directions
     */
    public void incrementIncorrectDirectionCount() {
        incorrectDirectionCount++;
    }
    
    /**
     * Get a reference to the register file
     * @return Reference to register file
     */
    public RegisterFile getRegisterFile() {
        return registerFile;
    }
    
    /**
     * Get a reference to memory
     * @return Reference to memory
     */
    public Memory getMemory() {
        return memory;
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
    
    /**
     * Print the state of the processor
     */
    protected void printState() {
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Cycle " + cycleCount);
        System.out.println("-----------\n");
        printRegisters();
        printFetchAndDecode();
        printExecutionUnits();
        printReservationStations();
        printROB();
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------");
    }
    
    protected void printSummary() {
        System.out.println("\nSummary:");
        System.out.println("--------");
        System.out.println("Cycles: " + cycleCount);
        System.out.println("Executed instructions: " + executedCount);
        System.out.println("Branches incorrectly predicted: " + incorrectDirectionCount);
    }
    
    /**
     * Print output for registers
     */
    protected void printRegisters() {
        System.out.println("Registers:");
        
        for (int i=0; i < NUM_REGISTERS; i++) {
            System.out.print("r" + i + ": " + registerFile.getRegister(i).getValue() + "\t");
            
            if ((i+1) % 8 == 0) {
                System.out.println();
            }
        }
        
        System.out.println();
    }
    
    /**
     * Print output for fetch and decode stages
     */
    protected void printFetchAndDecode() {
        System.out.println("Fetch:");
        
        System.out.println(fetchStage);
        
        System.out.println();
        
        System.out.println("Decode:");
        
        System.out.println(decodeStage);
        
        System.out.println();
    }
    
    protected void printExecutionUnits() {
        System.out.println("ALUs:");
        
        for (int i=0; i < NUM_ALUS; i++) {
            System.out.println((i+1) + ": " + alus[i]);
        }
        
        System.out.println("\nLoad Store units:");
        
        for (int i=0; i < NUM_LOAD_STORE_UNITS; i++) {
            System.out.println((i+1) + ": " + lsUnits[i]);
        }
        
        System.out.println("\nBranch units");
        
        for (int i=0; i < NUM_BRANCH_UNITS; i++) {
            System.out.println((i+1) + ": " + branchUnits[i]);
        }
        
        System.out.println();
        
    }
    
    /**
     * Print output for reservation stations
     */
    protected void printReservationStations() {
        System.out.println("Reservation stations:");
        
        for (int i=0; i < NUM_RESERVATION_STATIONS / 2; i++) {
            System.out.print((i+1) + ": " + reservationStations[i]);
            System.out.println("\t" + (i+1 + Processor.NUM_RESERVATION_STATIONS / 2) + ": " + reservationStations[i + Processor.NUM_RESERVATION_STATIONS / 2]);
        }
        
        System.out.println();
    }

    /**
     * Print output for the reorder buffer
     */
    protected void printROB() {
        System.out.println("Reorder Buffer:");
        
        System.out.println(reorderBuffer);
    }
    
}
