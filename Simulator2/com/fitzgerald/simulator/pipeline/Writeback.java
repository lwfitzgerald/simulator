package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReorderBuffer;

public class Writeback extends PipelineStage {
    
    /*
     * Createa a new Writeback stage
     */
    protected Writeback(Processor processor) {
        super(processor);
    }

    public void step(Processor processor, RegisterFile registerFile, ReorderBuffer reorderBuffer) {
        ROBEntry entry;
        
        while ((entry = reorderBuffer.attemptRetire()) != null) {
            entry.forwardResult(reorderBuffer);
            
            entry.writeBack();
        }
    }

    @Override
    public void flush() {}
    
}
