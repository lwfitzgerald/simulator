package com.fitzgerald.simulator.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Simulator {
    
    /**
     * Processor linked to memory, registers etc
     */
    protected Processor processor;
    
    protected static boolean branchTable = true;
    protected static boolean stepping = false;
    protected static int fetchDecodeWidth = 3;
    protected static int numALUs = 3;
    protected static int numLoadStoreUnits = 2;
    protected static int numBranchUnits = 2;
    protected static boolean printStatus = true;
    protected static String filename;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No program argument given!\n");
            printHelp();
            System.exit(1);
        }
        
        for (String arg : args) {
            parseArgument(arg);
        }
               
        new Simulator();
    }
    
    protected static void parseArgument(String arg) {
        if (arg.equals("--help") || arg.equals("-h")) {
            printHelp();
            System.exit(0);
        }
        
        if (arg.equals("--step")) {
            System.out.println("Enabling stepping");
            stepping = true;
            return;
        }
        
        if (arg.equals("--no-status")) {
            System.out.println("Disabling status output");
            printStatus = false;
            return;
        }
        
        if (arg.equals("--no-branch-table")) {
            System.out.println("Disabling branch table\n");
            branchTable = false;
            return;
        }
        
        if (arg.matches("--fd-width=\\d")) {
            fetchDecodeWidth = Integer.valueOf(arg.substring(11));
            System.out.println("Setting fetch/decode width to " + fetchDecodeWidth);
            return;
        }
        
        if (arg.matches("--num-alus=\\d")) {
            numALUs = Integer.valueOf(arg.substring(11));
            System.out.println("Setting number of ALUs to " + numALUs);
            return;
        }
        
        if (arg.matches("--num-ls-units=\\d")) {
            numLoadStoreUnits = Integer.valueOf(arg.substring(15));
            System.out.println("Setting number of Load/Store units to " + numLoadStoreUnits);
            return;
        }
        
        if (arg.matches("--num-branch-units=\\d")) {
            numBranchUnits = Integer.valueOf(arg.substring(19));
            System.out.println("Setting number of Branch units to " + numBranchUnits);
            return;
        }
        
        filename = arg;
    }
    
    protected static void printHelp() {
        System.out.println("Run as:");
        System.out.println("\tjava com.fitzgerald.simulator.processor.Simulator ASMFILE args");
        System.out.println("\n\t--step\tOptional argument enabling stepping of cycles");
        System.out.println("\n\t--no-status\tDisable status output at the end of each step");
        System.out.println("\n\t--no-branch-table\tOptional argument disabling the branch result table when predicting\n");
        System.out.println("\n\t--fd-width=NUM\tOptional argument setting fetch/decode width, 3 by default");
        System.out.println("\n\t--num-alus=NUM\tOptional argument setting number of ALUs, 3 by default");
        System.out.println("\n\t--num-ls-units=NUM\tOptional argument setting number of Load/Store units, 2 by default");
        System.out.println("\n\t--num-branch-units=NUM\tOptional argument setting number of Branch units, 2 by default");
        System.out.println("\n\t--help\tShow this message");
    }
    
    /**
     * Runs the Simulator for the given program file
     */
    public Simulator() {
        Parser parser = new Parser();
        Program program;
        
        try {
            program = parser.parseProgram(filename);
            processor = new Processor(program, printStatus, branchTable, fetchDecodeWidth,
                    numALUs, numLoadStoreUnits, numBranchUnits);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load supplied program file");
            System.exit(1);
        }
        
        if (!stepping) {
            while (processor.step()) {}
        } else {
            InputStreamReader isReader = new InputStreamReader(System.in);
            BufferedReader reader = new BufferedReader(isReader);
            
            try {
                while (reader.readLine() != null && processor.step()) {}
            } catch (IOException e) {}
        }
    }
    
}
