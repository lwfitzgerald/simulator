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
     * @param reservartionStation Reservation station
     * holding issued instruction
     */
    public ROBEntry addEntry(Instruction instruction,
            ReservationStation reservationStation) {
        
        ROBEntry newEntry = new ROBEntry(instruction, reservationStation);
        buffer.add(newEntry);
        
        return newEntry;
    }
    
    /**
     * Attempts to retire an entry
     * @return A ROBEntry or null if none can be retired
     */
    public ROBEntry attemptRetire() {
        ROBEntry entry = buffer.peek();
        
        if (entry.isFinished() && !entry.isSpeculative()) {
            // Front of queue finished and non-speculative,
            // so remove
            return buffer.remove();
        }
        
        return null;
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
        
        while ((entry = buffer.peek()).isSpeculative()) {
            // Remove from reservation station if necessary
            entry.abort();
            
            // Remove from ROB
            buffer.remove();
        }
    }

    @Override
    public Iterator<ROBEntry> iterator() {
        return buffer.iterator();
    }
}