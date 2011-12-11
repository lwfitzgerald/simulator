package com.fitzgerald.simulator.processor;

import java.util.LinkedList;
import java.util.List;

import com.fitzgerald.simulator.instruction.Instruction.InstructionType;

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
    
    protected int cycleCount = -1;
    
    /*
     * Pipeline stages
     */
    protected FetchStage fetchStage;
    protected DecodeStage decodeStage;
    protected ExecuteStage executeStage;
    
    public Processor(Program program, Memory memory) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memoryController = new MemoryController(memory);
        this.scoreboard = new Scoreboard();
        
        // Initialise reservation stations
        initReservationStations();
        
        this.reorderBuffer = new ReorderBuffer();
        
        // Initialise pipeline stages
        this.fetchStage = new FetchStage(ui);
        this.decodeStage = new DecodeStage(ui);
        this.executeStage = new ExecuteStage(ui);
        
        // Set program counter to 0
        registerFile.getRegister(PC_REG).setCurrentValue(Util.intToBytes(0));
    }
    
    /**
     * Performs one simulation step
     * @return Boolean representing whether the end
     * of the program has been reached
     */
    public boolean step() {
        // Update cycle count and register UIs
        ui.setCycleCount(++cycleCount);
        registerFile.updateUI();

        fetchStage.step(program, this, registerFile, alu, branchUnit, memoryController);
        
        if (fetchStage.containsArtificialNop() &&
            decodeStage.containsArtificialNop() &&
            executeStage.containsArtificialNop()) {
            
            /*
             * All stages contain artificial Nops so we can halt
             * but first we need to update the UI one last time to
             * clear the last instruction
             */
            decodeStage.updateUI();
            executeStage.updateUI();
            
            return false;
        }
        
        decodeStage.step(program, this, registerFile, alu, branchUnit, memoryController);
        executeStage.step(program, this, registerFile, alu, branchUnit, memoryController);
        
        if (executeStage.isCompleted()) {
            /*
             * Execute stage has completed (require no more cycles)
             * so copy register nexts to currents etc
             */
            finishStep();
        }
        
        return true;
    }
    
    protected void finishStep() {
        if (!pcIncrementedNormally()) {
            flushPipeline();
        }
        
        // In reverse order to maintain data correctness
        decodeStage.copyState(executeStage);
        fetchStage.copyState(decodeStage);
        
        // Set completed to false on all stages
        fetchStage.setCompleted(false);
        decodeStage.setCompleted(false);
        executeStage.setCompleted(false);
        
        // Copy register "next"'s to "current"'s
        registerFile.finishStep();
    }
    
    /**
     * Returns whether the PC has been incremented
     * normally
     * @return True if next PC = curr PC + 4, false
     * otherwise
     */
    protected boolean pcIncrementedNormally() {
        int curr = Util.bytesToInt(registerFile.getRegister(PC_REG).getCurrentValue());
        int next = Util.bytesToInt(registerFile.getRegister(PC_REG).getNextValue());
        
        return next - curr == 4;
    }
    
    /**
     * Initialise the reservation stations
     */
    protected void initReservationStations() {
        this.reservationStations = new ReservationStation[NUM_RESERVATION_STATIONS];
        
        for (int i=0; i < NUM_RESERVATION_STATIONS; i++) {
            this.reservationStations[i] = new ReservationStation();
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
     * Update all reservation stations
     */
    public void updateAllReservationStations() {
        for (ReservationStation rs : reservationStations) {
            rs.update(registerFile, scoreboard);
        }
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
    
}
