package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReorderBuffer;
import com.fitzgerald.simulator.processor.Scoreboard;

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
        Scoreboard scoreboard = processor.getScoreboard();
        
        ROBEntry entry;
        
        while ((entry = reorderBuffer.attemptRetire()) != null) {
            RegisterFile registerFile = processor.getRegisterFile();
            
            /*
             * Forward result to reservation stations
             * and update scoreboard
             */
            entry.handleFinish(reorderBuffer, scoreboard);
            
            // Perform write to registers
            entry.writeBack(registerFile);
        }
    }

    @Override
    public void flush() {}
    
}
