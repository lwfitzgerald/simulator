package com.fitzgerald.simulator.instruction;

public class Bgeq extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3474538516805884933L;

    @Override
    public boolean isUnconditional() {
        return false;
    }
    
    @Override
    public int getBranchAddress(int currentPC) {
        return operand3;
    }
    
    @Override
    public boolean branchCondition(Integer srcData1, Integer srcData2) {
        return srcData1 >= srcData2;
    }
}
