package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.processor.BranchPredictor;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class FetchStage extends PipelineStage {
    
    protected Instruction instruction1 = null;
    protected boolean instruction1Speculative;
    protected Integer instruction1BranchAddr = null;
    protected Instruction instruction2 = null;
    protected boolean instruction2Speculative;
    protected Integer instruction2BranchAddr = null;
    
    /**
     * Create new Fetch stage
     * @param processor Processor reference
     */
    public FetchStage(Processor processor) {
        super(processor);
    }
    
    public void step(Program program) {
        if (instruction1 == null) {
            // Fetch instruction 1
            fetchInstruction1(program);
            
            if (instruction1 == null) {
                // No instruction1 means no instruction2!
                return;
            }
        }
        
        if (instruction2 == null) {
            // Fetch instruction 2
            fetchInstruction2(program);
        }
    }
    
    /**
     * Fetch first instruction
     * @param program Program reference
     */
    protected void fetchInstruction1(Program program) {
        RegisterFile registerFile = processor.getRegisterFile();
        
        int currentPC = registerFile.getRegister(Processor.PC_REG).getValue();

        fetchInstruction(program, currentPC, 1);
    }

    /**
     * Fetch second instruction
     * @param program Program reference
     */
    protected void fetchInstruction2(Program program) {
        RegisterFile registerFile = processor.getRegisterFile();
        
        // Use next value as it's been set by fetch of first instruction
        int currentPC = registerFile.getRegister(Processor.PC_REG).getValue();

        fetchInstruction(program, currentPC, 2);
    }
    
    /**
     * Fetch the next instruction using a given current PC
     * @param program Program reference
     * @param processor Processor reference
     * @param registerFile Register file reference
     * @param branchPredictor Branch predictor reference
     * @param currentPC Current program counter
     * @param instructionNo Instruction no, 1 or 2
     */
    protected void fetchInstruction(Program program, int currentPC, int instructionNo) {
        RegisterFile registerFile = processor.getRegisterFile();
        BranchPredictor branchPredictor = processor.getBranchPredictor();
        
        int newPC = currentPC;
        
        Instruction instruction = program.getInstruction(currentPC);
        
        if (instruction == null) {
            // No more instructions!
            return;
        }
        
        // Update fetch address
        if (instruction.getType() == InstructionType.BRANCH) {
            BranchInstruction branchInstruction = (BranchInstruction) instruction;

            int branchAddress = branchInstruction.getBranchAddress(currentPC);
            
            if (branchInstruction.isUnconditional()) {
                newPC = branchAddress;
            } else {
                if (processor.getSpeculating()) {
                    /*
                     * Already speculating, cannot handle branch
                     * so throw away instruction
                     */
                    return;
                }
                
                if (branchPredictor.predictBranch(currentPC)) {
                    newPC = branchAddress;
                    // Set fail address to next instruction
                    processor.startSpeculating(currentPC + 4, currentPC);
                } else {
                    newPC += 4;
                    // Set fail address to branch address
                    processor.startSpeculating(branchAddress, currentPC);
                }
            }
            
            // Store actual branch address
            if (instructionNo == 1) {
                instruction1BranchAddr = branchAddress;
            } else if (instructionNo == 2) {
                instruction2BranchAddr = branchAddress;
            }
        } else {
            // Not a branch so increment counter
            newPC += 4;
        }
        
        registerFile.getRegister(Processor.PC_REG).setValue(newPC);
        
        if (instructionNo == 1) {
            instruction1 = instruction;
            
            if (instruction.getType() == InstructionType.BRANCH) {
                if (!((BranchInstruction) instruction).isUnconditional()) {
                    // Conditional branches are never speculative
                    instruction1Speculative = false;
                } else {
                    // Mark as speculative if necessary
                    instruction1Speculative = processor.getSpeculating();
                }
            } else {
                // Mark as speculative if necessary
                instruction1Speculative = processor.getSpeculating();
            }
        } else if (instructionNo == 2) {
            instruction2 = instruction;
            
            if (instruction.getType() == InstructionType.BRANCH) {
                if (!((BranchInstruction) instruction).isUnconditional()) {
                    // Conditional branches are never speculative
                    instruction2Speculative = false;
                } else {
                    // Mark as speculative if necessary
                    instruction2Speculative = processor.getSpeculating();
                }
            } else {
                // Mark as speculative if necessary
                instruction2Speculative = processor.getSpeculating();
            }
        }
    }
    
    /**
     * Copy instructions to the decode stage
     * if necessary
     * @param decodeStage Decode stage reference
     */
    public void finishStep(DecodeStage decodeStage) {
        // If decode slots are available, copy to them!
        
        if (instruction1 != null) {
            // Attempt to pass instruction1 to decode
            if (decodeStage.instruction1Free()) {
                decodeStage.setInstruction1(instruction1, instruction1Speculative, instruction1BranchAddr);
                instruction1 = null;
                instruction1BranchAddr = null;
            } else if (decodeStage.instruction2Free()) {
                decodeStage.setInstruction2(instruction1, instruction1Speculative, instruction1BranchAddr);
                instruction2 = null;
                instruction2BranchAddr = null;
            }
        }
        
        if (instruction2 != null) {
            // Attempt to pass instruction2 to decode
            if (decodeStage.instruction1Free()) {
                decodeStage.setInstruction1(instruction2, instruction2Speculative, instruction2BranchAddr);
                instruction1 = null;
                instruction1BranchAddr = null;
            } else if (decodeStage.instruction2Free()) {
                decodeStage.setInstruction2(instruction2, instruction2Speculative, instruction2BranchAddr);
                instruction2 = null;
                instruction2BranchAddr = null;
            }
            
            // Instruction 1 decoding so move instruction 2 -> 1
            if (instruction1 == null && instruction2 != null) {
                instruction1 = instruction2;
                instruction1BranchAddr = instruction2BranchAddr;
                instruction2 = null;
                instruction2BranchAddr = null;
            }
        }
    }
    
    /**
     * Returns whether this pipeline stage is empty
     * @return True if this stage is empty
     */
    public boolean isEmpty() {
        return instruction1 == null && instruction2 == null;
    }
    
    /**
     * Mark speculative instructions
     * as no longer speculative
     */
    public void approveSpeculative() {
        instruction1Speculative = false;
        instruction2Speculative = false;
    }
    
    /**
     * Remove speculative instructions
     */
    public void flushSpeculative() {
        if (instruction1Speculative) {
            instruction1 = null;
            instruction1BranchAddr = null;
        }
        
        if (instruction2Speculative) {
            instruction2 = null;
            instruction2BranchAddr = null;
        }
        
        if (instruction1 == null && instruction2 != null) {
            // Copy 2 -> 1
            instruction1 = instruction2;
            instruction1BranchAddr = instruction2BranchAddr;
            instruction2 = null;
            instruction2BranchAddr = null;
        }
    }

    @Override
    public void flush() {
        instruction1 = null;
        instruction1BranchAddr = null;
        instruction2 = null;
        instruction2BranchAddr = null;
    }
    
}
