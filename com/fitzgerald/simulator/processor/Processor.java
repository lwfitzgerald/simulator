package com.fitzgerald.simulator.processor;

public class Processor {
    
    protected Memory memory;
    protected RegisterFile registerFile;
    protected Program program;
    
    public Processor(Program program, Memory memory) {
        this.program = program;
        this.registerFile = new RegisterFile();
        this.memory = memory;
    }
}
