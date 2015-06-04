package com.fitzgerald.simulator.instruction;

public class Vecadd extends VectorInstruction {
    
    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 6739187082706485856L;

    @Override
    public int getVectorCyclesRequired() {
        return 1;
    }
    
    @Override
    public Integer vectorOperation(Integer srcData1, Integer srcData2,
            Integer srcData3, Integer srcData4) {
        
        return srcData1 + srcData2 + srcData3 + srcData4;
    }
    
}
