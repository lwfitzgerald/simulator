package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
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
         * We are now +1 down the pipeline!
         * 
         * Do a deep copy to unlink the value from the register value
         */
        int fetchPC = Util.bytesToInt(registerFile.getRegister(Processor.PC_REG).getCurrentValue());
        int actualPC = fetchPC - 4;
        decodeStage.setSourceData2(Util.intToBytes(actualPC));
        
        // Offset
        decodeStage.setSourceData1(operand1);
    }

    @Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, MemoryController memoryController, ExecuteStage executeStage) {
        
        int currentPC = Util.bytesToInt(executeStage.getSourceData1());
        int newPC = currentPC + Util.bytesToInt(executeStage.getSourceData2());
        
        registerFile.getRegister(Processor.PC_REG).setNextValue(Util.intToBytes(newPC));
        
        // Flush the pipeline
        processor.flushPipeline();
        
        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        // Not applicable
        return null;
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
