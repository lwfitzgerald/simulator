package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.ui.UI;

public class RegisterFile {
    
    public static final int NUM_REGISTERS = 16;
    
    protected Register[] registerArray = null;
    
    /**
     * UI control object reference
     */
    protected UI ui;
    
    public RegisterFile(UI ui) {
        this.ui = ui;
        
        // Create the arrays holding the entries for each register
        registerArray = new Register[NUM_REGISTERS];
        
        for (int i=0; i < NUM_REGISTERS; i++) {
            registerArray[i] = new Register(i, ui);
        }
        
        updateUI();
    }
    
    /**
     * Get the register with the given register number
     * @param regNumber Number of register to return
     * @return Register object
     */
    public Register getRegister(int regNumber) {
        return registerArray[regNumber];
    }
    
    /**
     * Called at the end of a simulation step
     * to update the "current" values of the registers
     */
    public void finishStep() {
        for (Register reg : registerArray) {
            reg.finishStep();
        }
    }
    
    public void updateUI() {
        for (Register register : registerArray) {
            register.updateUI();
        }
    }
}
