package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.Instruction;

public class PipelineLatch {
    
    protected Instruction currentInstruction;
    protected Instruction nextInstruction;
    
    protected byte[] currentSourceData1;
    protected byte[] nextSourceData1;
    
    protected byte[] currentSourceData2;
    protected byte[] nextSourceData2;
    
    protected byte[] currentResultData;
    protected byte[] nextResultData;
    
    public Instruction getCurrentInstruction() {
        return currentInstruction;
    }
    
    public void setCurrentInstruction(Instruction instruction) {
        currentInstruction = instruction;
    }
    
    public Instruction getNextInstruction() {
        return nextInstruction;
    }
    
    public void setNextInstruction(Instruction instruction) {
        nextInstruction = instruction;
    }
    
    public byte[] getCurrentSourceData1() {
        return currentSourceData1;
    }
    
    public void setCurrentSourceData1(byte[] data) {
        currentSourceData1 = data;
    }
    
    public byte[] getNextSourceData1() {
        return nextSourceData1;
    }
    
    public void setNextSourceData1(byte[] data) {
        nextSourceData1 = data;
    }
    
    public byte[] getCurrentSourceData2() {
        return currentSourceData2;
    }
    
    public void setCurrentSourceData2(byte[] data) {
        currentSourceData2 = data;
    }
    
    public byte[] getNextSourceData2() {
        return nextSourceData2;
    }
    
    public void setNextSourceData2(byte[] data) {
        nextSourceData2 = data;
    }
    
    public byte[] getCurrentResultData() {
        return currentResultData;
    }
    
    public void setCurrentResultData(byte[] data) {
        currentResultData = data;
    }
    
    public byte[] getNextResultData() {
        return nextResultData;
    }
    
    public void setNextResultData(byte[] data) {
        nextResultData = data;
    }
    
    public void finishCycle() {
        currentInstruction = nextInstruction;
        nextInstruction = null;
        
        currentSourceData1 = nextSourceData1;
        nextSourceData1 = null;
        
        currentSourceData2 = nextSourceData2;
        nextSourceData2 = null;
        
        currentResultData = nextResultData;
        nextResultData = null;
    }
}
