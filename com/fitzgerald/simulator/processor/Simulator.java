package com.fitzgerald.simulator.processor;

import java.util.LinkedList;

public class Simulator {
    
    protected static Simulator singleton;

    protected Processor processor;
    
	public static void main(String[] args) {
	    /*RegisterFile registerFile = RegisterFile.getSingleton();
	    Register intReg0 = registerFile.getRegister(0);
	    intReg0.setCurrentValue(0);
	    intReg0.setNextValue(5);
	    intReg0.finishCycle();
	    System.out.println(intReg0.getCurrentValue());*/
	    
	    test();
	}
	
    public Simulator getSingleton() {
        return singleton;
    }
    
	/**
	 * Creates an instance of the simulator for the given
	 * program file
	 * @param programFile Program file to load and run
	 */
	public Simulator(String programFile) {
	    
	}
	
	public static void test() {
	    System.out.println("Starting tests");
	    
	    Register.test();
	    RegisterFile.test();
	    
	    LinkedList<String> failedTests = TestUtil.getFailedTests();
	    
	    if (failedTests.size() > 0) {
	        System.err.println("Some tests failed:");
	        
	        for (String function : failedTests) {
	        	System.err.println("Testing " + function + " FAILED");
	        }
	    } else {
	        System.out.println("All tests passed :)");
	    }
	}
}
