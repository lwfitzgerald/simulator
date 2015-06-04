package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.ROBEntry.EntryState;

public abstract class ExecutionUnit {
    
    protected Integer srcData1 = null;
    protected Integer srcData2 = null;
    protected Integer dest = null;
    
    protected Instruction instruction = null;
    
    protected ROBEntry robEntry = null;
    
    protected Processor processor;
    
    /**
     * Ticks remaining until operation completes
     * 
     * Only one operation at once so we only need
     * one tick counter
     * 
     * 0 when there is no operation in progress
     */
    protected int ticksRemaining = 0;
    
    /**
     * Constructor
     * @param processor Processor reference
     */
    protected ExecutionUnit(Processor processor) {
        this.processor = processor;
    }
    
    /**
     * Return whether this execution unit is idle
     * @return True if idle
     */
    public boolean isIdle() {
        return ticksRemaining == 0;
    }
    
    /**
     * Start executing new instruction
     * @param instruction Instruction
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @param dest Destination data
     * @param robEntry ROB entry
     */
    public void startExecuting(Instruction instruction, Integer srcData1,
            Integer srcData2, Integer dest, ROBEntry robEntry) {
        
        this.instruction = instruction;
        this.srcData1 = srcData1;
        this.srcData2 = srcData2;
        this.dest = dest;
        this.robEntry = robEntry;
        
        // Set ROB state to executing
        robEntry.setState(EntryState.EXECUTING);
        
        //System.out.println("Executing " + instruction);
        
        performOperation();
    }
    
    /**
     * Perform the operation of the execution unit
     */
    protected abstract void performOperation();
    
    /**
     * Continue executing the currently
     * executing instruction
     */
    public void continueExecuting() {
        performOperation();
    }
    
    /**
     * Called when the instruction finishes executing
     * to update the ROB
     * @param result Result of operation
     */
    protected void finishedExecuting(int result) {
        // Store in ROB
        robEntry.setResult(result);
        
        // Update the state of the ROB
        finishedExecuting();
    }
    
    /**
     * Called when the instruction finishes executing
     */
    protected void finishedExecuting() {
        robEntry.handleFinish(processor);
        
        flush();
    }
    
    /**
     * Flush this execution unit
     */
    public void flush() {
        srcData1 = null;
        srcData2 = null;
        dest = null;
        instruction = null;
        robEntry = null;
        ticksRemaining = 0;
    }
    
    public String toString() {
        if (instruction == null) {
            return "[EMPTY]";
        }
        
        return "[[" + instruction + "]," + (robEntry.isSpeculative() ? "SP" : "NONSP") + "]";
    }
    
}
