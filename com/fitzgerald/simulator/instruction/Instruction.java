package com.fitzgerald.simulator.instruction;

import java.io.Serializable;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
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
     * Returns the number of cycles required
     * for this instruction to complete
     * @return Number of cycles required to complete
     */
    public abstract int getALUCyclesRequired();
    
    /**
     * Perform the individual decode operations for this
     * instruction
     * @param registerFile Register file reference
     * @param decodeStage Decode stage reference
     */
    public abstract void decode(RegisterFile registerFile, DecodeStage decodeStage);
    
    /**
     * Perform the individual execute operations for this
     * instruction.
     * @param processor Processor object reference
     * @param registerFile Register file reference
     * @param branchUnit TODO
     * @param memoryController Memory controller object reference
     * @param executeStage Execute stage reference
     * @return True if execution completed, false if
     * more cycles required
     */
    public abstract boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage);
    
    /**
     * Called by the ALU
     * Describes the operation performed by the ALU
     * for this instruction
     * @param executeStage Execute stage reference
     * @return Result of operation
     */
    public abstract byte[] aluOperation(ExecuteStage executeStage);
    
    /**
     * Called by the branch unit
     * Checks whether the branch should be taken
     * @param executeStage Execute stage reference
     * @return True if branch should be taken, false
     * otherwise
     */
    public abstract boolean branchCondition(ExecuteStage executeStage);
    
    /**
     * Called by the branch unit
     * Returns the calculated address to branch to
     * @param executeStage Execute stage reference
     * @return Address to branch to
     */
    public abstract byte[] branchCalculation(ExecuteStage executeStage);
    
    /**
     * Takes an address of a label and converts it
     * to an immediate value according to the behaviour
     * of the instruction (absolute or relative)
     * 
     * Used during assembly (for J / B)
     * @param labelAddr Address of label
     * @param instructionAddr Instruction address 
     * @return Immediate value to use
     */
    public abstract int labelToAddress(int labelAddr, int instructionAddr);
    
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
    
    /**
     * Returns if this instruction is a Nop
     * @return True if this instruction is a Nop
     */
    public boolean isNop() {
        return this instanceof Nop;
    }
    
    /**
     * Returns a string representation of the instruction
     */
    public abstract String toString();
}
