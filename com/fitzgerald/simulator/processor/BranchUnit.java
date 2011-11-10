package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.pipeline.ExecuteStage;

public class BranchUnit {
    
    /**
     * Process a branch/jump instruction
     * @param executeStage Execute stage reference
     * @return Address to write to program counter
     * or null if branch is not taken
     */
    public byte[] performBranch(ExecuteStage executeStage) {
        Instruction instruction = executeStage.getInstruction();
        
        if (instruction.branchCondition(executeStage)) {
            return instruction.branchCalculation(executeStage);
        }
        
        // Branch not taken
        return null;
    }
    
}
