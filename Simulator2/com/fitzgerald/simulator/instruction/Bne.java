package com.fitzgerald.simulator.instruction;

public class Bne extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7510274209185575190L;

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
        return srcData1 != srcData2;
    }

}
