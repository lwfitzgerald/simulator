package com.fitzgerald.simulator.instruction;

public class J extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1871790764749534500L;
    
    @Override
    public boolean isUnconditional() {
        return true;
    }
    
    @Override
    public int getBranchAddress(int currentPC) {
        return currentPC + operand1;
    }
    
    @Override
    public boolean branchCondition(Integer srcData1, Integer srcData2) {
        // Unconditional branch
        return true;
    }

}
