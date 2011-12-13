package com.fitzgerald.simulator.processor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;

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
        
        if (instruction.getType() == InstructionType.BRANCH) {
            /*
             * Only one level of speculating so branches are
             * never speculative
             */
            speculating = false;
        }
        
        ROBEntry newEntry = new ROBEntry(instruction, reservationStation, speculating);
        buffer.add(newEntry);
        
        return newEntry;
    }
    
    /**
     * Attempts to retire an entry
     * @return A ROBEntry or null if none can be retired
     */
    public ROBEntry attemptRetire() {
        ROBEntry entry = buffer.peek();
        
        if (entry != null) {
            if (entry.isFinished() && !entry.isSpeculative()) {
                // Front of queue finished and non-speculative,
                // so remove
                return buffer.remove();
            }
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
        
        for (ROBEntry entry : buffer) {
            strBuffer.append(entry);
        }
        
        return strBuffer.toString();
    }
    
}