package com.fitzgerald.simulator.processor;

import java.util.HashMap;

public class MemoryController {
    
    /**
     * Number of ticks required for a read or write
     * of memory
     */
    protected static final int MEM_TICKS_REQ = 10;
    
    /**
     * Reference to memory object
     */
    protected Memory memory;
    
    /**
     * Hash map for load requests
     * Maps location onto ticks remaining
     */
    protected HashMap<Integer, Integer> loadRequests = new HashMap<Integer, Integer>();
    
    /**
     * Hash map for write requests
     * Maps location onto ticks remaining
     */
    protected HashMap<Integer, Integer> writeRequests = new HashMap<Integer, Integer>();
    
    /**
     * Constructor
     * @param memory Reference to memory object
     */
    public MemoryController(Memory memory) {
        this.memory = memory;
    }
    
    /**
     * Request a load from memory
     * @param memoryLocation Memory location to load
     * @return Word requested or null indicating more cycles
     * are required to fulfil the request
     */
    public byte[] load(int memoryLocation) {
        Integer ticksRemaining;
        
        if ((ticksRemaining = loadRequests.get(memoryLocation)) == null) {
            // First request for this memory location
            ticksRemaining = MEM_TICKS_REQ;
            
            // Return null to indicate more cycles needed to fulfil load
            return null;
        }
        
        if (ticksRemaining == 0) {
            // Number of ticks satisfied, return the value in memory
            return memory.load(memoryLocation);
        } else {
            // Number of ticks NOT satisfied, return null to indicate so
            return null;
        }
    }
    
    /**
     * Request a store to memory
     * @param memoryLocation Memory location to store into
     * @param data Data to store in memory
     * @return True if data was stored, false if more cycles
     * are required to complete the store
     */
    public boolean store(int memoryLocation, byte[] data) {
        Integer ticksRemaining;
        
        if ((ticksRemaining = writeRequests.get(memoryLocation)) == null) {
            // First request for this memory location
            ticksRemaining = MEM_TICKS_REQ;
            
            // Return false to indicate more cycles needed to fulfil store
            return false;
        }
        
        if (ticksRemaining == 0) {
            // Number of ticks satisfied, perform the store
            
            /*
             * Do a deep copy on the data to prevent changes due to
             * shared array references
             */
            memory.store(memoryLocation, data.clone());
            return true;
        } else {
            // Number of ticks NOT satisfied, return false to indicate so
            return false;
        }
    }
    
}
