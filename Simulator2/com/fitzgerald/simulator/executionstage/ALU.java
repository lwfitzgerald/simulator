package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.ALUInstruction;

public class ALU {
    
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
     * Perform an ALU operation
     * @param instruction Instruction
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @return Result or null if more cycles required
     * for the operation to complete
     */
    public Integer performOperation(ALUInstruction instruction,
            Integer srcData1, Integer srcData2) {
        
        if (ticksRemaining > 1) {
            // More cycles required, decrement and return
            ticksRemaining--;
            return null;
        }
        
        if (ticksRemaining == 0) {
            if ((ticksRemaining = instruction.getALUCyclesRequired()) != 1) {
                // More cycles required, decrement and return
                ticksRemaining--;
                return null;
            }
            
            // Complete so mark as so!
            ticksRemaining = 0;
        }
        
        return instruction.aluOperation(srcData1, srcData2);
    }
    
}
