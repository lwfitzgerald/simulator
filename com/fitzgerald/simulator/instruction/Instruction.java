package com.fitzgerald.simulator.instruction;

public abstract class Instruction {
    protected abstract boolean conditional();
    
    public void executeConditionally() {
        if (this.conditional()) {
            execute();
        }
    }
    
    protected abstract void execute();
}
