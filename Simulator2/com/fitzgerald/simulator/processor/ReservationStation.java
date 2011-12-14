package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction.LoadStoreType;

public class ReservationStation {
    
    protected boolean srcData1Ready = false;
    protected boolean srcData2Ready = false;
    protected boolean destReady = false;
    
    protected Integer srcData1 = null;
    protected Integer srcData2 = null;
    protected Integer dest = null;
    
    protected Instruction instruction = null;
    protected ROBEntry robEntry = null;    
    
    protected Processor processor;
    
    /**
     * Create new reservation station
     * @param processor Processor reference
     */
    public ReservationStation(Processor processor) {
        this.processor = processor;
    }
    
    /**
     * Store an instruction in the reservation station
     * @param instruction Instruction
     * @param branchAddr Calculated branch address
     * @param speculating Whether currently speculating
     */
    public void issueInstruction(Instruction instruction, Integer branchAddr, boolean speculating) {
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        
        // Reset status
        flush();
        
        // Create reorder buffer entry
        robEntry = reorderBuffer.addEntry(instruction, this, speculating);

        this.instruction = instruction;

        // Attempt operand fetch and claim destination register
        initialSetup(branchAddr);
    }
    
    /**
     * Attempt to fetch operands and claim destination register
     */
    protected void initialSetup(Integer branchAddr) {
        RegisterFile registerFile = processor.getRegisterFile();
        Scoreboard scoreboard = processor.getScoreboard();
        
        instruction.initialSetup(registerFile, scoreboard, robEntry,
                branchAddr, this);
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
     * Return whether the load store instruction
     * in this reservation station is ready for
     * dispatch
     * @param reorderBuffer ROB reference
     * @return True if OK to dispatch
     */
    public boolean isLoadStoreReady(ReorderBuffer reorderBuffer) {
        LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
        
        if (robEntry.isSpeculative()) {
            if (lsInstruction.getLSType() == LoadStoreType.STORE) {
                // NO stores when speculating!
                return false;
            }
            
            /*
             * Otherwise it's a load...
             * 
             * Loads are allowed until there is a write
             * and that will be handled by the regular code below
             */
        }
        
        int addr = lsInstruction.getLSAddress(srcData1, srcData2, dest);
        
        for (ROBEntry entry : reorderBuffer) {
            if (entry == robEntry) {
                // Stop once we hit the current entry
                break;
            }
            
            Instruction entryInstr = entry.getInstruction();
            
            if (entryInstr.getType() == InstructionType.LOADSTORE
                    && !entry.isFinished()) {
                
                /*
                 * There is a loadstore ahead of us which we can't
                 * calculate the address for yet so we don't know
                 * if we can execute
                 */
                if (!entry.isReadyForDispatch()) {
                    return false;
                }
                
                // If it's a memory operation, and it hasn't completed...
                
                LoadStoreInstruction lsEntryInstr = (LoadStoreInstruction) entryInstr;
                
                if (entry.getMemAddr() == addr) {
                    // If it's a memory operation on the same address
                    if (lsEntryInstr.getLSType() == LoadStoreType.STORE) {
                        // Earlier store not completed so we can't read or write
                        return false;
                    }
                    
                    // Otherwise it's a load so only block writes
                    if (lsInstruction.getLSType() == LoadStoreType.STORE) {
                        /*
                         * We're trying to store and there is an earlier
                         * read so we can't store yet
                         */
                        return false;
                    }
                }
            }
        }
        
        return true;
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
    public void flush() {
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
    
    public String toString() {
        if (instruction != null) {
            String src1String = srcData1Ready ? "S1READY" : "S1NOTREADY";
            String src2String = srcData2Ready ? "S2READY" : "S2NOTREADY";
            String destString = destReady ? "DESTREADY" : "DESTNOTREADY";
            return "[[" + instruction + "]," + src1String + "," + src2String + "," + destString + "," + (robEntry.isSpeculative() ? "SP" : "NONSP") + "]";
        } else {
            return "[EMPTY]";
        }
    }
    
}
