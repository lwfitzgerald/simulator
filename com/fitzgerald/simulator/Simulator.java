package com.fitzgerald.simulator;

import java.util.LinkedList;
import java.util.ListIterator;

public class Simulator {

	public static void main(String[] args) {
	    test();
	}
	
	public static void test() {
	    System.out.println("Starting tests");
	    
	    IntegerRegister.test();
	    FloatRegister.test();
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
