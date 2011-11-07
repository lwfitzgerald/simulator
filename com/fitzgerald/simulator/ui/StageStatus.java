package com.fitzgerald.simulator.ui;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fitzgerald.simulator.instruction.Instruction;

public class StageStatus extends JPanel {
    
    protected JLabel instruction;
    
    public StageStatus(String stageName) {
        super();
        
        GridLayout layoutMgr = new GridLayout(2, 1);
        setLayout(layoutMgr);
        
        add(new JLabel(stageName));
        
        instruction = new JLabel("");
        add(instruction);
        
        setVisible(true);
    }
    
    /**
     * Update the instruction in this stage
     * @param instruction Instruction to set
     */
    public void setInstruction(Instruction instruction) {
        this.instruction.setText(instruction.toString());
    }
    
}
