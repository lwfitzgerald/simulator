package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;

public class Processor {
    
    protected Memory memory;
    protected RegisterFile registerFile;
    protected Program program;
    
    public static final int PC_REG = 15;
    
    /*
     * Pipeline buffers/latches
     */
    
    /**
     * Buffer following fetch stage 
     */
    protected PipelineLatch fetchLatch;
    
    protected PipelineLatch decodeLatch;
    
    protected PipelineLatch executeLatch;
    
    public Processor(Program program, Memory memory) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memory = memory;
        
        // Set program counter to 0
        registerFile.getRegister(PC_REG).setCurrentValue(Util.intToBytes(0));
    }
    
    /**
     * Performs one clock cycle
     * @return Boolean representing whether the end
     * of the program has been reached
     */
    public boolean step() {
        fetch();
        decode();
        execute();
        writeback();
        
        finishCycle();
        return true;
    }
    
    /**
     * Perform the fetch pipeline stage
     * @return True if there was an instruction to fetch
     */
    protected boolean fetch() {
        // Get the current program counter value
        int programCounter = Util.bytesToInt(registerFile.getRegister(PC_REG).getCurrentValue());
        
        // Load the instruction from that address
        Instruction instruction = program.getInstruction(programCounter);
        
        if (instruction == null) {
            // No more instructions to execute
            return false;
        }
        
        // Set the next instruction of the fetch latch
        fetchLatch.setNextInstruction(instruction);
        
        /*
         * Set the next value of the program counter.
         * Copied to current at end of this cycle
         */
        registerFile.getRegister(PC_REG).setNextValue(Util.intToBytes(programCounter + 4));
        return true;
    }
    
    /**
     * Perform the decode pipeline stage
     * @return True if there was an instruction to decode
     */
    protected boolean decode() {
        Instruction instruction = fetchLatch.getCurrentInstruction();
        
        if (instruction == null) {
            // No instruction from fetch stage
            return false;
        }
        
        // Call the instruction's individual decode method
        instruction.decode(registerFile, decodeLatch);
        
        return true;
    }
    
    /**
     * Perform the execute pipeline stage
     * @return True if there was an instruction to execute
     */
    protected boolean execute() {
        Instruction instruction = decodeLatch.getCurrentInstruction();
        
        if (instruction == null) {
            // No instruction from decode stage
            return false;
        }
        
        // Call the instruction's individual execute method
        instruction.execute(registerFile, decodeLatch, executeLatch);
        
        return true;
    }
    
    /**
     * Perform the writeback pipeline stage
     * @return True if there was an instruction result to
     * write back
     */
    protected boolean writeback() {
        /*
         * Should this actually be a separate pipeline stage or
         * does this occur:
         * 
         * 1. At the end of execute stage
         * 2. During next fetch (although I don't know how that
         * would actually work)
         */
        
        return true;
    }
    
    protected void finishCycle() {
        fetchLatch.finishCycle();
        decodeLatch.finishCycle();
        executeLatch.finishCycle();
        
        registerFile.finishCycle();
    }
}
