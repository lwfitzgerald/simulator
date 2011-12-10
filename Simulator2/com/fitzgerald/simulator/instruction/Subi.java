package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Subi extends Sub {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7082676089212708779L;

    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationImm(registerFile, scoreboard, reservationStation);
    }

}
