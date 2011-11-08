package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
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
    protected boolean conditional() {
        // Unconditional jump, always execute!
        return true;
    }
    
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
        /*
         * We are now +1 down the pipeline!
         * 
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
    protected boolean executeOperation(RegisterFile registerFile, MemoryController memoryController, ExecuteStage executeStage) {
        int currentPC = Util.bytesToInt(executeStage.getSourceData1());
        int newPC = currentPC + Util.bytesToInt(executeStage.getSourceData2());
        
        registerFile.getRegister(Processor.PC_REG).setNextValue(Util.intToBytes(newPC));
        
        return true;
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
