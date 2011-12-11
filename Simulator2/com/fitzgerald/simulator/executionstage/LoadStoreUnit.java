package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.ALUInstruction;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction;

public class LoadStoreUnit extends ExecutionUnit {

    @Override
    protected void performOperation() {
        LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
        
        if (ticksRemaining > 1) {
            // More cycles required, decrement and return
            ticksRemaining--;
            return;
        }
        
        if (ticksRemaining == 0) {
            if ((ticksRemaining = lsInstruction.getALUCyclesRequired()) != 1) {
                // More cycles required, decrement and return
                ticksRemaining--;
                return;
            }
            
            // Complete so mark as so!
            ticksRemaining = 0;
        }
        
        // Calculate result
        int result = lsInstruction.aluOperation(srcData1, srcData2);
        
        // Update ROB
        finishedExecuting(result);
    }

}
