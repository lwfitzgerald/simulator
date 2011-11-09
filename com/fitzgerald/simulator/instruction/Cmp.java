package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Cmp extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 2546816640284639270L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }
    
    @Override
    protected boolean conditional() {
        // Always execute
        return true;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        byte[] sourceData2 = registerFile.getRegister(Util.bytesToInt(operand3)).getCurrentValue();
        
        /*
         * Do a deep copy of the source data and store the
         * reference as the next source data value for this
         * instruction
         */
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2.clone());
    }

    @Override
    protected boolean executeOperation(RegisterFile registerFile, ALU alu,
            MemoryController memoryController, ExecuteStage executeStage) {

        byte[] result = alu.performOperation(executeStage);
        
        // Set value as register's next value
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(result);
        
        // Completes in 1 cycle so return true
        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        int srcInt1 = Util.bytesToInt(executeStage.getSourceData1());
        int srcInt2 = Util.bytesToInt(executeStage.getSourceData2());
        
        int result;
        
        if (srcInt1 > srcInt2) {
            result = 1;
        } else if (srcInt1 == srcInt2) {
            result = 0;
        } else { // srcInt1 < srcInt2
            result = -1;
        }
        
        return Util.intToBytes(result);
    }
    
    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Not applicable
        return -1;
    }

    @Override
    public String toString() {
        return "CMP r" + Util.bytesToInt(operand1) +
               ", r" + Util.bytesToInt(operand2) +
               ", r" + Util.bytesToInt(operand3);
    }

}
