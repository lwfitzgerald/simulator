package com.fitzgerald.simulator;

public class IntegerRegister {
    
    protected Integer currentValue;
    protected Integer nextValue;
    
    public Integer getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int newValue) {
        currentValue = newValue;
    }

    public Integer getNextValue() {
        return nextValue;
    }
    
    
    public void setNextValue(int newValue) {
        nextValue = newValue;
    }
    
    public void finishCycle() {
        currentValue = nextValue;
        nextValue = null;
    }
    
    public static void test_getCurrentValue() {
        String identifier = "IntegerRegister.getCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final int testVal = 21;
        
        IntegerRegister testReg = new IntegerRegister();
        testReg.currentValue = testVal;
        
        if (testReg.getCurrentValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setCurrentValue() {
        String identifier = "IntegerRegister.setCurrentValue";
        
        TestUtil.startTest(identifier);
        
        final int testVal = 35;
        
        IntegerRegister testReg = new IntegerRegister();
        testReg.setCurrentValue(testVal);
        
        if (testReg.currentValue != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_getNextValue() {
        String identifier = "IntegerRegister.getNextValue";
        TestUtil.startTest(identifier);
        
        final int testVal = 21;
        
        IntegerRegister testReg = new IntegerRegister();
        testReg.nextValue = testVal;
        
        if (testReg.getNextValue() != testVal) {
            TestUtil.testFailed(identifier);
            return;
        }
        
        TestUtil.testPassed(identifier);
    }
    
    public static void test_setNextValue() {
        String identifier = "IntegerRegister.setNextValue";
        TestUtil.startTest(identifier);
        
        final int testVal = 35;
        
        IntegerRegister testReg = new IntegerRegister();
        testReg.setNextValue(testVal);
        
        if (testReg.nextValue != testVal) {
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
    }
}
