package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

/**
 * Perform an indexed load from memory using a register operand
 * as the base address and an immediate operand as the offset
 */
public class Ld extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7256535789909345398L;

    @Override
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Register containing base
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        
        // Deep copy of the register value to unlink
        decodeStage.setSourceData1(sourceData1.clone());
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        int memoryLocation = Util.bytesToInt(executeStage.getSourceData1()) + Util.bytesToInt(operand3);
        
        byte[] loadResult = memoryController.load(memoryLocation);
        
        if (loadResult == null) {
            // Needs more cycles
            return false;
        }
        
        /*
         * Load completed successfully
         * 
         * Do a deep copy of the result to unlink from memory
         */
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(loadResult.clone());
        
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
        return "LD r" + Util.bytesToInt(operand1) + 
               ", r" + Util.bytesToInt(operand2) +
               ", " + Util.bytesToInt(operand3);
    }

}
