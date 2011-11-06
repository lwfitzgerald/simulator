package com.fitzgerald.simulator.instruction;

import java.io.Serializable;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.RegisterFile;

public abstract class Instruction implements Serializable {
    
    /**
     * Serialising ID
     */
    private static final long serialVersionUID = 570193615900542137L;
    
    /**
     * Operand 1
     */
    protected byte[] operand1;
    
    /**
     * Operand 2
     */
    protected byte[] operand2;
    
    /**
     * Operand 3
     */
    protected byte[] operand3;
    
    /**
     * Called to decide whether to execute instruction
     * (for conditional branches etc)
     * @return True if instruction should be executed 
     */
    protected abstract boolean conditional();
    
    /**
     * Perform the individual decode operations for this
     * instruction
     * @param registerFile Register file reference
     * @param decodeStage Decode stage reference
     */
    public abstract void decode(RegisterFile registerFile, DecodeStage decodeStage);
    
    /**
     * Evaluate the conditional method for this instruction
     * and execute if it evaluates to true
     * @param registerFile Register file reference
     * @param executeStage Execute stage reference
     */
    public void execute(RegisterFile registerFile, ExecuteStage executeStage) {
        if (conditional()) {
            executeOperation(registerFile, executeStage);
        }
    }
    
    /**
     * Perform the individual execute operations for this
     * instruction.
     * 
     * This should never be called directly, only by the
     * execute method which checks the conditional for the
     * instruction
     * @param registerFile Register file reference
     * @param executeStage Execute stage reference
     */
    protected abstract void executeOperation(RegisterFile registerFile, ExecuteStage executeStage);
    
    /**
     * Set the value of an operand for this instruction
     * @param operandNo Operand to set
     * @param value Value to set operand to
     */
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
