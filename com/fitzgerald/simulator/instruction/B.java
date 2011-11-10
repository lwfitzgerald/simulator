package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
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
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Nothing to do
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        
        registerFile.getRegister(Processor.PC_REG).setNextValue(operand1);
        
        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        // Not applicable
        return null;
    }
    
    @Override
    public boolean branchCondition(ExecuteStage executeStage) {
        // Unconditional branch!
        return true;
    }
    
    @Override
    public byte[] branchCalculation(ExecuteStage executeStage) {
        return operand1;
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
