package com.fitzgerald.simulator.instruction;

public class B extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3946843299323568039L;

    @Override
    public boolean isUnconditional() {
        return true;
    }
    
    @Override
    public int getBranchAddress(int currentPC) {
        return operand1;
    }

    @Override
    public boolean branchCondition(Integer srcData1, Integer srcData2) {
        // Unconditional branch
        return true;
    }

}
