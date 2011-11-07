package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.pipeline.*;

public class Processor {
    
    protected MemoryController memoryController;
    protected RegisterFile registerFile;
    protected Program program;
    
    public static final int PC_REG = 15;
    
    /*
     * Pipeline stages
     */
    protected FetchStage fetchStage;
    protected DecodeStage decodeStage;
    protected ExecuteStage executeStage;
    
    public Processor(Program program, Memory memory) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memoryController = new MemoryController(memory);
        
        // Initialise pipeline stages
        this.fetchStage = new FetchStage();
        this.decodeStage = new DecodeStage(new Nop(true));
        this.executeStage = new ExecuteStage(new Nop(true));
        
        // Set program counter to 0
        registerFile.getRegister(PC_REG).setCurrentValue(Util.intToBytes(0));
    }
    
    /**
     * Performs one simulation step
     * @return Boolean representing whether the end
     * of the program has been reached
     */
    public boolean step() {
        boolean fetchResult = fetchStage.step(program, registerFile, memoryController);
        boolean decodeResult = decodeStage.step(program, registerFile, memoryController);
        boolean executeResult = executeStage.step(program, registerFile, memoryController);
        
        if (executeStage.isCompleted()) {
            /*
             * Execute stage has completed (require no more cycles)
             * so copy register nexts to currents etc
             */
            finishStep();
        }
        
        return fetchResult || decodeResult || executeResult;
    }
    
    protected void finishStep() {
        // In reverse order to maintain data correctness
        decodeStage.copyState(executeStage);
        fetchStage.copyState(decodeStage);
        
        // Set completed to false on all stages
        fetchStage.setCompleted(false);
        decodeStage.setCompleted(false);
        executeStage.setCompleted(false);
        
        // Copy register "next"'s to "current"'s
        registerFile.finishStep();
    }
}
