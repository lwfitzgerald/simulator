package com.fitzgerald.simulator.assembler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.processor.Util;

public class Assembler {
    
    /**
     * Stores the line number, operand number and label
     * needing replacement in the second pass
     */
    protected class OperandLabelReplace {
        protected int lineNo;
        protected int operandNo;
        protected String label;
        
        public OperandLabelReplace(int lineNo, int operandNo, String label) {
            this.lineNo = lineNo;
            this.operandNo = operandNo;
            this.label = label;
        }
        
        public int getLineNo() {
            return lineNo;
        }
        
        public int getOperandNo() {
            return operandNo;
        }
        
        public String getLabel() {
            return label;
        }
    }
    
    /**
     * Hashmap to allow lookups of labels
     */
    protected HashMap<String, Integer> labels = new HashMap<String, Integer>();

    /**
     * Linked list used during the first pass
     * to hold generated instructions
     */
    protected LinkedList<Instruction> instructionList = new LinkedList<Instruction>();
    
    /**
     * Array to hold instructions after the first
     * pass has completed. This is then serialised to form
     * the output file.
     */
    protected Instruction[] instructions;
    
    /**
     * Linked list of instructions needing label replacement
     * in second pass
     */
    protected LinkedList<OperandLabelReplace> labelsToReplace = new LinkedList<OperandLabelReplace>();
    
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
        //parseLine("   beginning   :   add    r1,    r2,    r3   ", 1);
        
        firstPass(inputFilename);
        secondPass();
    }
    
    protected void firstPass(String inputFilename) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFilename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        
        String line;
        int lineCounter = 0;
        
        try {
            while ((line = bufferedReader.readLine()) != null) {
                parseLine(line, lineCounter++);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
        instructions = (Instruction[]) instructionList.toArray(new Instruction[0]);
    }
    
    protected void secondPass() {
        for (OperandLabelReplace toReplace : labelsToReplace) {
            byte[] value = Util.intToBytes(labels.get(toReplace.getLabel()));
            instructions[toReplace.getLineNo()].setOperand(toReplace.getOperandNo(), value);
        }
    }
    
    protected void parseLine(String line, int lineNo) {
        // Remove preceding and trailing whitespace
        line = line.trim();
        
        int colonPos;
        
        if ((colonPos = line.indexOf(":")) != -1) {
            // We have a label
            String label = line.substring(0, colonPos).replaceAll("[ \t]", "");
            
            // Remove it from the line
            line = line.substring(colonPos+1, line.length()).trim();
            
            // Store it in the lookup table
            labels.put(label, lineNo * 4);
        }
        
        line = stripWhitespace(line);
        
        String[] instructionSplit = line.split(" ");
        String opcode = instructionSplit[0];
        String[] operands = instructionSplit[1].split(",");
        
        // Get an instance of the relevant instruction class
        String className = opcode.substring(0, 1).toUpperCase() + opcode.substring(1).toLowerCase();
        
        Instruction instruction = null;
        try {
            instruction = (Instruction) Class.forName("com.fitzgerald.simulator.instruction." + className).newInstance();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        // Set operands in the object
        if (operands.length >= 1) {
            String operand1 = operands[0];
            
            if (!operand1.matches("r?[0-9]")) {
                // Label
                labelsToReplace.addLast(new OperandLabelReplace(lineNo, 1, operand1));
            } else {
                operand1 = operand1.replace("r", "");
                instruction.setOperand(1, Util.intToBytes(Integer.parseInt(operand1)));
            }
            
            if (operands.length >= 2) {
                String operand2 = operands[1];
                
                if (!operand2.matches("r?[0-9]")) {
                    // Label
                    labelsToReplace.addLast(new OperandLabelReplace(lineNo, 2, operand2));
                } else {
                    operand2 = operand2.replace("r", "");
                    instruction.setOperand(2, Util.intToBytes(Integer.parseInt(operand2)));
                }
                
                if (operands.length >= 3) {
                    String operand3 = operands[2];
                    
                    if (!operand3.matches("r?[0-9]")) {
                        // Label
                        labelsToReplace.addLast(new OperandLabelReplace(lineNo, 3, operand3));
                    } else {
                        operand3 = operand3.replace("r", "");
                        instruction.setOperand(3, Util.intToBytes(Integer.parseInt(operand3)));
                    }
                } else {
                    instruction.setOperand(3, Util.intToBytes(0));
                }
            } else {
                instruction.setOperand(2, Util.intToBytes(0));
                instruction.setOperand(3, Util.intToBytes(0));
            }
        } else {
            // No operands so set to 0
            instruction.setOperand(1, Util.intToBytes(0));
            instruction.setOperand(2, Util.intToBytes(0));
            instruction.setOperand(3, Util.intToBytes(0));
        }
        
        // Insert into the instruction list
        instructionList.addLast(instruction);
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
