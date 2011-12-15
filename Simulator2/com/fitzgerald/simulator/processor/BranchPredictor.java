package com.fitzgerald.simulator.processor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class BranchPredictor {
    
    protected HashMap<Integer, Queue<Boolean>> previousBranches = new HashMap<Integer, Queue<Boolean>>();
    
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
            Queue<Boolean> previousDirections = previousBranches.get(address);
            
            if (previousDirections != null) {
                int numTaken = 0;
                int numNotTaken = 0;
                
                for (boolean taken : previousDirections) {
                    if (taken) {
                        numTaken++;
                    } else {
                        numNotTaken++;
                    }
                }
                
                if (numTaken > numNotTaken) {
                    takeBranch = true;
                } else if (numTaken < numNotTaken) {
                    takeBranch = false;
                } else {
                    takeBranch = predictUnknown(address, targetAddress);
                }
            } else {
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
        if (previousBranches.containsKey(address)) {
            Queue<Boolean> previous = previousBranches.get(address);
            
            if (previous.size() == 3) {
                previous.poll();
            }
            
            previous.add(taken);
        } else {
            Queue<Boolean> previous = new LinkedList<Boolean>();
            
            previous.add(taken);
            
            previousBranches.put(address, previous);
        }
    }
    
}
