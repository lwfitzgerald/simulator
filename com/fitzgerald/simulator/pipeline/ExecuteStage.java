package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class ExecuteStage extends PipelineStage {
    
    public ExecuteStage(Instruction instruction) {
        super(instruction);
    }

    @Override
    public boolean step(Program program, RegisterFile registerFile, MemoryController memoryController) {
        /*
         * Only perform the step if it's not been completed on
         * a previous cycle
         */
        if (!isCompleted) {
            // Call the instruction's individual execute method
            boolean executeResult = instruction.execute(registerFile, memoryController, this);
            
            if (executeResult == true) {
                // No more cycles required, mark as completed
                setCompleted(true);
            }
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
