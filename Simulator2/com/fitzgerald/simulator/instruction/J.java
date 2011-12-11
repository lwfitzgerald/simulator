package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class J extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1871790764749534500L;
    
    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationImm(registerFile, scoreboard, reservationStation);
        
        // TODO: We should be storing current PC here?
    }

    @Override
    public boolean branchCondition(Integer srcData1, Integer srcData2) {
        // Unconditional jump
        return true;
    }

    @Override
    public int branchCalculation(Integer srcData1, Integer srcData2) {
        // TODO: This is almost definitely wrong
        
        // Calculate the correct PC due to pipelining
        int correctPC = srcData1 - 4;
        int offset = srcData2;
        
        // Apply the offset
        return correctPC + offset;
    }
    
    @Override
    public boolean isUnconditional() {
        return true;
    }

    @Override
    public int getBranchAddress(int currentPC) {
        return currentPC + operand1;
    }
    
    @Override
    public String toString() {
        return "J " + operand1;
    }

}
