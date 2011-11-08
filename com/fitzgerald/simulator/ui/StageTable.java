package com.fitzgerald.simulator.ui;

import java.awt.GridLayout;
import javax.swing.JPanel;

import com.fitzgerald.simulator.instruction.Instruction;

public class StageTable extends JPanel {
    
    protected StageStatus[] stages;
    
    public StageTable() {
        super();
        
        stages = new StageStatus[3];
        
        stages[0] = new StageStatus("Fetch");
        stages[1] = new StageStatus("Decode");
        stages[2] = new StageStatus("Execute");
        
        GridLayout layoutMgr = new GridLayout(3, 1);
        setLayout(layoutMgr);
        
        for (StageStatus stage : stages) {
            add(stage);
        }
        
        setVisible(true);
    }
    
    /**
     * Sets the instruction shown in the UI for this stage
     * @param stageNum Stage number
     * @param instruction Instruction to set
     */
    public void setStageInstruction(int stageNum, Instruction instruction) {
        stages[stageNum-1].setInstruction(instruction);
    }
    
}
