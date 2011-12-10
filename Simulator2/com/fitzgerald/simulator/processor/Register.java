package com.fitzgerald.simulator.processor;

public class Register {
    
    /**
     * Register number
     */
    protected int registerNum;
    
    protected int currentValue;
    protected Integer nextValue;
    
    /**
     * Create a register with the given number
     */
    public Register(int registerNum) {
        this.registerNum = registerNum;
        
        this.currentValue = 0;
        this.nextValue = null;
    }
    
    /**
     * Get the current value of the register
     * @return The current value of the register
     */
    public int getCurrentValue() {
        return currentValue;
    }

    /**
     * Set the current value of the register
     * @param newValue New value to set
     */
    public void setCurrentValue(int newValue) {
        currentValue = newValue;
    }

    /**
     * Get the next value of the register
     * @return The next value of the register
     */
    public int getNextValue() {
        return nextValue;
    }
    
    /**
     * Set the next value of the register
     * @param newValue New value to set
     */
    public void setNextValue(int newValue) {
        nextValue = newValue;
    }
    
    /**
     * Copy the next value to the current value
     * of the register
     */
    public void finishStep() {
        if (nextValue != null) {
            currentValue = nextValue;
            nextValue = null;
        }
    }
    
}
