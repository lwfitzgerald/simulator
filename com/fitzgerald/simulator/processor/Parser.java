package com.fitzgerald.simulator.processor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;

import com.fitzgerald.simulator.instruction.Instruction;

public class Parser {
    
    /**
     * Stores the line number, operand number and label
     * needing replacement in the second pass
     */
    protected class OperandLabelReplace {
        protected int instructionAddr;
        protected int operandNo;
        protected String label;
        
        public OperandLabelReplace(int instructionAddr, int operandNo, String label) {
            this.instructionAddr = instructionAddr;
            this.operandNo = operandNo;
            this.label = label;
        }
        
        public int getInstructionAddr() {
            return instructionAddr;
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
    
    /**
     * Counts up through the addresses of instructions
     * during parsing
     */
    protected int addressCounter = 0;
    
    public Program parseProgram(String filename) {
        firstPass(filename);
        secondPass();
        
        return new Program(instructions);
    }
    
    protected void firstPass(String inputFilename) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(inputFilename);
        } catch (FileNotFoundException e) {
            System.err.println("Supplied program file not found");
            System.exit(1);
        }
        
        InputStreamReader reader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        
        String line;
        
        try {
            while ((line = bufferedReader.readLine()) != null) {
                parseLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error whilst parsing file");
            System.exit(1);
        }
        
        instructions = (Instruction[]) instructionList.toArray(new Instruction[0]);
    }
    
    protected void secondPass() {
        for (OperandLabelReplace toReplace : labelsToReplace) {
            int instructionAddr = toReplace.getInstructionAddr();
            String label = toReplace.getLabel();
            Instruction instruction = instructions[instructionAddr / 4];
            
            instruction.setOperand(toReplace.getOperandNo(), labels.get(label));
        }
    }
    
    protected void parseLine(String lineStr) {
        // Strip comments
        lineStr = stripComments(lineStr);
        
        // Remove preceding and trailing whitespace
        lineStr = lineStr.trim();
        
        int colonPos;
        
        if ((colonPos = lineStr.indexOf(":")) != -1) {
            // We have a label
            String label = lineStr.substring(0, colonPos).replaceAll("[ \t]", "");
            
            // Remove it from the line
            lineStr = lineStr.substring(colonPos+1, lineStr.length()).trim();
            
            // Store it in the lookup table
            labels.put(label, addressCounter);
        }
        
        lineStr = stripWhitespace(lineStr);
        
        if (lineStr.length() == 0) {
            // Blank / only comments line
            return;
        }
        
        String[] instructionSplit = lineStr.split(" ");
        String opcode = instructionSplit[0];
        
        // Get an instance of the relevant instruction class
        String className = opcode.substring(0, 1).toUpperCase() + opcode.substring(1).toLowerCase();
        
        Instruction instruction = null;
        try {
            instruction = (Instruction) Class.forName("com.fitzgerald.simulator.instruction." + className).newInstance();
        } catch (Exception e) {
            System.err.println("Instruction instantiation failed");
            e.printStackTrace();
            System.exit(1);
        }
        
        // Set operands in the object
        if (instructionSplit.length > 1) {
            String[] operands = instructionSplit[1].split(",");
            
            if (operands.length >= 1) {
                String operand1 = operands[0];
                
                if (!operand1.matches("^r?[0-9]+")) {
                    // Label
                    labelsToReplace.addLast(new OperandLabelReplace(addressCounter, 1, operand1));
                } else {
                    operand1 = operand1.replace("r", "");
                    instruction.setOperand(1, Integer.parseInt(operand1));
                }
                
                if (operands.length >= 2) {
                    String operand2 = operands[1];
                    
                    if (!operand2.matches("^r?[0-9]+")) {
                        // Label
                        labelsToReplace.addLast(new OperandLabelReplace(addressCounter, 2, operand2));
                    } else {
                        operand2 = operand2.replace("r", "");
                        instruction.setOperand(2, Integer.parseInt(operand2));
                    }
                    
                    if (operands.length >= 3) {
                        String operand3 = operands[2];
                        
                        if (!operand3.matches("^r?[0-9]+")) {
                            // Label
                            labelsToReplace.addLast(new OperandLabelReplace(addressCounter, 3, operand3));
                        } else {
                            operand3 = operand3.replace("r", "");
                            instruction.setOperand(3, Integer.parseInt(operand3));
                        }
                    } else {
                        instruction.setOperand(3, 0);
                    }
                } else {
                    instruction.setOperand(2, 0);
                    instruction.setOperand(3, 0);
                }
            } else {
                // No operands so set to 0
                instruction.setOperand(1, 0);
                instruction.setOperand(2, 0);
                instruction.setOperand(3, 0);
            }
        }
        
        // Insert into the instruction list
        instructionList.addLast(instruction);
        
        // Increment the address counter for the next instruction
        addressCounter += 4;
    }
    
    protected String stripComments(String str) {
        int hashPos = str.indexOf("#");
        
        if (hashPos == -1) {
            // No comments
            return str;
        }
        
        return str.substring(0, hashPos);
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
