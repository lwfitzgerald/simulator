package com.fitzgerald.simulator.ui;

import java.awt.GridLayout;
import javax.swing.JPanel;

import com.fitzgerald.simulator.instruction.Sub;

public class StageTable extends JPanel {
    
    protected StageStatus[] stages;
    
    public StageTable() {
        super();
        
        stages = new StageStatus[3];
        
        Sub sub = new Sub();
        sub.setOperand(1, new byte[] {0, 0, 0, 1});
        sub.setOperand(2, new byte[] {0, 0, 0, 2});
        sub.setOperand(3, new byte[] {0, 0, 0, 3});
        
        stages[0] = new StageStatus("Fetch");
        stages[0].setInstruction(sub);
        stages[1] = new StageStatus("Decode");
        stages[1].setInstruction(sub);
        stages[2] = new StageStatus("Execute");
        stages[2].setInstruction(sub);
        
        GridLayout layoutMgr = new GridLayout(3, 1);
        setLayout(layoutMgr);
        
        for (StageStatus stage : stages) {
            add(stage);
        }
        
        setVisible(true);
    }
    
}
