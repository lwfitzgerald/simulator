package com.fitzgerald.simulator.ui;

import java.awt.GridLayout;

import javax.swing.JFrame;

import com.fitzgerald.simulator.instruction.Instruction;

public class MainWindow extends JFrame {
    protected StageTable stageTable;
    protected RegisterTable registerTable;
    
    public MainWindow() {
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        
        GridLayout layoutMgr = new GridLayout(1, 2);
        setLayout(layoutMgr);
        
        stageTable = new StageTable();
        add(stageTable);
        
        registerTable = new RegisterTable();
        add(registerTable);
        
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
