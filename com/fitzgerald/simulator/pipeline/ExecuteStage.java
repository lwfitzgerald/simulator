package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.Program;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.ui.UI;

public class ExecuteStage extends PipelineStage {
    
    public ExecuteStage(UI ui) {
        super(ui);
        
        /*
         * Set the stage number
         */
        STAGE_NUM = 3;
    }

    @Override
    public void step(Program program, Processor processor, RegisterFile registerFile,
            ALU alu, MemoryController memoryController) {
        
        // Update the UI
        updateUI();
        
        /*
         * Only perform the step if it's not been completed on
         * a previous cycle
         */
        if (!isCompleted) {
            // Call the instruction's individual execute method
            boolean executeResult = instruction.execute(processor, registerFile, alu, memoryController, this);
            
            if (executeResult == true) {
                // No more cycles required, mark as completed
                setCompleted(true);
            }
        }
    }
    
}
