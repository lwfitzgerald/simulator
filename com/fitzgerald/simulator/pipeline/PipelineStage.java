package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.Processor;

public abstract class PipelineStage {
    
    protected Processor processor;
    
    protected PipelineStage(Processor processor) {
        this.processor = processor;
    }
    
    /**
     * Flush pipeline stage
     */
    public abstract void flush();
}
