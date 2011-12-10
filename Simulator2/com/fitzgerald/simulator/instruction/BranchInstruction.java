package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class BranchInstruction extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1686053093849707573L;
    
    /**
     * Called by the branch unit
     * Checks whether the branch should be taken
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData1 Source data 2 or null if N/A
     * @return True if branch should be taken, false
     * otherwise
     */
    public abstract boolean branchCondition(Integer srcData1, Integer srcData2);
    
    /**
     * Called by the branch unit
     * Returns the calculated address to branch to
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData1 Source data 2 or null if N/A
     * @return Address to branch to
     */
    public abstract int branchCalculation(Integer srcData1, Integer srcData2);
    
    /**
     * Update the reservation station for a
     * src1reg, src2reg, dstimm instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    protected void updateReservationStationReg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // src1reg, src2reg, dstimm
        
        updateReservationStationSource1Reg(registerFile, scoreboard, reservationStation, operand1);
        updateReservationStationSource2Reg(registerFile, scoreboard, reservationStation, operand2);
        updateReservationStationDestination(registerFile, scoreboard, reservationStation, operand3);
    }
    
    /**
     * Update the reservation station for a
     * dstimm instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    protected void updateReservationStationImm(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // dstimm
        
        updateReservationStationDestination(registerFile, scoreboard, reservationStation, operand1);
        reservationStation.setSourceData1Ready();
        reservationStation.setSourceData2Ready();
    }
    
    /**
     * Update the destination register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     * @param immediateValue Immediate value to store
     */
    private void updateReservationStationDestination(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation,
            int immediateValue) {
        
        // Attempt to claim destination register
        if (reservationStation.getDestination() == null) {
            // Store in reservation station
            reservationStation.setDestination(immediateValue);
            
            // Set as ready
            reservationStation.setDestinationReady();
        }
    }
}
