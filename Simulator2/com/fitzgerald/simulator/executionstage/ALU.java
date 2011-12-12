package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.ALUInstruction;
import com.fitzgerald.simulator.processor.Processor;

public class ALU extends ExecutionUnit {
    
    /**
     * Create a new ALU
     * @param processor Processor reference
     */
    public ALU(Processor processor) {
        super(processor);
    }

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
