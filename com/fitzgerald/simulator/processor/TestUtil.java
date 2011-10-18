package com.fitzgerald.simulator.processor;

import java.util.LinkedList;

public class TestUtil {
    
    protected static LinkedList<String> failedTests = new LinkedList<String>();
    
    public static LinkedList<String> getFailedTests() {
        return failedTests;
    }
    
    public static void startTest(String identifier) {
        System.out.print("Testing " + identifier + "...");
    }
    
    public static void testFailed(String identifier) {
        System.out.println(" FAILED");
        failedTests.addLast(identifier);
    }
    
    protected static void testPassed(String identifier) {
        System.out.println(" PASSED");
    }
}
