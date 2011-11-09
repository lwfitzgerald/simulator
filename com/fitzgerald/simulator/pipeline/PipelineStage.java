package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.ui.UI;

public abstract class PipelineStage {
    protected Instruction instruction;
    
    protected byte[] sourceData1 = null;
    protected byte[] sourceData2 = null;
    protected byte[] sourceData3 = null;
    protected byte[] buffer = null;
    
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
     * @param registerFile Reference to register file object
     * @param alu Reference to ALU object
     * @param memoryController Reference to memory controller object
     */
    public abstract void step(Program program, RegisterFile registerFile,
            ALU alu, MemoryController memoryController);
    
    /**
     * Copies the state of this pipeline stage to the
     * given (next) one
     * @param newStage Pipeline stage to copy state to
     */
    public void copyState(PipelineStage newStage) {
        newStage.setInstruction(instruction);
        newStage.setSourceData1(sourceData1);
        newStage.setSourceData2(sourceData2);
        //newStage.setSourceData3(sourceData3);
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
     * @return the sourceData3
     */
    public byte[] getSourceData3() {
        return sourceData3;
    }

    /**
     * @param sourceData3 the sourceData3 to set
     */
    public void setSourceData3(byte[] sourceData3) {
        this.sourceData3 = sourceData3;
    }

    /**
     * @return the buffer
     */
    public byte[] getBuffer() {
        return buffer;
    }

    /**
     * @param buffer the buffer to set
     */
    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
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
