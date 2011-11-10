package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.RegisterFile;

public class Nop extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -5850254084069555927L;

    protected boolean endOfProgramNop;
    
    /**
     * Default constructor for a Nop
     * 
     * This is called by reflection when assembling
     * a program so endOfProgramNop should be set to
     * false
     */
    public Nop() {
        this.endOfProgramNop = false;
    }
    
    /**
     * Called when creating a fake Nop used after there
     * are no more instructions in the program to fetch
     * @param endOfProgramNop
     */
    public Nop(boolean endOfProgramNop) {
        this.endOfProgramNop = endOfProgramNop;
    }
    
    @Override
    public int getALUCyclesRequired() {
        // Not applicable
        return -1;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {}

    @Override
    public boolean execute(RegisterFile registerFile, ALU alu,
            MemoryController memoryController, ExecuteStage executeStage) {
        
        return true;
    }
    
    @Override
    public byte[] aluOperation(ExecuteStage executeStage) {
        // Not applicable
        return null;
    }
    
    @Override
    public int labelToAddress(int labelAddr, int instructionAddr) {
        // Not applicable
        return -1;
    }

    /**
     * Returns if this is a Nop inserted
     * when there are no more instructions in the
     * program to execute
     * @return True if Nop is artificial
     */
    public boolean isEndOfProgramNop() {
        return endOfProgramNop;
    }

    @Override
    public String toString() {
        return "NOP";
    }
    
}
