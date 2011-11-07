package com.fitzgerald.simulator.ui;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
    protected RegisterTable registerTable;
    
    public MainWindow() {
        super();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        
        registerTable = new RegisterTable();
        add(registerTable);
        
        pack();
        setVisible(true);
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
