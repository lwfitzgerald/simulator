package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReorderBuffer;

public class WritebackStage extends PipelineStage {
    
    /**
     * Create a new Writeback stage
     * @param processor Processor reference
     */
    public WritebackStage(Processor processor) {
        super(processor);
    }

    public void step() {
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        
        ROBEntry entry;
        
        while ((entry = reorderBuffer.attemptRetire()) != null) {
            RegisterFile registerFile = processor.getRegisterFile();
            
            // Forward result to reservation stations
            entry.forwardResult(reorderBuffer);
            
            // Perform write to registers
            entry.writeBack(registerFile);
        }
    }

    @Override
    public void flush() {}
    
}
