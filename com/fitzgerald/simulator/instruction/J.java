package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class J extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1871790764749534500L;

    @Override
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }
    
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        /*
         * Load the PC register
         * 
         * Do a deep copy to unlink the value from the register value
         */
        int fetchPC = Util.bytesToInt(registerFile.getRegister(Processor.PC_REG).getCurrentValue());
        decodeStage.setSourceData1(Util.intToBytes(fetchPC));
        
        // Offset
        decodeStage.setSourceData2(operand1);
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        
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
        // Unconditional jump
        return true;
    }
    
    @Override
    public byte[] branchCalculation(ExecuteStage executeStage) {
        // Calculate the correct PC due to pipelining
        int correctPC = Util.bytesToInt(executeStage.getSourceData1()) - 4;
        int offset = Util.bytesToInt(executeStage.getSourceData2());
        
        // Apply the offset
        return Util.intToBytes(correctPC + offset);
    }
    
    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Relative jump!
        return labelAddr - instructionAddr;
    }

    @Override
    public String toString() {
        return "J " + Util.bytesToInt(operand1);
    }

}
