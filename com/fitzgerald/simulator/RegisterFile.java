package com.fitzgerald.simulator;

public class RegisterFile {
    
    protected static final int NUM_INTEGER_REGISTERS = 10;
    protected static final int NUM_FLOAT_REGISTERS   = 10;
    
    protected int[] intRegisterArray;
    protected float[] floatRegisterArray;
    
    /**
     * Singleton instance
     */
    protected static RegisterFile singletonInstance = null;
    
    /**
     * Private constructor
     */
    protected RegisterFile() {
        // Create the arrays holding the entries for each register
        intRegisterArray = new int[NUM_INTEGER_REGISTERS];
        floatRegisterArray = new float[NUM_FLOAT_REGISTERS];
    }
    
    /**
     * Get the RegisterFile singleton object
     * @return RegisterFile Singleton object
     */
    public static RegisterFile getSingleton() {
        // Create an instance if necessary
        if (singletonInstance == null) {
            singletonInstance = new RegisterFile();

        }
        
        return singletonInstance;
    }
}
