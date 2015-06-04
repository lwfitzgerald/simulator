package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Instruction;

public class PipelineBuffer {
    public Instruction instruction = null;
    public boolean speculative = false;
    public Integer branchAddr = null;
}
