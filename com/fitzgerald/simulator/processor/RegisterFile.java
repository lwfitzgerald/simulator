package com.fitzgerald.simulator.processor;

public class RegisterFile {
    
    protected static final int NUM_REGISTERS = 10;
    
    protected Register[] registerArray = null;
    
    /**
     * Singleton instance
     */
    protected static RegisterFile singletonInstance = null;
    
    /**
     * Private constructor
     */
    protected RegisterFile() {
        // Create the arrays holding the entries for each register
        registerArray = new Register[NUM_REGISTERS];
        
        for (int i=0; i < NUM_REGISTERS; i++) {
            registerArray[i] = new Register();
        }
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
    
    public Register getRegister(int regNumber) {
        return registerArray[regNumber];
    }
    
    @SuppressWarnings("unused")
    public static void test_sanity() {
        String identifier = "RegisterFile constants sanity";
        
        TestUtil.startTest(identifier);
        
        if (NUM_REGISTERS < 0 || NUM_REGISTERS > 100) {
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
        
        // Check array is initialised
        if (RegisterFile.getSingleton().registerArray == null) {
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
