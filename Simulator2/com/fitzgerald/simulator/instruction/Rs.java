package com.fitzgerald.simulator.instruction;

public class Rs extends Ls {
    
    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -8120615838348681295L;

    @Override
    public Integer aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 >>> srcData2;
    }
    
}
