package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Addi extends Add {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3348748444887990566L;

    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // dstreg, src1reg, src2imm
        updateReservationStationImm(registerFile, scoreboard, reservationStation);
    }

    @Override
    public String toString() {
        return "ADDI r" + operand1 + 
               ", r" + operand2 + 
               ", " + operand3;
    }

}
