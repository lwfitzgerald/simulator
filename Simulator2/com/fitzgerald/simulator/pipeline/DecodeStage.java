package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ReservationStation;

public class DecodeStage extends PipelineStage {

    protected Instruction instruction1 = null;
    protected boolean instruction1Speculative;
    protected Integer instruction1BranchAddr = null;
    protected Instruction instruction2 = null;
    protected boolean instruction2Speculative;
    protected Integer instruction2BranchAddr = null;
    
    /**
     * Create a new decode stage
     * @param processor Processor reference
     */
    public DecodeStage(Processor processor) {
        super(processor);
    }
    
    public void step() {
        if (instruction1 != null) {
            ReservationStation rs = processor.getFreeReservationStation();
            
            if (rs != null) {
                // Free reservation station
                rs.issueInstruction(instruction1, instruction1BranchAddr, instruction1Speculative);
                instruction1 = null;
                instruction1BranchAddr = null;
                
                if (instruction2 != null) {
                    // Now attempt to issue instruction 2
                    
                    rs = processor.getFreeReservationStation();
                    
                    if (rs != null) {
                        rs.issueInstruction(instruction2, instruction2BranchAddr, instruction2Speculative);
                        instruction2 = null;
                        instruction2BranchAddr = null;
                    } else {
                        // Move instruction 2 to 1
                        instruction1 = instruction2;
                        instruction1Speculative = instruction2Speculative;
                        instruction1BranchAddr = instruction2BranchAddr;
                        instruction2 = null;
                        instruction2BranchAddr = null;
                    }
                }
            }
        }
    }
    
    /**
     * Return whether instruction slot 1 is free
     * @return True if free
     */
    public boolean instruction1Free() {
        return instruction1 == null;
    }
    
    /**
     * Return whether instruction slot 2 is free
     * @return True if free
     */
    public boolean instruction2Free() {
        return instruction2 == null;
    }
    
    /**
     * Set instruction 1 and calculated branch address
     * @param instruction1 Instruction to set
     * @param speculative Whether instruction is speculative
     * @param branchAddr Calculated branch address
     */
    public void setInstruction1(Instruction instruction1, boolean speculative, Integer branchAddr) {
        this.instruction1 = instruction1;
        this.instruction1Speculative = speculative;
        this.instruction1BranchAddr = branchAddr;
    }
    
    /**
     * Set instruction 2 and calculated branch address
     * @param instruction2 Instruction to set
     * @param speculative Whether instruction is speculative
     * @param branchAddr Calculated branch address
     */
    public void setInstruction2(Instruction instruction2, boolean speculative, Integer branchAddr) {
        this.instruction2 = instruction2;
        this.instruction2Speculative = speculative;
        this.instruction2BranchAddr = branchAddr;
    }
    
    /**
     * Returns whether this pipeline stage is empty
     * @return True if this stage is empty
     */
    public boolean isEmpty() {
        return instruction1Free() && instruction2Free();
    }

    /**
     * Mark speculative instructions
     * as no longer speculative
     */
    public void approveSpeculative() {
        instruction1Speculative = false;
        instruction2Speculative = false;
    }
    
    /**
     * Remove speculative instructions
     */
    public void flushSpeculative() {
        if (instruction1Speculative) {
            instruction1 = null;
            instruction1BranchAddr = null;
        }
        
        if (instruction2Speculative) {
            instruction2 = null;
            instruction2BranchAddr = null;
        }
        
        if (instruction1 == null && instruction2 != null) {
            // Copy 2 -> 1
            instruction1 = instruction2;
            instruction1BranchAddr = instruction2BranchAddr;
            instruction2 = null;
            instruction2BranchAddr = null;
        }
    }
    
    @Override
    public void flush() {
        instruction1 = null;
        instruction1BranchAddr = null;
        instruction2 = null;
        instruction2BranchAddr = null;
    }
    
    public String toString() {
        if (instruction1 == null) {
            return "EMPTY";
        }
        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("1: [[" + instruction1 + "]," + (instruction1Speculative ? "SP" : "NONSP") + "]\n");
        
        if (instruction2 == null) {
            buffer.append("2: [EMPTY]\n");
        } else {
            buffer.append("2: [[" + instruction2 + "]," + (instruction2Speculative ? "SP" : "NONSP") + "]\n");
        }
        
        return buffer.substring(0, buffer.length()-1);
    }
    
}
