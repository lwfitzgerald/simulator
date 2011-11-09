package com.fitzgerald.simulator.ui;

import javax.swing.SwingUtilities;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Simulator;

public class UI implements Runnable {
    
    protected Simulator simulator;
    protected MainWindow mainWindow;
    protected boolean initialised = false;
    
    public UI(Simulator simulator) {
        this.simulator = simulator;
        SwingUtilities.invokeLater(this);
    }
    
    public void run() {
        mainWindow = new MainWindow(simulator);
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
    
    /**
     * Sets the displayed cycle count
     * @param cycleCount Number of cycles to display
     */
    public void setCycleCount(int cycleCount) {
        mainWindow.setCycleCount(cycleCount);
    }
    
}
