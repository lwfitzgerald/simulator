package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
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
    protected boolean conditional() {
        // Never do anything!
        return false;
    }

    @Override
    public void decode(RegisterFile registerFile, DecodeStage decodeStage) {}

    @Override
    protected void executeOperation(RegisterFile registerFile, ExecuteStage executeStage) {}

}
