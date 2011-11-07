package com.fitzgerald.simulator.processor;

public class RegisterFile {
    
    public static final int NUM_REGISTERS = 16;
    
    protected Register[] registerArray = null;
    
    public RegisterFile() {
        // Create the arrays holding the entries for each register
        registerArray = new Register[NUM_REGISTERS];
        
        for (int i=0; i < NUM_REGISTERS; i++) {
            registerArray[i] = new Register();
        }
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
}
