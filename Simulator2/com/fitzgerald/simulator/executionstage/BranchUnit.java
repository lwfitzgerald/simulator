package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.processor.BranchPredictor;
import com.fitzgerald.simulator.processor.Processor;

public class BranchUnit extends ExecutionUnit {
    
    /**
     * Create a new BranchUnit
     * @param processor Processor reference
     */
    public BranchUnit(Processor processor) {
        super(processor);
    }

    @Override
    protected void performOperation() {
        BranchInstruction branchInstruction = (BranchInstruction) instruction;
        
        if (branchInstruction.isUnconditional()) {
            // Unconditional so don't modify speculation state
            finishedExecuting();
        } else {
            BranchPredictor branchPredictor = processor.getBranchPredictor();
            
            // Get the fail address
            int failAddr = processor.getSpeculateFailAddress();
            int branchAddr = processor.getSpeculateBranchAddr();
            
            int result;
            
            if (branchInstruction.branchCondition(srcData1, srcData2)) {
                // Set branch outcome in predictor
                branchPredictor.setTakenDirection(branchAddr, true);
                
                if (failAddr != dest) {
                    result = 1;
                } else {
                    result = 0;
                }
            } else {
                // Set branch outcome in predictor
                branchPredictor.setTakenDirection(branchAddr, false);
                
                if (failAddr == dest) {
                    result = 1;
                } else {
                    result = 0;
                }
            }
            
            // Update ROB
            finishedExecuting(result);
        }
    }
    
}
