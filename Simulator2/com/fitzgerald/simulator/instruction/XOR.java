package com.fitzgerald.simulator.instruction;

public class XOR extends LogicalInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -6885854347843975825L;

    @Override
    public Integer aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 ^ srcData2;
    }

}
