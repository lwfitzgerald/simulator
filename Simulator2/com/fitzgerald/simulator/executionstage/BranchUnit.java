package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction;

public class BranchUnit extends ExecutionUnit {
    
    @Override
    protected void performOperation() {
        LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
        
        if (instruction.branchCondition(srcData1, srcData2)) {
            return instruction.branchCalculation(srcData1, srcData2);
        }
        
        // Calculate result
        int result = lsInstruction.aluOperation(srcData1, srcData2);
        
        // Update ROB
        finishedExecuting(result);
    }
    
    /**
     * Process a branch/jump instruction
     */
    public void performOperation() {
        if (instruction.branchCondition(srcData1, srcData2)) {
            return instruction.branchCalculation(srcData1, srcData2);
        }
        
        // Branch not taken
        return null;
    }
    
}
