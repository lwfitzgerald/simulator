package com.fitzgerald.simulator.processor;

public class Simulator {
    
    /**
     * Processor linked to memory, registers etc
     */
    protected Processor processor;
    
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
        Parser parser = new Parser();
        Program program;
        
        try {
            program = parser.parseProgram(programFile);
            processor = new Processor(program, new Memory());
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
        }
        
        //System.out.println("Halting");
    }
    
    /*protected Program loadProgram(String programPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(programPath);
        ObjectInputStream objectStream = new ObjectInputStream(inputStream);
        
        return (Program) objectStream.readObject();
    }*/
    
    /*@Override
    public void actionPerformed(ActionEvent arg0) {
        processor.step();
    }*/
}
