package com.fitzgerald.simulator.instruction;

public abstract class Instruction {
    
    protected byte[] operand1;
    protected byte[] operand2;
    protected byte[] operand3;
    
    protected abstract boolean conditional();
    
    public void executeConditionally() {
        if (this.conditional()) {
            execute();
        }
    }
    
    protected abstract void execute();
    
    public void setOperand(int operandNo, byte[] value) {
        switch (operandNo) {
        case 1:
            operand1 = value;
            break;
        case 2:
            operand2 = value;
            break;
        case 3:
            operand3 = value;
        }
    }
}
