package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.pipeline.*;
import com.fitzgerald.simulator.ui.UI;

public class Processor {
    
    protected ALU alu;
    protected MemoryController memoryController;
    protected RegisterFile registerFile;
    protected Program program;
    protected UI ui;
    
    protected int cycleCount = -1;
    
    public static final int PC_REG = 15;
    
    /*
     * Pipeline stages
     */
    protected FetchStage fetchStage;
    protected DecodeStage decodeStage;
    protected ExecuteStage executeStage;
    
    public Processor(Program program, Memory memory, UI ui) {
        this.program = program;
        this.registerFile = new RegisterFile(ui);
        this.alu = new ALU();
        this.memoryController = new MemoryController(memory);
        this.ui = ui;
        
        // Initialise pipeline stages
        this.fetchStage = new FetchStage(ui);
        this.decodeStage = new DecodeStage(ui);
        this.executeStage = new ExecuteStage(ui);
        
        // Set program counter to 0
        registerFile.getRegister(PC_REG).setCurrentValue(Util.intToBytes(0));
    }
    
    /**
     * Performs one simulation step
     * @return Boolean representing whether the end
     * of the program has been reached
     */
    public boolean step() {
        // Update cycle count and register UIs
        ui.setCycleCount(++cycleCount);
        registerFile.updateUI();

        fetchStage.step(program, this, registerFile, alu, memoryController);
        
        if (fetchStage.containsArtificialNop() &&
            decodeStage.containsArtificialNop() &&
            executeStage.containsArtificialNop()) {
            
            /*
             * All stages contain artificial Nops so we can halt
             * but first we need to update the UI one last time to
             * clear the last instruction
             */
            decodeStage.updateUI();
            executeStage.updateUI();
            
            return false;
        }
        
        decodeStage.step(program, this, registerFile, alu, memoryController);
        executeStage.step(program, this, registerFile, alu, memoryController);
        
        if (executeStage.isCompleted()) {
            /*
             * Execute stage has completed (require no more cycles)
             * so copy register nexts to currents etc
             */
            finishStep();
        }
        
        return true;
    }
    
    protected void finishStep() {
        if (!pcIncrementedNormally()) {
            flushPipeline();
        }
        
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
    
    /**
     * Returns whether the PC has been incremented
     * normally
     * @return True if next PC = curr PC + 4, false
     * otherwise
     */
    protected boolean pcIncrementedNormally() {
        int curr = Util.bytesToInt(registerFile.getRegister(PC_REG).getCurrentValue());
        int next = Util.bytesToInt(registerFile.getRegister(PC_REG).getNextValue());
        
        return next - curr == 4;
    }
    
    /**
     * Flushes the pipeline following a branch
     */
    protected void flushPipeline() {
        // Insert artificial Nops
        fetchStage.setInstruction(new Nop(true));
        decodeStage.setInstruction(new Nop(true));
    }
    
}
