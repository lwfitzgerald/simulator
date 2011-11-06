package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Add extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -2371581904490721607L;
    
    @Override
    protected boolean conditional() {
        // TODO Auto-generated method stub
        return false;
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
        decodeStage.setSourceData1(sourceData2.clone());
    }

    @Override
    protected void executeOperation(RegisterFile registerFile, ExecuteStage executeStage) {
        int srcInt1 = Util.bytesToInt(executeStage.getSourceData1());
        int srcInt2 = Util.bytesToInt(executeStage.getSourceData2());
        int result = srcInt1 + srcInt2;

        // Set value as register's next value
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(Util.intToBytes(result));
    }

}
