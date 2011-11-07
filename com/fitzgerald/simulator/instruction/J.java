package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

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
    
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // No latch changes required
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        // TODO: Set PC here?
        // Not sure how this is going to work yet
        return true;
    }

    @Override
    public String toString() {
        return "J " + Util.bytesToInt(operand1);
    }

}
