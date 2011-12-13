package com.fitzgerald.simulator.processor;

import java.util.HashSet;

public class MemoryScoreboard {
    
    protected HashSet<Integer> reads = new HashSet<Integer>();
    
    protected HashSet<Integer> writes = new HashSet<Integer>();
    
    /**
     * Returns if an address is available to read
     * @param addr Address
     * @return True if ok to read
     */
    public boolean isAvailableToRead(int addr) {
        if (writes.contains(addr)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Returns if an address is available to write
     * @param addr Address
     * @return True if ok to write
     */
    public boolean isAvailableToWrite(int addr) {
        if (reads.contains(addr) || writes.contains(addr)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Change the read status of a memory address
     * @param addr Address
     * @param status True to mark as reading, false
     * to mark as not reading
     */
    public void changeReadStatus(int addr, boolean status) {
        if (status) {
            reads.add(addr);
        } else {
            reads.remove(addr);
        }
    }
    
    /**
     * Change the write status of a memory address
     * @param addr Address
     * @param status True to mark as writing, false
     * to mark as not writing
     */
    public void changeWriteStatus(int addr, boolean status) {
        if (status) {
            writes.add(addr);
        } else {
            writes.remove(addr);
        }
    }
    
    /**
     * Mark all memory addresses as free
     * for read and write
     */
    public void flush() {
        reads.clear();
        writes.clear();
    }
    
}
