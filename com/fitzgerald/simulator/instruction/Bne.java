package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Bne extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7510274209185575190L;

    @Override
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand1)).getCurrentValue();
        byte[] sourceData2 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();

        // Do a deep copy to unlink from the register value
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2.clone());
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController,
            ExecuteStage executeStage) {

        byte[] result = branchUnit.performBranch(executeStage);

        if (result != null) {
            // Branch unit has returned an address, set it
            registerFile.getRegister(Processor.PC_REG).setNextValue(result);
        }

        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        // Not applicable
        return null;
    }

    @Override
    public boolean branchCondition(ExecuteStage executeStage) {
        int src1 = Util.bytesToInt(executeStage.getSourceData1());
        int src2 = Util.bytesToInt(executeStage.getSourceData2());

        return src1 != src2;
    }

    @Override
    public byte[] branchCalculation(ExecuteStage executeStage) {
        return operand3;
    }

    @Override
    public String toString() {
        return "BNE r" + Util.bytesToInt(operand1) +
                ", r" + Util.bytesToInt(operand2) +
                ", " + Util.bytesToInt(operand3);
    }

}