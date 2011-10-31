package com.fitzgerald.simulator.processor;

public class Register {
    
    protected Integer currentValue;
    protected Integer nextValue;
    
    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Integer newValue) {
        currentValue = newValue;
    }

    public Integer getNextValue() {
        return nextValue;
    }
    
    public void setNextValue(Integer newValue) {
        nextValue = newValue;
    }
    
    public void finishCycle() {
        currentValue = nextValue;
        nextValue = null;
    }
    
    public static void test_getCurrentValue() {
        String identifier = "Register.getCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final Integer testVal = 21;
        
        Register testReg = new Register();
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
        
        final Integer testVal = 35;
        
        Register testReg = new Register();
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
        
        final Integer testVal = 21;
        
        Register testReg = new Register();
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
        
        final Integer testVal = 35;
        
        Register testReg = new Register();
        testReg.setNextValue(testVal);
        
        if (testReg.nextValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_finishCycle() {
        String identifier = "Register.finishCycle";
        TestUtil.startTest(identifier);
        
        final Integer testVal1 = 35;
        final Integer testVal2 = 47;
        
        Register testReg = new Register();
        
        testReg.currentValue = testVal1;
        testReg.nextValue = testVal2;
        
        testReg.finishCycle();
        
        // Check the next value has been moved into the current
        if (testReg.currentValue != testVal2) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        // Check the next value has been set to null
        if (testReg.nextValue != null) {
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
        test_finishCycle();
    }
}
