package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class B extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3946843299323568039L;

    @Override
    protected boolean conditional() {
        // Always execute
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Nothing to do
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        registerFile.getRegister(Processor.PC_REG).setNextValue(operand1);
        
        return true;
    }

    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Absolute jump
        return labelAddr;
    }

    @Override
    public String toString() {
        return "B " + Util.bytesToInt(operand1);
    }

}
