package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;

public class LoadStoreUnit extends ExecutionUnit {

    /**
     * Create a new load store unit
     * @param processor Processor reference
     */
    public LoadStoreUnit(Processor processor) {
        super(processor);
    }

    @Override
    public void startExecuting(Instruction instruction, Integer srcData1,
            Integer srcData2, Integer dest, ROBEntry robEntry) {
        
        super.startExecuting(instruction, srcData1, srcData2, dest, robEntry);
        
        LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
        
        // Also set the memAddr value in the ROB
        robEntry.setMemAddr(lsInstruction.getLSAddress(srcData1, srcData2, dest));
    }
    
    @Override
    protected void performOperation() {
        LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
        
        if (ticksRemaining > 1) {
            // More cycles required, decrement and return
            ticksRemaining--;
            return;
        }
        
        if (ticksRemaining == 0) {
            if ((ticksRemaining = LoadStoreInstruction.NUM_CYCLES_REQUIRED) != 1) {
                // More cycles required, decrement and return
                ticksRemaining--;
                return;
            }
            
            // Complete so mark as so!
            ticksRemaining = 0;
        }
        
        // Get result
        Integer result = lsInstruction.memoryOperation(srcData1, srcData2,
                dest, processor.getMemory());
        
        // Update ROB
        if (result != null) {
            finishedExecuting(result);
        } else {
            finishedExecuting();
        }
    }

}
