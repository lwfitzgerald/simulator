package com.fitzgerald.simulator.ui;

import javax.swing.SwingUtilities;

public class UI implements Runnable {
    
    protected MainWindow mainWindow;
    protected boolean initialised = false;
    
    public UI() {
        SwingUtilities.invokeLater(this);
    }
    
    public void run() {
        mainWindow = new MainWindow();
    }
    
    /**
     * Set the value of a register status box
     * @param registerNum Register number
     * @param value Value to set to
     */
    public void setRegisterValue(int registerNum, byte[] value) {
        mainWindow.setRegisterValue(registerNum, value);
    }
}
