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
        // 1 cycle needed to calculate branch address
        return 1;
    }
    
    @Override
    protected boolean conditional() {
        // Unconditional jump, always execute!
        return true;
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
    protected boolean executeOperation(RegisterFile registerFile, ALU alu,
            MemoryController memoryController, ExecuteStage executeStage) {
        
        if (executeStage.getBuffer() == null) {
            // First calculate the branch address
            executeStage.setBuffer(alu.performOperation(executeStage));
            
            // Return false to force second cycle
            return false;
        }
        
        // Then return (simulating 2 cycle requirement)
        registerFile.getRegister(Processor.PC_REG).setNextValue(executeStage.getBuffer());
        
        // Don't forget to set the buffer back to null!
        executeStage.setBuffer(null);
        
        return true;
    }

    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        int currentPC = Util.bytesToInt(executeStage.getSourceData1());
        int newPC = currentPC + Util.bytesToInt(executeStage.getSourceData2());
        
        return Util.intToBytes(newPC);
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
