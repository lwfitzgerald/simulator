package com.fitzgerald.simulator.processor;

public class Simulator {
    
    /**
     * Processor linked to memory, registers etc
     */
    protected Processor processor;
    
    protected static boolean branchTable = true;
    protected static boolean stepping = false;
    
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("No program argument given!\n");
            printHelp();
            System.exit(1);
        }
        
        if (args[0].equals("--help") || args[0].equals("-h")) {
            printHelp();
            System.exit(0);
        }
        
        if (args.length > 1) {
            // We have a flag
            if (args[1].equals("--no-branch-table")) {
                System.out.println("Disabling branch table\n");
                branchTable = false;
            }
        }
               
        new Simulator(args[0], branchTable);
    }
    
    protected static void printHelp() {
        System.out.println("Run as:");
        System.out.println("\tjava com.fitzgerald.simulator.processor.Simulator ASMFILE [--no-branch-table]");
        System.out.println("\n\t--no-branch-table\t Optional argument disabling the branch result table when predicting\n");
    }
    
    /**
     * Runs the Simulator for the given program file
     * @param programFile Program file to load and run
     * @param branchTable Whether or not to use the branch
     * table prediction mechanism
     */
    public Simulator(String programFile, boolean branchTable) {
        Parser parser = new Parser();
        Program program;
        
        try {
            program = parser.parseProgram(programFile);
            processor = new Processor(program, branchTable);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load supplied program file");
            System.exit(1);
        }
        
        while (processor.step()) {
        }
    }
    
}
