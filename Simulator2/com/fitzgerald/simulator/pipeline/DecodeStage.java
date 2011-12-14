package com.fitzgerald.simulator.pipeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ReservationStation;

public class DecodeStage extends PipelineStage {

    protected Queue<PipelineBuffer> buffers = new LinkedList<PipelineBuffer>();
    
    /**
     * Create a new decode stage
     * @param processor Processor reference
     */
    public DecodeStage(Processor processor) {
        super(processor);
    }
    
    public void step() {
        Iterator<PipelineBuffer> itr = buffers.iterator();
        
        while (itr.hasNext()) {
            ReservationStation rs = processor.getFreeReservationStation();
            PipelineBuffer buffer = itr.next();
            
            if (rs != null) {
                // Free reservation station
                rs.issueInstruction(buffer.instruction, buffer.branchAddr,
                        buffer.speculative);
                
                // Issued so remove from buffer
                itr.remove();
            }
        }
    }
    
    /**
     * Get the filled buffers of this pipeline stage
     * @return filled Buffers
     */
    public Queue<PipelineBuffer> getBuffers() {
        return buffers;
    }
    
    /**
     * Returns whether this pipeline stage is empty
     * @return True if this stage is empty
     */
    public boolean isEmpty() {
        return buffers.isEmpty();
    }

    /**
     * Mark speculative instructions
     * as no longer speculative
     */
    public void approveSpeculative() {
        for (PipelineBuffer buffer : buffers) {
            buffer.speculative = false;
        }
    }
    
    /**
     * Remove speculative instructions
     */
    public void flushSpeculative() {
        Iterator<PipelineBuffer> itr = buffers.iterator();
        
        while (itr.hasNext()) {
            if (itr.next().speculative) {
                itr.remove();
            }
        }
    }
    
    @Override
    public void flush() {
        buffers.clear();
    }
    
    public String toString() {
        if (buffers.isEmpty()) {
            return "EMPTY";
        }
        
        StringBuffer strBuffer = new StringBuffer();
        
        Iterator<PipelineBuffer> itr = buffers.iterator();
        
        for (int i=0; i < Processor.FETCH_DECODE_WIDTH; i++) {
            if (itr.hasNext()) {
                PipelineBuffer buffer = itr.next();
                strBuffer.append(i + ": [[" + buffer.instruction + "]," + (buffer.speculative ? "SP" : "NONSP") + "]\n");
            } else {
                strBuffer.append(i + ": [EMPTY]\n");
            }
        }
        
        return strBuffer.substring(0, strBuffer.length()-1);
    }
    
}
