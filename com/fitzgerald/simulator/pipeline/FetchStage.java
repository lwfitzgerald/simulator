package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class FetchStage extends PipelineStage {

    @Override
    public boolean step(Program program, RegisterFile registerFile) {
        // Get the current program counter value
        int programCounter = Util.bytesToInt(registerFile.getRegister(Processor.PC_REG).getCurrentValue());
        
        // Load the instruction from that address
        instruction = program.getInstruction(programCounter);
        
        if (instruction == null) {
            // No more instructions to execute, insert a NOP
            instruction = new Nop(true);
        }
        
        /*
         * Set the next value of the program counter.
         * Copied to current at end of this cycle
         */
        registerFile.getRegister(Processor.PC_REG).setNextValue(Util.intToBytes(programCounter + 4));
        
        // TODO: return false here if it's a faux NOP
        return true;
    }
}
