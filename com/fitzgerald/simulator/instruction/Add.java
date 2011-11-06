package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.PipelineLatch;
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
    public void decode(RegisterFile registerFile, PipelineLatch decodeLatch) {
        byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        byte[] sourceData2 = registerFile.getRegister(Util.bytesToInt(operand3)).getCurrentValue();
        
        /*
         * Do a deep copy of the source data and store the
         * reference as the next source data value for this
         * instruction
         */
        decodeLatch.setNextSourceData1(sourceData1.clone());
        decodeLatch.setNextSourceData1(sourceData2.clone());
    }

    @Override
    protected void executeOperation(RegisterFile registerFile,
            PipelineLatch decodeLatch, PipelineLatch executeLatch) {
        // TODO Auto-generated method stub

    }

}
