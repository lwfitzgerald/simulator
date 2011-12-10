package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.BranchInstruction;

public class BranchUnit {
    
    /**
     * Process a branch/jump instruction
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @return Address to write to program counter
     * or null if branch is not taken
     */
    public Integer performBranch(BranchInstruction instruction,
            Integer srcData1, Integer srcData2) {
        
        if (instruction.branchCondition(srcData1, srcData2)) {
            return instruction.branchCalculation(srcData1, srcData2);
        }
        
        // Branch not taken
        return null;
    }
    
}
