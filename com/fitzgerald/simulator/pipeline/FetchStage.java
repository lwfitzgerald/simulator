package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.instruction.Nop;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;
import com.fitzgerald.simulator.ui.UI;

public class FetchStage extends PipelineStage {
    
    public FetchStage(UI ui) {
        super(ui);
        
        /*
         * Set the stage number
         */
        STAGE_NUM = 1;
    }
    
    @Override
    public void step(Program program, RegisterFile registerFile, MemoryController memoryController) {
        // Only perform the step if it has not been done already
        if (isCompleted == false) {
            // Get the current program counter value
            int programCounter = Util.bytesToInt(registerFile.getRegister(Processor.PC_REG).getCurrentValue());
            
            // Load the instruction from that address
            instruction = program.getInstruction(programCounter);
            
            if (instruction == null) {
                // No more instructions to execute, insert a NOP
                instruction = new Nop(true);
            }
            
            // Update the UI
            updateUI();
            
            /*
             * Set the next value of the program counter.
             * Copied to current at end of this cycle
             */
            registerFile.getRegister(Processor.PC_REG).setNextValue(Util.intToBytes(programCounter + 4));
            
            // Fetch only ever takes 1 cycle so mark as completed
            setCompleted(true);
        }
    }
}
