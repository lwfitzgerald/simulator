package com.fitzgerald.simulator.processor;

public class Scoreboard {

    /**
     * Availability array
     */
    protected boolean[] availabilityArray = new boolean[Processor.NUM_REGISTERS];
    
    /**
     * Create a scoreboard
     */
    public Scoreboard() {
        // Initially mark all registers as available
        flush();
    }
    
    /**
     * Return if a register is available or not
     * @param registerNo Register number
     * @return True if register is available
     */
    public boolean isAvailable(int registerNo) {
        return availabilityArray[registerNo];
    }
    
    /**
     * Set the availability of a register
     * @param registerNo Register number
     * @param availability Availability to set
     */
    public void setAvailablity(int registerNo, boolean availability) {
        availabilityArray[registerNo] = availability;
    }
    
    /**
     * Mark all registers as available
     */
    public void flush() {
        for (int i=0; i < Processor.NUM_REGISTERS; i++) {
            availabilityArray[i] = true;
        }
    }
    
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        
        for (int i=0; i < Processor.NUM_REGISTERS; i++) {
            buffer.append("r" + i + ": " + (availabilityArray[i] ? "Available" : "Unavailable") + "\n");
        }
        
        return buffer.toString();
    }
    
}
