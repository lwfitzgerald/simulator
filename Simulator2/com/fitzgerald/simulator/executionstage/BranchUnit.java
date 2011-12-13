package com.fitzgerald.simulator.executionstage;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.processor.BranchPredictor;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReorderBuffer;

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
        
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        BranchPredictor branchPredictor = processor.getBranchPredictor();
        
        // Get the fail address
        int failAddr = processor.getSpeculateFailAddress();
        int branchAddr = processor.getSpeculateBranchAddr();
        
        // Mark as no longer speculating
        processor.stopSpeculating();
        
        if (branchInstruction.branchCondition(srcData1, srcData2)) {
            // Set branch outcome in predictor
            branchPredictor.setTakenDirection(branchAddr, true);
            
            if (failAddr != dest) {
                // Approve all speculative ROB entries
                reorderBuffer.approveSpeculative();
            } else {
                recoverWrongDirection(reorderBuffer, failAddr);
                return;
            }
        } else {
            // Set branch outcome in predictor
            branchPredictor.setTakenDirection(branchAddr, false);
            
            if (failAddr == dest) {
                // Approve all speculative ROB entries
                reorderBuffer.approveSpeculative();
            } else {
                recoverWrongDirection(reorderBuffer, failAddr);
                return;
            }
        }
        
        // Update ROB
        finishedExecuting();
    }
    
    /**
     * Recover from going the wrong direction on
     * a branch
     * @param reorderBuffer Reorder buffer reference
     * @param failAddr Correct instruction address
     */
    protected void recoverWrongDirection(ReorderBuffer reorderBuffer, int failAddr) {
        // Remove speculative instructions
        reorderBuffer.removeSpeculative();
        
        // Update PC
        RegisterFile registerFile = processor.getRegisterFile();
        registerFile.getRegister(Processor.PC_REG).setValue(failAddr);
        
        // Flush pipeline
        processor.flushPipeline();
    }
    
}
