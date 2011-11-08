package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
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
    protected boolean conditional() {
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Register containing base
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        // Immediate operand for offset
        byte[] sourceData2 = operand3;
        
        // Deep copy of the register value to unlink
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2);
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        int memoryLocation = Util.bytesToInt(executeStage.getSourceData1()) + Util.bytesToInt(executeStage.getSourceData1());
        
        byte[] loadResult = memoryController.load(memoryLocation);
        
        if (loadResult == null) {
            // Needs more cycles
            return false;
        } else {
            /*
             * Load completed successfully
             * 
             * Do a deep copy of the result to unlink from memory
             */
            registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(loadResult.clone());
            
            return true;
        }
    }
    
    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Absolute
        return labelAddr;
    }

    @Override
    public String toString() {
        return "LD r" + Util.bytesToInt(operand1) + 
               ", " + Util.bytesToInt(operand2);
    }

}
