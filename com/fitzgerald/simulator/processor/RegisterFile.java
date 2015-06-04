package com.fitzgerald.simulator.processor;

public class RegisterFile {
    
    protected Register[] registerArray = null;
    
    public RegisterFile() {
        // Create the arrays holding the entries for each register
        registerArray = new Register[Processor.NUM_REGISTERS];
        
        for (int i=0; i < Processor.NUM_REGISTERS; i++) {
            registerArray[i] = new Register(i);
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
    
}
