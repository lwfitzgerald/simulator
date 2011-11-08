package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Stc extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7387641905906737577L;

    @Override
    protected boolean conditional() {
        // Always execute
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand1)).getCurrentValue();
        
        // Deep copy of the register value to unlink
        decodeStage.setSourceData1(sourceData1.clone());
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        int memoryLocation = Util.bytesToInt(executeStage.getSourceData1()) + Util.bytesToInt(operand2);
        byte[] data = operand3;
        
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
        return "STC r" + Util.bytesToInt(operand1) +
               ", " + Util.bytesToInt(operand2) +
               ", " + Util.bytesToInt(operand3);
    }

}
