package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.VectorInstruction;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;

public class VectorUnit extends ExecutionUnit {

    protected Integer vectorSrcData3 = null;
    protected Integer vectorSrcData4 = null;
    
    public void startExecuting(Instruction instruction, Integer srcData1,
            Integer srcData2, Integer vectorSrcData3, Integer vectorSrcData4,
            Integer dest, ROBEntry robEntry) {
    
        super.startExecuting(instruction, srcData1, srcData2, dest, robEntry);
        
        this.vectorSrcData3 = vectorSrcData3;
        this.vectorSrcData4 = vectorSrcData4;
    }
    
    public VectorUnit(Processor processor) {
        super(processor);
    }

    @Override
    protected void performOperation() {
        VectorInstruction vInstruction = (VectorInstruction) instruction;
        
        if (ticksRemaining == 0) {
            ticksRemaining = vInstruction.getVectorCyclesRequired();
            return;
        }
        
        ticksRemaining--;
        
        if (ticksRemaining != 0) {
            // More cycles required, return
            return;
        }
        
        // Get result
        Integer result = vInstruction.vectorOperation(srcData1, srcData2,
                vectorSrcData3, vectorSrcData4);
        
        // Update ROB
        finishedExecuting(result);
    }
    
    @Override
    public void flush() {
        super.flush();
        
        this.vectorSrcData3 = null;
        this.vectorSrcData4 = null;
    }

}
