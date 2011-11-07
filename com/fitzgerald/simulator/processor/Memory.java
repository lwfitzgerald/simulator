package com.fitzgerald.simulator.processor;

import java.util.HashMap;
import java.util.Random;

public class Memory {
    
    /**
     * HashMap mapping memory address to value
     */
    protected HashMap<Integer, byte[]> memorySpace = new HashMap<Integer, byte[]>();
    
    /**
     * Load from a memory address
     * @param memoryAddress Memory address to load from
     * @return Word at address
     * @throws MemoryException
     */
    public byte[] load(int memoryAddress) {
        byte[] loaded;
        
        if ((loaded = memorySpace.get(memoryAddress)) == null) {
            /*
             * Memory has not been written to / read yet
             * 
             * In a real machine there would be a random value here so
             * simulate that by writing a random value (for consistency)
             * and returning that
             */
            Random generator = new Random();
            loaded = Util.intToBytes(generator.nextInt());
            memorySpace.put(memoryAddress, loaded);
        }
        
        return loaded;
    }
    
    public void store(int memoryAddress, byte[] toStore) {
        memorySpace.put(memoryAddress, toStore);
    }
    
}
