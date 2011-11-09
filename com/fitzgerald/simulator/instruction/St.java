package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class St extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7554310606728502465L;

    @Override
    public int getALUCyclesRequired() {
        // 1 cycle required for calculating address
        return 1;
    }
    
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
        
        decodeStage.setSourceData3(operand3);
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, ALU alu,
            MemoryController memoryController, ExecuteStage executeStage) {
        
        if (executeStage.getBuffer() == null) {
            // First calculate the address
            executeStage.setBuffer(alu.performOperation(executeStage));
            
            // Return false to continue remaining cycles
            return false;
        }
        
        if (executeStage.getBuffer() == null) {
            // First calculate the address
            executeStage.setBuffer(alu.performOperation(executeStage));
            
            // Return false to continue remaining cycles
            return false;
        }

        byte[] data = executeStage.getSourceData1();
        int address = Util.bytesToInt(executeStage.getBuffer());
        boolean storeResult = memoryController.store(address, data);
        
        if (!storeResult) {
            // Needs more cycles
            return false;
        }
        
        // Don't forget to set the buffer back to null!
        executeStage.setBuffer(null);
        
        // Store completed successfully
        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        int base = Util.bytesToInt(executeStage.getSourceData2());
        int offset = Util.bytesToInt(operand3);
        int result = base + offset;
        
        return Util.intToBytes(result);
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
