package com.fitzgerald.simulator.ui;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.fitzgerald.simulator.instruction.Instruction;

public class StageStatus extends JPanel {
    
    protected JLabel instruction;
    
    public StageStatus(String stageName) {
        super();
        
        instruction = new JLabel("");
        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        
        JLabel stageLabel = new JLabel(stageName);
        
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                .addComponent(stageLabel)
                .addComponent(instruction)
        );
        
        layout.setHorizontalGroup(
                layout.createParallelGroup()
                .addComponent(stageLabel)
                .addComponent(instruction)
        );
        
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
