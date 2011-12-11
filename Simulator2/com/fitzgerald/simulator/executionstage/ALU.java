package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.ALUInstruction;

public class ALU extends ExecutionUnit {
    
    @Override
    protected void performOperation() {
        ALUInstruction aluInstruction = (ALUInstruction) instruction;
        
        if (ticksRemaining > 1) {
            // More cycles required, decrement and return
            ticksRemaining--;
            return;
        }
        
        if (ticksRemaining == 0) {
            if ((ticksRemaining = aluInstruction.getALUCyclesRequired()) != 1) {
                // More cycles required, decrement and return
                ticksRemaining--;
                return;
            }
            
            // Complete so mark as so!
            ticksRemaining = 0;
        }
        
        // Calculate result
        int result = aluInstruction.aluOperation(srcData1, srcData2);
        
        // Update ROB
        finishedExecuting(result);
    }
    
}