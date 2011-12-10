package com.fitzgerald.simulator.processor;

import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.instruction.Instruction;

public class ReorderBuffer {
    
    /**
     * Queue holding entries
     */
    protected Queue<ROBEntry> buffer = new LinkedList<ROBEntry>();
    
    /**
     * Add an entry for a new instruction
     */
    public void addEntry(Instruction instruction) {
        ROBEntry newEntry = new ROBEntry(instruction);
        buffer.add(newEntry);
    }
    
    /**
     * Attempts to retire an entry
     * @return A ROBEntry or null if none can be retired
     */
    public ROBEntry attemptRetire() {
        if (buffer.peek().isFinished()) {
            // Front of queue finished so remove
            return buffer.remove();
        }
        
        return null;
    }
}