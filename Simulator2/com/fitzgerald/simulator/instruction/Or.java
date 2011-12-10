package com.fitzgerald.simulator.instruction;

public class Or extends LogicalInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -26285003489026726L;

    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 | srcData2;
    }

}
