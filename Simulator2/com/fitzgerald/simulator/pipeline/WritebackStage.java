package com.fitzgerald.simulator.pipeline;

import java.util.Iterator;

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
        RegisterFile registerFile = processor.getRegisterFile();
        
        boolean ready = true;
        
        Iterator<ROBEntry> itr = reorderBuffer.iterator();
        
        while (itr.hasNext()) {
            ROBEntry entry = itr.next();
            
            if (entry.isFinished()) {
                entry.forwardResult(processor);
                
                if (ready && entry.isReadyToRetire()) {
                    /*
                     * Do speculation update or
                     * update scoreboard
                     */
                    entry.handleRetire(processor, itr);
                    
                    // Perform write to registers
                    entry.writeBack(registerFile);
                } else {
                    ready = false;
                }
            } else {
                ready = false;
            }
        }
    }

    @Override
    public void flush() {}
    
}
