package com.fitzgerald.simulator.instruction;

public class Cmp extends LogicalInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 2546816640284639270L;

    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        int result;
        
        if (srcData1 > srcData2) {
            result = 1;
        } else if (srcData1 == srcData2) {
            result = 0;
        } else { // srcInt1 < srcInt2
            result = -1;
        }
        
        return result;
    }
    
}
