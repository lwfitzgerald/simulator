package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.pipeline.ExecuteStage;

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
     * @param executeStage Execution stage reference
     * @return Result or null if more cycles required
     * for the operation to complete
     */
    public byte[] performOperation(ExecuteStage executeStage) {
        Instruction instruction = executeStage.getInstruction();
        
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
            
            // Single cycle instruction, so mark as done
            ticksRemaining = 0;
        }
        
        return instruction.aluOperation(executeStage);
    }
    
}
