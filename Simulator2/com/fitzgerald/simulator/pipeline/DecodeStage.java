package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReorderBuffer;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class DecodeStage {

    protected Instruction instruction1 = null;
    protected Integer instruction1BranchAddr = null;
    protected Instruction instruction2 = null;
    protected Integer instruction2BranchAddr = null;
    
    public void step(Processor processor, RegisterFile registerFile,
            Scoreboard scoreboard, ReorderBuffer reorderBuffer) {
        
        ReservationStation rs = processor.getFreeReservationStation();
        
        if (rs != null) {
            // Free reservation station
            rs.issueInstruction(instruction1, registerFile, scoreboard, reorderBuffer);
            instruction1 = null;
            instruction1BranchAddr = null;
            
            // Now attempt to issue instruction 2
            
            rs = processor.getFreeReservationStation();
            
            if (rs != null) {
                rs.issueInstruction(instruction2, registerFile, scoreboard, reorderBuffer);
                instruction2 = null;
                instruction2BranchAddr = null;
            } else {
                // Move instruction 2 to 1
                instruction1 = instruction2;
                instruction1BranchAddr = instruction2BranchAddr;
                instruction2 = null;
                instruction2BranchAddr = null;
            }
        }
        
        // Attempt to get all reservation stations ready
        processor.updateAllReservationStations();
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
     * @param instruction1BranchAddr Calculated branch address
     */
    public void setInstruction1(Instruction instruction1, Integer instruction1BranchAddr) {
        this.instruction1 = instruction1;
        this.instruction1BranchAddr = instruction1BranchAddr;
    }
    
    /**
     * Set instruction 2 and calculated branch address
     * @param instruction2 Instruction to set
     * @param instruction2BranchAddr Calculated branch address
     */
    public void setInstruction2(Instruction instruction2, Integer instruction2BranchAddr) {
        this.instruction2 = instruction2;
        this.instruction2BranchAddr = instruction2BranchAddr;
    }
    
}
