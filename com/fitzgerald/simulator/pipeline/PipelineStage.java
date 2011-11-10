package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.ui.UI;

public abstract class PipelineStage {
    protected Instruction instruction;
    
    protected byte[] sourceData1;
    protected byte[] sourceData2;
    
    /**
     * Stage number for this stage
     */
    protected int STAGE_NUM;
    
    /**
     * UI control object reference
     */
    protected UI ui;
    
    /**
     * Represents if the stage has completed handling
     * of the current instruction 
     */
    protected boolean isCompleted = false;
    
    public PipelineStage(UI ui) {
        this.ui = ui;
        
        this.instruction = new Nop(true);
    }
    
    /**
     * Step this pipeline stage
     * @param program Reference to program object
     * @param processor Reference to Processor object
     * @param registerFile Reference to register file object
     * @param alu Reference to ALU object
     * @param memoryController Reference to memory controller object
     */
    public abstract void step(Program program, Processor processor,
            RegisterFile registerFile, ALU alu, MemoryController memoryController);
    
    /**
     * Copies the state of this pipeline stage to the
     * given (next) one
     * @param newStage Pipeline stage to copy state to
     */
    public void copyState(PipelineStage newStage) {
        newStage.setInstruction(instruction);
        newStage.setSourceData1(sourceData1);
        newStage.setSourceData2(sourceData2);
    }
    
    /**
     * Updates the UI for this stage
     */
    public void updateUI() {
        ui.setStageInstruction(STAGE_NUM, instruction);
    }
    
    /**
     * Returns whether this stage contains an artificial
     * Nop
     * @return True if this stage contains an artificial
     * Nop
     */
    public boolean containsArtificialNop() {
        if (instruction.isNop()) {
            if (((Nop) instruction).isEndOfProgramNop()) {
                return true;
            }
            
            return false;
        }
        
        return false;
    }
    
    /**
     * @return the instruction
     */
    public Instruction getInstruction() {
        return instruction;
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(Instruction instruction) {
        this.instruction = instruction;
    }

    /**
     * @return the sourceData1
     */
    public byte[] getSourceData1() {
        return sourceData1;
    }
    
    /**
     * @param sourceData1 the sourceData1 to set
     */
    public void setSourceData1(byte[] sourceData1) {
        this.sourceData1 = sourceData1;
    }
    
    /**
     * @return the sourceData2
     */
    public byte[] getSourceData2() {
        return sourceData2;
    }
    
    /**
     * @param sourceData2 the sourceData2 to set
     */
    public void setSourceData2(byte[] sourceData2) {
        this.sourceData2 = sourceData2;
    }
    
    /**
     * @return True if execution has completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * @param Whether execution has completed
     */
    public void setCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
    
}
