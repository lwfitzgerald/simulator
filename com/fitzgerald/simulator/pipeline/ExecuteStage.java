package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;

public class ExecuteStage extends PipelineStage {

    public ExecuteStage(Instruction instruction) {
        super(instruction);
    }

    @Override
    public boolean step(Program program, RegisterFile registerFile) {
        // Call the instruction's individual execute method
        instruction.execute(registerFile, this);

        // TODO: Return false here if it's a faux NOP
        return true;
    }
}
