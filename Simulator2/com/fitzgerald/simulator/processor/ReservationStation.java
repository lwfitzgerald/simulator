package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;

public class ReservationStation {
    
    protected boolean srcData1Ready = false;
    protected boolean srcData2Ready = false;
    protected boolean destReady = false;
    
    protected Integer srcData1 = null;
    protected Integer srcData2 = null;
    protected Integer dest = null;
    
    protected Instruction instruction = null;
    protected ROBEntry robEntry = null;    
    
    /**
     * Store an instruction in the reservation station
     * @param instruction Instruction
     */
    public void issueInstruction(Instruction instruction,
            RegisterFile registerFile, Scoreboard scoreboard,
            ReorderBuffer reorderBuffer) {
        
        // Reset status
        reset();
        
        // Create reorder buffer entry
        robEntry = reorderBuffer.addEntry(instruction);

        this.instruction = instruction;

        // Attempt operand fetch and claim destination register
        update(registerFile, scoreboard);
    }
    
    /**
     * Attempt to fetch operands and claim destination register
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     */
    public void update(RegisterFile registerFile, Scoreboard scoreboard) {
        if (instruction != null) {
            instruction.updateReservationStation(registerFile, scoreboard, this);
        }
    }
    
    /**
     * Returns whether the instruction in this reservation
     * station is ready for dispatch to an execution unit
     * @return True if instruction is ready for dispatch
     */
    public boolean isReadyForDispatch() {
        return srcData1Ready && srcData2Ready && destReady;
    }
    
    /**
     * Get the required type of Execution unit
     * @return Type of execution unit required
     */
    public InstructionType getRequiredExecutionType() {
        return instruction.getType();
    }
    
    /**
     * Returns if this reservation station is
     * currently empty
     * @return True if empty
     */
    public boolean isEmpty() {
        return instruction == null;
    }
    
    /**
     * Reset this reservation station (to empty)
     */
    protected void reset() {
        this.instruction = null;
        this.srcData1Ready = false;
        this.srcData2Ready = false;
        this.destReady = false;
        this.srcData1 = null;
        this.srcData2 = null;
        this.dest = null;
    }
    
    /**
     * Get the instruction
     * @return Instruction
     */
    public Instruction getInstruction() {
        return instruction;
    }
    
    /**
     * Get the ROB Entry reference
     * @return ROB Entry reference
     */
    public ROBEntry getRobEntry() {
        return robEntry;
    }
    
    /**
     * Get source data 1
     * @return Source data 1 or null if N/A
     */
    public Integer getSourceData1() {
        return srcData1;
    }
    
    /**
     * Set source data 1
     * @param value New value
     */
    public void setSourceData1(int value) {
        this.srcData1 = value;
    }
    
    /**
     * Set source data 1 as ready
     */
    public void setSourceData1Ready() {
        this.srcData1Ready = true;
    }
    
    /**
     * Get source data 2
     * @return Source data 2 or null if N/A
     */
    public Integer getSourceData2() {
        return srcData2;
    }
    
    /**
     * Set source data 2
     * @param value New value
     */
    public void setSourceData2(int value) {
        this.srcData2 = value;
    }
    
    /**
     * Set source data 2 as ready
     */
    public void setSourceData2Ready() {
        this.srcData2Ready = true;
    }
    
    /**
     * Get destination
     * @return Destination or null if N/A
     */
    public Integer getDestination() {
        return dest;
    }
    
    /**
     * Set destination
     * @param value New value
     */
    public void setDestination(int value) {
        this.dest = value;
    }
    
    /**
     * Set destination as ready
     */
    public void setDestinationReady() {
        this.destReady = true;
    }
    
}
