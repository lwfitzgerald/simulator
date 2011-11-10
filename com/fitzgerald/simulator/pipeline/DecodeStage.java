package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.ui.UI;

public class DecodeStage extends PipelineStage {

    public DecodeStage(UI ui) {
        super(ui);
        
        /*
         * Set the stage number
         */
        STAGE_NUM = 2;
    }

    @Override
    public void step(Program program, Processor processor, RegisterFile registerFile,
            ALU alu, MemoryController memoryController) {
        
        // Update the UI
        updateUI();
        
        // Only perform the step if it has not been done already
        if (isCompleted == false) {
            // Call the instruction's individual decode method
            instruction.decode(registerFile, this);
            
            // Decode only ever takes 1 cycle so mark as completed
            setCompleted(true);
        }
    }
}
