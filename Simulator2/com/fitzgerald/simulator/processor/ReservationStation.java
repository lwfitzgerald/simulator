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
    
    /**
     * Store an instruction in the reservation station
     * @param instruction Instruction
     */
    public void issueInstruction(Instruction instruction) {
        // Reset status
        reset();
        
        this.instruction = instruction;
    }
    
    /**
     * Returns whether the instruction in this reservation
     * station is ready for dispatch to an execution unit
     * @return True if instruction is ready for dispatch
     */
    public boolean isReadyForDispatch() {
        return srcData1Ready && srcData2Ready;
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
