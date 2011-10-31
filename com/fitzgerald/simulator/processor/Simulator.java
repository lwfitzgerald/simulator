package com.fitzgerald.simulator.processor;

import java.util.LinkedList;
import java.util.ListIterator;

public class Simulator {

	public static void main(String[] args) {
	    RegisterFile registerFile = RegisterFile.getSingleton();
	    Register intReg0 = registerFile.getRegister(0);
	    intReg0.setCurrentValue(0);
	    intReg0.setNextValue(5);
	    intReg0.finishCycle();
	    System.out.println(intReg0.getCurrentValue());
	    
	    test();
	}
	
	public static void test() {
	    System.out.println("Starting tests");
	    
	    Register.test();
	    RegisterFile.test();
	    
	    LinkedList<String> failedTests = TestUtil.getFailedTests();
	    
	    if (failedTests.size() > 0) {
	        System.err.println("Some tests failed:");
	        
	        ListIterator<String> itr = failedTests.listIterator();
	        
	        while (itr.hasNext()) {
	            System.err.println("Testing " + itr.next() + " FAILED");
	        }
	    } else {
	        System.out.println("All tests passed :)");
	    }
	}
}
