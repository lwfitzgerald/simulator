package com.fitzgerald.simulator.processor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.LinkedList;

import com.fitzgerald.simulator.ui.UI;

public class Simulator {
    
    /**
     * Processor linked to memory, registers etc
     */
    protected Processor processor;
    
    /**
     * UI controller
     */
    protected UI ui;
    
    public static void main(String[] args) {
        /*RegisterFile registerFile = RegisterFile.getSingleton();
        Register intReg0 = registerFile.getRegister(0);
        intReg0.setCurrentValue(0);
        intReg0.setNextValue(5);
        intReg0.finishCycle();
        System.out.println(intReg0.getCurrentValue());*/
        
        //test();
        
        // args[0] = "Compiled" program path
        
        if (args.length < 1) {
            System.err.println("No program argument given!");
            System.exit(1);
        }
        
        new Simulator(args[0]);
    }
    
    /**
     * Runs the Simulator for the given program file
     * @param programFile Program file to load and run
     */
    public Simulator(String programFile) {
        // Initialise the UI
        ui = new UI();
        
        try {
            processor = new Processor(loadProgram(programFile), new Memory(), ui);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load supplied program file");
            System.exit(1);
        }
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        
        while (processor.step()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Halting");
    }
    
    protected Program loadProgram(String programPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(programPath);
        ObjectInputStream objectStream = new ObjectInputStream(inputStream);
        
        return (Program) objectStream.readObject();
    }
    
    public static void test() {
        System.out.println("Starting tests");
        
        Register.test();
        
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
