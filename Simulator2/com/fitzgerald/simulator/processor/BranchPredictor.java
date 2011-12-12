package com.fitzgerald.simulator.processor;

import java.util.HashMap;

public class BranchPredictor {
    
    protected HashMap<Integer, Boolean> previousBranches = new HashMap<Integer, Boolean>(); 
    
    /**
     * Predict whether a branch is taken
     * @param address Address of branch
     * @return True if taken
     */
    public boolean predictBranch(int address) {
        return getPreviousDirection(address);
    }
    
    /**
     * Return the previous direction of this branch
     * @param address Address of branch
     * @return True if taken
     */
    protected boolean getPreviousDirection(int address) {
        Boolean takeBranch;
        
        if ((takeBranch = previousBranches.get(address)) == null) {
            takeBranch = true;
        }
        
        return takeBranch;
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
