package com.fitzgerald.simulator;

public class RegisterFile {
    
    protected static final int NUM_INTEGER_REGISTERS = 10;
    protected static final int NUM_FLOAT_REGISTERS   = 10;
    
    protected int[] intRegisterArray = null;
    protected float[] floatRegisterArray = null;
    
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
    
    @SuppressWarnings("unused")
    public static void test_sanity() {
        String identifier = "RegisterFile constants sanity";
        
        TestUtil.startTest(identifier);
        
        if (NUM_INTEGER_REGISTERS < 0 || NUM_INTEGER_REGISTERS > 100) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        if (NUM_FLOAT_REGISTERS < 0 || NUM_FLOAT_REGISTERS > 100) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_getSingleton() {
        String identifier = "RegisterFile::getSingleton";
        
        TestUtil.startTest(identifier);
        
        RegisterFile.singletonInstance = null;
        
        // Check singleton is returned by method
        if (RegisterFile.getSingleton() == null) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        // Check singleton variable is set
        if (RegisterFile.singletonInstance == null) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        // Check arrays are initialised
        if (RegisterFile.getSingleton().intRegisterArray == null
                || RegisterFile.getSingleton().floatRegisterArray == null) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        // Reset singleton to null
        RegisterFile.singletonInstance = null;
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test() {
        test_sanity();
        test_getSingleton();
    }
}
