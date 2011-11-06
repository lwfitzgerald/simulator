package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.PipelineLatch;
import com.fitzgerald.simulator.processor.RegisterFile;

public class J extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1871790764749534500L;

    @Override
    protected boolean conditional() {
        // Unconditional jump, always execute!
        return true;
    }
    
    public void decode(RegisterFile registerFile, PipelineLatch decodeLatch) {
        // No latch changes required
    }

    @Override
    protected void executeOperation(RegisterFile registerFile, PipelineLatch decodeLatch, PipelineLatch executeLatch) {
        // TODO: Set PC here?
        // Not sure how this is going to work yet
    }

}
