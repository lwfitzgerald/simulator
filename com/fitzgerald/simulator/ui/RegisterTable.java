package com.fitzgerald.simulator.ui;

import java.awt.GridLayout;

import javax.swing.JPanel;

import com.fitzgerald.simulator.processor.RegisterFile;

public class RegisterTable extends JPanel {
    
    protected RegisterStatus[] registers;
    
    public RegisterTable() {
        super();
        
        // Initialise the register status indicators
        registers = new RegisterStatus[RegisterFile.NUM_REGISTERS];
        
        for (int i=0; i < RegisterFile.NUM_REGISTERS; i++) {
            registers[i] = new RegisterStatus("R" + i);
        }
        
        GridLayout layoutMgr = new GridLayout(RegisterFile.NUM_REGISTERS / 2, 2);
        setLayout(layoutMgr);
        
        for (RegisterStatus register : registers) {
            add(register);
        }
    }
    
    /**
     * Set the value of a register status box
     * @param registerNum Register number
     * @param value Value to set to
     */
    public void setRegisterValue(int registerNum, byte[] value) {
        registers[registerNum].setValue(value);
    }
    
}
