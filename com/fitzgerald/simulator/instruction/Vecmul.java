package com.fitzgerald.simulator.instruction;

public class Vecmul extends VectorInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -5477629307944638634L;

    @Override
    public int getVectorCyclesRequired() {
        return 2;
    }
    
    @Override
    public Integer vectorOperation(Integer srcData1, Integer srcData2,
            Integer srcData3, Integer srcData4) {
        
        return srcData1 * srcData2 * srcData3 * srcData4;
    }
    
}
