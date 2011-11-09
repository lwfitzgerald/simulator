package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
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
    public int getALUCyclesRequired() {
        // 1 cycle required for calculating memory address
        return 1;
    }
    
    @Override
    protected boolean conditional() {
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        // Register containing base
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        
        // Deep copy of the register value to unlink
        decodeStage.setSourceData1(sourceData1.clone());
        
        // Immediate offset
        decodeStage.setSourceData2(operand3);
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
        
        // Then do the actual load
        byte[] loadResult = memoryController.load(Util.bytesToInt(executeStage.getBuffer()));
        
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
        
        // Don't forget to set the buffer back to null!
        executeStage.setBuffer(null);
        
        return true;
    }
    
    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        int base = Util.bytesToInt(executeStage.getSourceData1());
        int offset = Util.bytesToInt(executeStage.getSourceData2());
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
        return "LD r" + Util.bytesToInt(operand1) + 
               ", r" + Util.bytesToInt(operand2) +
               ", " + Util.bytesToInt(operand3);
    }

}
