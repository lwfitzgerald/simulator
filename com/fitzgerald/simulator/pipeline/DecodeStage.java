package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class DecodeStage extends PipelineStage {

    public DecodeStage(Instruction instruction) {
        super(instruction);
    }

    @Override
    public boolean step(Program program, RegisterFile registerFile, MemoryController memoryController) {
        // Only perform the step if it has not been done already
        if (isCompleted == false) {
            // Call the instruction's individual decode method
            instruction.decode(registerFile, this);
            
            // Decode only ever takes 1 cycle so mark as completed
            setCompleted(true);
        }
        
        // Check if this is an artificial Nop
        if (instruction.isNop()) {
            if (((Nop) instruction).isEndOfProgramNop()) {
                return false;
            }
        }
        
        return true;
    }
}
