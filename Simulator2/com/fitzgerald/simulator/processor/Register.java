package com.fitzgerald.simulator.processor;

public class Register {
    
    /**
     * Register number
     */
    protected int registerNum;
    
    protected int value;
    
    /**
     * Create a register with the given number
     */
    public Register(int registerNum) {
        this.registerNum = registerNum;
        
        this.value = 0;
    }
    
    /**
     * Get the value of the register
     * @return The value of the register
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the register
     * @param newValue New value to set
     */
    public void setValue(int newValue) {
        value = newValue;
        
        if (registerNum == 4) {
            System.out.println("r4 = " + newValue);
        }
    }
    
}
