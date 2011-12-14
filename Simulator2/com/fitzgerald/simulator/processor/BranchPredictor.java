package com.fitzgerald.simulator.processor;

import java.util.HashMap;

public class BranchPredictor {
    
    protected HashMap<Integer, Boolean> previousBranches = new HashMap<Integer, Boolean>();
    
    protected boolean useBranchTable;
    
    public BranchPredictor(boolean useBranchTable) {
        this.useBranchTable = useBranchTable;
    }
    
    /**
     * Predict whether a branch is taken
     * @param address Address of branch
     * @param targetAddress Target address of branch
     * @return True if taken
     */
    public boolean predictBranch(int address, int targetAddress) {
        Boolean takeBranch;
        
        if (useBranchTable) {
            if ((takeBranch = previousBranches.get(address)) == null) {
                takeBranch = predictUnknown(address, targetAddress);
            }
        } else {
            takeBranch = predictUnknown(address, targetAddress);
        }
        
        return takeBranch;
    }
    
    /**
     * Use the branch direction to predict
     * whether it is taken or not
     * @param address Address of branch
     * @param targetAddress Target address of branch
     * @return True if taken
     */
    protected boolean predictUnknown(int address, int targetAddress) {
        if (targetAddress <= address) {
            // Backwards branches are normally taken
            return true;
        } else {
            // Forward branches are normally not taken
            return false;
        }
    }
    
    /**
     * Set the actual direction of a branch
     * @param address Address of branch
     * @param taken True if taken
     */
    public void setTakenDirection(int address, boolean taken) {
        previousBranches.put(address, taken);
    }
    
}
