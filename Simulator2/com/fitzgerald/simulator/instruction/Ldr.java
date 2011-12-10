package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Ldr extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7013572713573792144L;

    @Override
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Immediate value to load
        byte[] sourceData1 = operand2;
        
        decodeStage.setSourceData1(sourceData1);
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(executeStage.getSourceData1());
        
        // Load immediate always takes 1 cycle
        return true;
    }
    
    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        // Not applicable
        return null;
    }

    @Override
    public boolean branchCondition(ExecuteStage executeStage) {
        // Not applicable
        return false;
    }
    
    @Override
    public byte[] branchCalculation(ExecuteStage executeStage) {
        // Not applicable
        return null;
    }

    @Override
    public String toString() {
        return "LDR r" + Util.bytesToInt(operand1) + 
               ", " + Util.bytesToInt(operand2);
    }

}
