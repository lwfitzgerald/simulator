package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class St extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7554310606728502465L;

    @Override
    protected boolean conditional() {
        // Always execute
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Register containing value to store
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand1)).getCurrentValue();
        
        // Register containing base address
        byte[] sourceData2 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        
        // Deep copy of the register values to unlink
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2.clone());
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        int memoryLocation = Util.bytesToInt(executeStage.getSourceData2()) + Util.bytesToInt(operand3);
        byte[] data = executeStage.getSourceData1();
        
        boolean storeResult = memoryController.store(memoryLocation, data);
        
        if (!storeResult) {
            // Needs more cycles
            return false;
        }
        
        // Store completed successfully
        return true;
    }

    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Absolute
        return labelAddr;
    }

    @Override
    public String toString() {
        return "ST r" + Util.bytesToInt(operand1) + 
               ", r" + Util.bytesToInt(operand2) +
               ", " + Util.bytesToInt(operand3);
    }

}
