package com.fitzgerald.simulator;

public class FloatRegister {
    
    protected Float currentValue;
    protected Float nextValue;
    
    public Float getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(Float newValue) {
        currentValue = newValue;
    }

    public Float getNextValue() {
        return nextValue;
    }
    
    
    public void setNextValue(Float newValue) {
        nextValue = newValue;
    }
    
    public void finishCycle() {
        currentValue = nextValue;
        nextValue = null;
    }
    
    public static void test_getCurrentValue() {
        String identifier = "FloatRegister.getCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final Float testVal = 21.123f;
        
        FloatRegister testReg = new FloatRegister();
        testReg.currentValue = testVal;
        
        if (testReg.getCurrentValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setCurrentValue() {
        String identifier = "FloatRegister.setCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final Float testVal = 35.532f;
        
        FloatRegister testReg = new FloatRegister();
        testReg.setCurrentValue(testVal);
        
        if (testReg.currentValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_getNextValue() {
        String identifier = "FloatRegister.getNextValue";
        TestUtil.startTest(identifier);
        
        final Float testVal = 21.214f;
        
        FloatRegister testReg = new FloatRegister();
        testReg.nextValue = testVal;
        
        if (testReg.getNextValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setNextValue() {
        String identifier = "FloatRegister.setNextValue";
        TestUtil.startTest(identifier);
        
        final Float testVal = 35.463f;
        
        FloatRegister testReg = new FloatRegister();
        testReg.setNextValue(testVal);
        
        if (testReg.nextValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_finishCycle() {
        String identifier = "FloatRegister.finishCycle";
        TestUtil.startTest(identifier);
        
        final Float testVal1 = 35.353254f;
        final Float testVal2 = 47.324523f;
        
        FloatRegister testReg = new FloatRegister();
        
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
