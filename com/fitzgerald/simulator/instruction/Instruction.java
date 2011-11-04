package com.fitzgerald.simulator.instruction;

import java.io.Serializable;

import com.fitzgerald.simulator.processor.PipelineLatch;
import com.fitzgerald.simulator.processor.RegisterFile;

public abstract class Instruction implements Serializable {
    
    /**
     * Serialising ID
     */
    private static final long serialVersionUID = 570193615900542137L;
    
    protected byte[] operand1;
    protected byte[] operand2;
    protected byte[] operand3;
    
    protected abstract boolean conditional();
    
    public void executeConditionally() {
        if (this.conditional()) {
            execute();
        }
    }
    
    public abstract void decode(RegisterFile registerFile, PipelineLatch decodeLatch);
    
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
