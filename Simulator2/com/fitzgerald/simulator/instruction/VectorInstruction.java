package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class VectorInstruction extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -6831017045048889630L;

    public static final int NUM_CYCLES_REQUIRED = 2;
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, getSrc(1));
        initialFetchSource2Reg(registerFile, scoreboard, reservationStation, getSrc(2));
        initialFetchSource3Reg(registerFile, scoreboard, reservationStation, getSrc(3));
        initialFetchSource4Reg(registerFile, scoreboard, reservationStation, getSrc(4));
        
        initialClaimDestination(registerFile, scoreboard, robEntry, reservationStation);
    }

    protected void initialFetchSource3Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation, int registerNum) {
        
        // Attempt to fetch source data 3
        if (reservationStation.getVectorSrcData3() == null
                && scoreboard.isAvailable(registerNum)) {
            
            // Get value
            int sourceData3 = registerFile.getRegister(registerNum).getValue();
            
            // Store in reservation station
            reservationStation.setVectorSrcData3(sourceData3);
            
            // Set as ready
            reservationStation.setVectorSrcData3Ready();
        }
    }
    
    protected void initialFetchSource4Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation, int registerNum) {
        
        // Attempt to fetch source data 4
        if (reservationStation.getVectorSrcData4() == null
                && scoreboard.isAvailable(registerNum)) {
            
            // Get value
            int sourceData4 = registerFile.getRegister(registerNum).getValue();
            
            // Store in reservation station
            reservationStation.setVectorSrcData4(sourceData4);
            
            // Set as ready
            reservationStation.setVectorSrcData4Ready();
        }
    }
    
    /**
     * Called by the Vector unit
     * Describes the operation performed by the vector
     * unit for this instruction
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @param srcData3 Source data 3 or null if N/A
     * @param srcData4 Source data 4 or null if N/A
     * @return Result of operation
     */
    public abstract Integer vectorOperation(Integer srcData1, Integer srcData2,
            Integer srcData3, Integer srcData4);
    
    /**
     * Returns the number of cycles required
     * for this instruction to complete
     * @return Number of cycles required to complete
     */
    public abstract int getVectorCyclesRequired();
    
    /**
     * Get the actual register number from the
     * encoded version
     * @param srcNum Source number wanted
     * @return Actual register number
     */
    protected int getSrc(int srcNum) {
        switch (srcNum) {
        case 1:
            // Mask off top 16 bits, and shift down
            return (operand2 & 0xFFFF0000) >>> 16;
        
        case 2:
            // Mask off bottom 16 bits
            return operand2 & 0x0000FFFF;
            
        case 3:
            // Mask off top 16 bits, and shift down
            return (operand3 & 0xFFFF0000) >>> 16;
        
        case 4:
            // Mask off bottom 16 bits
            return operand3 & 0x0000FFFF;
        }
        
        return -1;
    }
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == getSrc(1)) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
        
        if (destRegister == getSrc(2)) {
            reservationStation.setSourceData2(result);
            reservationStation.setSourceData2Ready();
        }
        
        if (destRegister == getSrc(3)) {
            reservationStation.setVectorSrcData3(result);
            reservationStation.setVectorSrcData3Ready();
        }
        
        if (destRegister == getSrc(4)) {
            reservationStation.setVectorSrcData4(result);
            reservationStation.setVectorSrcData4Ready();
        }
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName().toUpperCase() + " r" + getSrc(1) +
               ", r" + getSrc(2) +
               ", r" + getSrc(3) +
               ", r" + getSrc(4);
    }

}
