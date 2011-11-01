package com.fitzgerald.simulator.processor;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Random;

import com.fitzgerald.simulator.instruction.Instruction;

public class Memory {
    
    /**
     * Program "loaded" into memory
     */
    protected Program program;
    
    /**
     * HashMap mapping memory address to value
     */
    protected HashMap<Integer, Integer> memorySpace = new HashMap<Integer, Integer>();
    
    /**
     * Initialises memory with the program from
     * the given program
     * @param programPath Path to program file
     */
    public Memory(String programPath) throws Exception {
        loadProgram(programPath);
    }
    
    protected void loadProgram(String programPath) throws Exception {
        FileInputStream inputStream = new FileInputStream(programPath);
        ObjectInputStream objectStream = new ObjectInputStream(inputStream);
        
        program = (Program) objectStream.readObject();
    }
    
    /**
     * Get instruction at memory address
     * @param memoryAddress
     * @return
     */
    public Instruction getInstruction(int memoryAddress) {
        if (memoryAddress >= program.getEndLocation()) {
            /*
             * If we're outside the memory area of the program
             * return null
             */
            return null;
        }
        
        return program.getInstruction(memoryAddress);
    }
    
    /**
     * Load from a memory address
     * @param memoryAddress Memory address to load from
     * @return Word at address
     * @throws MemoryException
     */
    public Integer load(int memoryAddress) throws MemoryException {
        // Check we're not trying to read in the program memory area
        if (memoryAddress < program.getEndLocation()) {
            throw new MemoryException("Attempting to read from program memory area");
        }
        
        Integer loaded;
        
        if ((loaded = memorySpace.get(memoryAddress)) == null) {
            /*
             * Memory has not been written to / read yet
             * 
             * In a real machine there would be a random value here so
             * simulate that by writing a random value (for consistency)
             * and returning that
             */
            Random generator = new Random();
            loaded = generator.nextInt();
            memorySpace.put(memoryAddress, loaded);
        }
        
        return loaded;
    }
    
    public void store(int memoryAddress) throws MemoryException {
        // Check we're not trying to write in the program memory area
        if (memoryAddress < program.getEndLocation()) {
            throw new MemoryException("Attempting to write to program memory area");
        }
    }
    
}
