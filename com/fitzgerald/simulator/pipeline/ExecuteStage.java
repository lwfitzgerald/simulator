package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.processor.MemoryController;
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
        
        updateUI();
    }

    @Override
    public void step(Program program, RegisterFile registerFile, MemoryController memoryController) {
        /*
         * Only perform the step if it's not been completed on
         * a previous cycle
         */
        if (!isCompleted) {
            // Call the instruction's individual execute method
            boolean executeResult = instruction.execute(registerFile, memoryController, this);
            
            // Update the UI
            updateUI();
            
            if (executeResult == true) {
                // No more cycles required, mark as completed
                setCompleted(true);
            }
        }
    }
    
}
