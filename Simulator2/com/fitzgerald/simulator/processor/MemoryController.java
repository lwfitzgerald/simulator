package com.fitzgerald.simulator.processor;

import java.util.HashMap;

public class MemoryController {
    
    /**
     * Number of ticks required for a read or write
     * of memory
     */
    protected static final int MEM_TICKS_REQ = 5;
    
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
    protected HashMap<Integer, Integer> storeRequests = new HashMap<Integer, Integer>();
    
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
    public Integer load(int memoryLocation) {
        Integer ticksRemaining;
        
        if ((ticksRemaining = loadRequests.get(memoryLocation)) == null) {
            // First request for this memory location
            loadRequests.put(memoryLocation, MEM_TICKS_REQ - 1);
            
            // Return null to indicate more cycles needed to fulfil load
            return null;
        }
        
        if (ticksRemaining == 1) {
            /*
             * Number of ticks satisfied, return the value in memory
             * and clear request
             */
            loadRequests.remove(memoryLocation);
            
            return memory.load(memoryLocation);
        }
        
        /*
         * Number of ticks NOT satisfied, decrement,
         * then return null to indicate so
         */
        loadRequests.put(memoryLocation, ticksRemaining - 1);
        return null;
    }
    
    /**
     * Request a store to memory
     * @param memoryLocation Memory location to store into
     * @param data Data to store in memory
     * @return True if data was stored, false if more cycles
     * are required to complete the store
     */
    public boolean store(int memoryLocation, int data) {
        Integer ticksRemaining;
        
        if ((ticksRemaining = storeRequests.get(memoryLocation)) == null) {
            // First request for this memory location
            storeRequests.put(memoryLocation, MEM_TICKS_REQ - 1);
            
            // Return false to indicate more cycles needed to fulfil store
            return false;
        }
        
        if (ticksRemaining == 1) {
            /*
             * Number of ticks satisfied, perform the store
             * and clear request
             */
            storeRequests.remove(memoryLocation);
            
            /*
             * Do a deep copy on the data to prevent changes due to
             * shared array references
             */
            memory.store(memoryLocation, data);
            return true;
        }
        
        /*
         * Number of ticks NOT satisfied, decrement,
         * then return false to indicate so
         */
        storeRequests.put(memoryLocation, ticksRemaining - 1);
        return false;
    }
    
}
