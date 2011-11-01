package com.fitzgerald.simulator.instruction;

public interface Instruction {
    public abstract boolean conditional();
    
    public abstract void executeConditionally();
    
    public abstract void execute();
}
