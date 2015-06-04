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
        
        if (ticksRemaining == 0) {
            ticksRemaining = aluInstruction.getALUCyclesRequired();
            return;
        }
        
        ticksRemaining--;
        
        if (ticksRemaining != 0) {
            // More cycles required, return
            return;
        }
        
        // Calculate result
        Integer result = aluInstruction.aluOperation(srcData1, srcData2);
        
        // Update ROB
        if (result != null) {
            finishedExecuting(result);
        } else {
            finishedExecuting();
        }
    }
    
}
