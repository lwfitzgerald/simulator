package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class FetchStage {
    
    protected Instruction instruction1 = null;
    protected Integer instruction1BranchAddr = null;
    protected Instruction instruction2 = null;
    protected Integer instruction2BranchAddr = null;
    
    public void step(Program program, Processor processor, RegisterFile registerFile) {
        if (instruction1 == null) {
            // Fetch instruction 1
            fetchInstruction1(program, registerFile);
            
            if (instruction1 == null) {
                // No instruction1 means no instruction2!
                return;
            }
        }
        
        if (instruction2 == null) {
            // Fetch instruction 2
            fetchInstruction2(program, registerFile);
        }
    }
    
    public void finishStep(DecodeStage decodeStage) {
        // If decode slots are available, copy to them!
        if (instruction1 != null) {
            // Attempt to pass instruction1 to decode
            if (decodeStage.instruction1Free()) {
                decodeStage.setInstruction1(instruction1, instruction1BranchAddr);
                instruction1 = null;
                instruction1BranchAddr = null;
            } else if (decodeStage.instruction2Free()) {
                decodeStage.setInstruction2(instruction1, instruction1BranchAddr);
                instruction2 = null;
                instruction2BranchAddr = null;
            }
        }
        
        if (instruction2 != null) {
            // Attempt to pass instruction2 to decode
            if (decodeStage.instruction1Free()) {
                decodeStage.setInstruction1(instruction2, instruction2BranchAddr);
                instruction1 = null;
                instruction1BranchAddr = null;
            } else if (decodeStage.instruction2Free()) {
                decodeStage.setInstruction2(instruction2, instruction2BranchAddr);
                instruction2 = null;
                instruction2BranchAddr = null;
            }
            
            // Instruction 1 decoding so move instruction 2 -> 1
            if (instruction1 == null) {
                instruction1 = instruction2;
                instruction1BranchAddr = instruction2BranchAddr;
                instruction2 = null;
                instruction2BranchAddr = null;
            }
        }
    }
    
    /**
     * Fetch first instruction
     * @param program Program reference
     * @param registerFile Register file reference
     * @return Fetched instruction
     */
    protected void fetchInstruction1(Program program, RegisterFile registerFile) {
        int currentPC = registerFile.getRegister(Processor.PC_REG).getCurrentValue();
        
        fetchInstruction(program, registerFile, currentPC, 1);
    }
    
    /**
     * Fetch second instruction
     * @param program Program reference
     * @param registerFile Register file reference
     * @return Fetched instruction
     */
    protected void fetchInstruction2(Program program, RegisterFile registerFile) {
        // Use next value as it's been set by fetch of first instruction
        int currentPC = registerFile.getRegister(Processor.PC_REG).getNextValue();
        
        fetchInstruction(program, registerFile, currentPC, 2);
    }
    
    /**
     * Fetch the next instruction using a given current PC
     * @param program Program reference
     * @param registerFile Register file reference
     * @param currentPC Current program counter
     */
    protected void fetchInstruction(Program program, RegisterFile registerFile, int currentPC, int instructionNo) {
        int newPC = currentPC;
        
        Instruction instruction = program.getInstruction(currentPC);
        
        if (instruction == null) {
            // No more instructions!
            return;
        }
        
        // Update fetch address
        if (instruction.getType() == InstructionType.BRANCH) {
            BranchInstruction branchInstruction = (BranchInstruction) instruction;
            
            // Store actual branch address
            if (instructionNo == 1) {
                instruction1BranchAddr = branchInstruction.getBranchAddress(currentPC);
            } else if (instructionNo == 2) {
                instruction2BranchAddr = branchInstruction.getBranchAddress(currentPC);
            }
            
            if (branchInstruction.isUnconditional()) {
                newPC = branchInstruction.getBranchAddress(currentPC);
            } else {
                // TODO: Handle conditional branch
            }
        } else {
            // Not a branch so increment counter
            newPC += 4;
        }
        
        registerFile.getRegister(Processor.PC_REG).setNextValue(newPC);
        
        if (instructionNo == 1) {
            instruction1 = instruction;
        } else if (instructionNo == 2) {
            instruction2 = instruction;
        }
    }
    
}
