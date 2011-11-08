package com.fitzgerald.simulator.ui;

import javax.swing.SwingUtilities;

import com.fitzgerald.simulator.instruction.Instruction;

public class UI implements Runnable {
    
    protected MainWindow mainWindow;
    protected boolean initialised = false;
    
    public UI() {
        //SwingUtilities.invoke(this);
        run();
    }
    
    public void run() {
        mainWindow = new MainWindow();
    }
    
    /**
     * Sets the instruction shown in the UI for this stage
     * @param stageNum Stage number
     * @param instruction Instruction to set
     */
    public void setStageInstruction(int stageNum, Instruction instruction) {
        mainWindow.setStageInstruction(stageNum, instruction);
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
