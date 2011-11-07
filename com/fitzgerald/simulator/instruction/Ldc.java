package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Ldc extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 8413348152180654390L;

    @Override
    protected boolean conditional() {
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Immediate value to load
        byte[] sourceData1 = operand2;
        
        decodeStage.setSourceData1(sourceData1);
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(executeStage.getSourceData1());
        
        // Load immediate always takes 1 cycle
        return true;
    }

}
