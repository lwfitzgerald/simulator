package com.fitzgerald.simulator.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSeparator;

import com.fitzgerald.simulator.instruction.Instruction;

public class MainWindow extends JFrame {
    protected StageTable stageTable;
    protected RegisterTable registerTable;
    
    public MainWindow() {
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        stageTable = new StageTable();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(stageTable, c);
        
        registerTable = new RegisterTable();
        c.gridx = 1;
        add(registerTable, c);
        
        JButton stepButton = new JButton("Step");
        stepButton.setMinimumSize(new Dimension(82, 19));
        stepButton.setMaximumSize(new Dimension(82, 19));
        stepButton.setPreferredSize(new Dimension(82, 19));
        
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        add(new JSeparator(), c);
        
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        add(stepButton, c);
        
        pack();
        setVisible(true);
    }
    
    /**
     * Sets the instruction shown in the UI for this stage
     * @param stageNum Stage number
     * @param instruction Instruction to set
     */
    public void setStageInstruction(int stageNum, Instruction instruction) {
        stageTable.setStageInstruction(stageNum, instruction);
    }
    
    /**
     * Set the value of a register status box
     * @param registerNum Register number
     * @param value Value to set to
     */
    public void setRegisterValue(int registerNum, byte[] value) {
        registerTable.setRegisterValue(registerNum, value);
    }
}
