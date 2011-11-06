package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public abstract class PipelineStage {
    protected Instruction instruction;
    
    protected byte[] sourceData1;
    protected byte[] sourceData2;
    
    // Not sure this is needed yet
    protected int programCounter;
    
    public PipelineStage() {}
    
    /**
     * Initialise this pipeline stage with an instruction
     * @param instruction Instruction to initialise with
     */
    public PipelineStage(Instruction instruction) {
        this.instruction = instruction;
    }
    
    /**
     * Step this pipeline stage
     * @return
     */
    public abstract boolean step(Program program, RegisterFile registerFile);
    
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
}
