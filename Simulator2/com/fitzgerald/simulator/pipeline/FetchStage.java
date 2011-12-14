package com.fitzgerald.simulator.pipeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.processor.BranchPredictor;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class FetchStage extends PipelineStage {
    
    protected Queue<PipelineBuffer> buffers = new LinkedList<PipelineBuffer>();
    
    /**
     * Create new Fetch stage
     * @param processor Processor reference
     */
    public FetchStage(Processor processor) {
        super(processor);
    }
    
    public void step(Program program) {
        for (int i=0; i < Processor.FETCH_DECODE_WIDTH; i++) {
            if (buffers.size() < Processor.FETCH_DECODE_WIDTH) {
                RegisterFile registerFile = processor.getRegisterFile();
                
                int currentPC = registerFile.getRegister(Processor.PC_REG).getValue();
                
                fetchInstruction(program, currentPC, i);
            }
        }
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
        PipelineBuffer buffer = new PipelineBuffer();
        
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
                
                if (branchPredictor.predictBranch(currentPC, branchAddress)) {
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
            buffer.branchAddr = branchAddress;
        } else {
            // Not a branch so increment counter
            newPC += 4;
        }
        
        registerFile.getRegister(Processor.PC_REG).setValue(newPC);
        
        buffer.instruction = instruction;
        buffers.add(buffer);
        
        if (instruction.getType() == InstructionType.BRANCH) {
            if (!((BranchInstruction) instruction).isUnconditional()) {
                // Conditional branches are never speculative
                buffer.speculative = false;
            } else {
                // Mark as speculative if necessary
                buffer.speculative = processor.getSpeculating();
            }
        } else {
            // Mark as speculative if necessary
            buffer.speculative = processor.getSpeculating();
        }
    }
    
    /**
     * Copy instructions to the decode stage
     * if necessary
     * @param decodeStage Decode stage reference
     */
    public void finishStep(DecodeStage decodeStage) {
        // If decode slots are available, copy to them!
        Queue<PipelineBuffer> decodeBuffers = decodeStage.getBuffers();
        
        Iterator<PipelineBuffer> fetchItr = buffers.iterator();
        
        while (fetchItr.hasNext()) {
            if (decodeBuffers.size() < Processor.FETCH_DECODE_WIDTH) {
                // Space in decode buffer so move to it
                decodeBuffers.add(fetchItr.next());
                fetchItr.remove();
            } else {
                // Decode buffers filled
                break;
            }
        }
    }
    
    /**
     * Returns whether this pipeline stage is empty
     * @return True if this stage is empty
     */
    public boolean isEmpty() {
        return buffers.isEmpty();
    }
    
    /**
     * Mark speculative instructions
     * as no longer speculative
     */
    public void approveSpeculative() {
        for (PipelineBuffer buffer : buffers) {
            buffer.speculative = false;
        }
    }
    
    /**
     * Remove speculative instructions
     */
    public void flushSpeculative() {
        Iterator<PipelineBuffer> itr = buffers.iterator();
        
        while (itr.hasNext()) {
            if (itr.next().speculative) {
                itr.remove();
            }
        }
    }

    @Override
    public void flush() {
        buffers.clear();
    }
    
    public String toString() {
        if (buffers.isEmpty()) {
            return "EMPTY";
        }
        
        StringBuffer strBuffer = new StringBuffer();
        
        Iterator<PipelineBuffer> itr = buffers.iterator();
        
        for (int i=0; i < Processor.FETCH_DECODE_WIDTH; i++) {
            if (itr.hasNext()) {
                PipelineBuffer buffer = itr.next();
                strBuffer.append(i + ": [[" + buffer.instruction + "]," + (buffer.speculative ? "SP" : "NONSP") + "]\n");
            } else {
                strBuffer.append(i + ": [EMPTY]\n");
            }
        }
        
        return strBuffer.substring(0, strBuffer.length()-1);
    }
    
}
