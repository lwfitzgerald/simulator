package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.ui.UI;

public class Register {
    
    /**
     * Register number
     */
    protected int registerNum;
    
    protected byte[] currentValue;
    protected byte[] nextValue;
    
    /**
     * UI control object reference
     */
    protected UI ui;
    
    /**
     * Constructor
     */
    public Register(int registerNum, UI ui) {
        this.registerNum = registerNum;
        this.ui = ui;
        
        // Initialise value to 0
        currentValue = new byte[] {0, 0, 0, 0};
    }
    
    public byte[] getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(byte[] newValue) {
        currentValue = newValue;
    }

    public byte[] getNextValue() {
        return nextValue;
    }
    
    public void setNextValue(byte[] newValue) {
        nextValue = newValue;
    }
    
    public void finishStep() {
        if (nextValue != null) {
            currentValue = nextValue;
        }
    }
    
    public void updateUI() {
        // Update UI
        ui.setRegisterValue(registerNum, currentValue);
    }
    
    public static void test_getCurrentValue() {
        String identifier = "Register.getCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final byte[] testVal = Util.intToBytes(21);
        
        Register testReg = new Register(0, null);
        testReg.currentValue = testVal;
        
        if (testReg.getCurrentValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setCurrentValue() {
        String identifier = "Register.setCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final byte[] testVal = Util.intToBytes(35);
        
        Register testReg = new Register(0, null);
        testReg.setCurrentValue(testVal);
        
        if (testReg.currentValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_getNextValue() {
        String identifier = "Register.getNextValue";
        TestUtil.startTest(identifier);
        
        final byte[] testVal = Util.intToBytes(21);
        
        Register testReg = new Register(0, null);
        testReg.nextValue = testVal;
        
        if (testReg.getNextValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setNextValue() {
        String identifier = "Register.setNextValue";
        TestUtil.startTest(identifier);
        
        final byte[] testVal = Util.intToBytes(35);
        
        Register testReg = new Register(0, null);
        testReg.setNextValue(testVal);
        
        if (testReg.nextValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_finishStep() {
        String identifier = "Register.finishStep";
        TestUtil.startTest(identifier);
        
        final byte[] testVal1 = Util.intToBytes(35);
        final byte[] testVal2 = Util.intToBytes(47);
        
        Register testReg = new Register(0, null);
        
        testReg.currentValue = testVal1;
        testReg.nextValue = testVal2;
        
        testReg.finishStep();
        
        // Check the next value has been moved into the current
        if (testReg.currentValue != testVal2) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test() {
        test_getCurrentValue();
        test_setCurrentValue();
        test_getNextValue();
        test_setNextValue();
        test_finishStep();
    }
}
