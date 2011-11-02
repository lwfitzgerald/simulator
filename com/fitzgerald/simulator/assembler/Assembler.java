package com.fitzgerald.simulator.assembler;

import java.util.HashMap;

public class Assembler {
    
    /**
     * Hashmap to allow lookups of labels
     */
    protected HashMap<String, Integer> labels = new HashMap<String, Integer>();
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Error: Assembler requires two arguments.");
            System.err.println("Usage: ");
            System.err.println("\tjava com.fitzgerald.simulator.assembler.Assembler INPUTFILENAME OUTPUTFILENAME");
        }
        // args[0] = Filename to assemble
        // args[1] = Filename to output assembled program to
        new Assembler(args[0], args[1]);
    }
    
    protected Assembler(String inputFilename, String outputFilename) {
        parseLine("   beginning   :   add    r1,    r2,    r3   ", 1);
    }
    
    protected void parseLine(String line, int lineNo) {
        // Remove preceeding and trailing whitespace
        line = line.trim();
        System.out.println("After trimming: \"" + line + "\"");
        
        int colonPos;
        
        if ((colonPos = line.indexOf(":")) != -1) {
            // We have a label
            String label = line.substring(0, colonPos).replaceAll("[ \t]", "");
            
            // Remove it from the line
            line = line.substring(colonPos+1, line.length()).trim();
            
            // Store it in the lookup table
            labels.put(label, lineNo * 4);
        }
        
        System.out.println("After label removal: \"" + line + "\"");
        
        line = stripWhitespace(line);
        
        System.out.println("After final processing: \"" + line + "\"");
        
        String[] instructionSplit = line.split(" ");
        String opcode = instructionSplit[0];
        String[] operands = instructionSplit[1].split(",");
        String operand1 = operands[0];
        String operand2 = operands[1];
        String operand3 = operands[2];
        
        System.out.println("Done!");
    }
    
    protected String stripWhitespace(String str) {
        /*
         * Strip all whitespace following the first space
         * after the opcode
         */
        int spacePos = str.indexOf(" ");
        str = str.substring(0, spacePos+1) + str.substring(spacePos+1, str.length()).replaceAll("[ \t]", "");
        
        return str;
    }
}
