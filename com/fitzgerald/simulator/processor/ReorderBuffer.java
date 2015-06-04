package com.fitzgerald.simulator.processor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.instruction.Instruction;

public class ReorderBuffer implements Iterable<ROBEntry> {
    
    /**
     * Queue holding entries
     */
    protected Queue<ROBEntry> buffer = new LinkedList<ROBEntry>();
    
    /**
     * Add an entry for a new instruction
     * @param instruction Instruction to create entry
     * for
     * @param reservationStation Reservation station
     * holding issued instruction
     * @param speculating Whether currently speculating
     */
    public ROBEntry addEntry(Instruction instruction,
            ReservationStation reservationStation, boolean speculating) {

        ROBEntry newEntry = new ROBEntry(instruction, reservationStation, speculating);
        buffer.add(newEntry);
        
        return newEntry;
    }
    
    /**
     * Mark all speculative entries as
     * non-speculative so they can be
     * retired
     */
    public void approveSpeculative() {
        for (ROBEntry entry : buffer) {
            entry.setSpeculativity(false);
        }
    }
    
    /**
     * Remove all speculative instructions
     */
    public void removeSpeculative() {
        ROBEntry entry;
        
        while ((entry = buffer.peek()) != null) {
            if (entry.isSpeculative()) {
                // Remove from reservation station if necessary
                entry.abort();
                
                // Remove from ROB
                buffer.remove();
            }
        }
    }
    
    /**
     * Return if the ROB is empty or not
     * @return True if empty
     */
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public Iterator<ROBEntry> iterator() {
        return buffer.iterator();
    }
    
    public String toString() {
        StringBuffer strBuffer = new StringBuffer();
        
        if (buffer.isEmpty()) {
            return "EMPTY";
        }
        
        int counter = 1;
        
        for (ROBEntry entry : buffer) {
            strBuffer.append((counter++) + ": " + entry + "\n");
        }
        
        return strBuffer.substring(0, strBuffer.length()-1);
    }
    
}