package com.fitzgerald.simulator.processor;

import java.io.Serializable;

import com.fitzgerald.simulator.instruction.Instruction;

public class Program implements Serializable {
    
    /**
     * Serial version ID for serialising
     */
    private static final long serialVersionUID = -5727828910136726501L;
    
    protected Instruction[] instructions;
    
    public Instruction getInstruction(int memoryLocation) {
        // Memory locations are in bytes and word aligned so divide by 4
        if (memoryLocation / 4 >= instructions.length) {
            // No instruction here, return null
            return null;
        }
        
        return instructions[memoryLocation / 4];
    }
    
    /**
     * Return the first free memory location after
     * the end of the program
     * @return First free memory location
     */
    public int getEndLocation() {
        return instructions.length * 4;
    }
}
