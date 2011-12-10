package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;

public class ROBEntry {
    
    /**
     * Enum describing the state of the instruction
     */
    protected enum EntryState { ISSUED, EXECUTING, FINISHED };
    
    /**
     * Instruction entry is for
     */
    protected Instruction instruction;
    
    /**
     * Value to store in register / memory upon retiring
     */
    protected int result;
    
    /**
     * State of the instruction
     */
    protected EntryState state;
    
    /**
     * Creates a ROBEntry
     * @param instruction Instruction entry is for
     */
    public ROBEntry(Instruction instruction) {
        this.state = EntryState.ISSUED;
    }
    
    /**
     * Returns whether the instruction has finished
     * @return True if finished
     */
    public boolean isFinished() {
        return state == EntryState.FINISHED ? true : false;
    }
    
    /**
     * Set the result data for this ROB entry
     * @param result Result data to store
     */
    public void setResult(int result) {
        this.result = result;
    }
}
