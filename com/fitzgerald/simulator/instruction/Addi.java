package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Addi extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3348748444887990566L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        byte[] sourceData2 = operand3;
        
        /*
         * Do a deep copy of the source data and store the
         * reference as the next source data value for this
         * instruction
         */
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2);
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, MemoryController memoryController, ExecuteStage executeStage) {
        
        byte[] result = alu.performOperation(executeStage);
        
        // Set value as register's next value
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(result);
        
        // Addi always takes one cycle
        return true;
    }
    
    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        int srcInt1 = Util.bytesToInt(executeStage.getSourceData1());
        int srcInt2 = Util.bytesToInt(executeStage.getSourceData2());
        int result = srcInt1 + srcInt2;
        
        return Util.intToBytes(result);
    }
    
    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Absolute
        return labelAddr;
    }

    @Override
    public String toString() {
        return "ADDI r" + Util.bytesToInt(operand1) + 
               ", r" + Util.bytesToInt(operand2) + 
               ", " + Util.bytesToInt(operand3);
    }

}
