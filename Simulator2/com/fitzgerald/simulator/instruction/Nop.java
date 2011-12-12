package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Nop extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -5850254084069555927L;

    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        reservationStation.setSourceData1Ready();
        reservationStation.setSourceData2Ready();
        reservationStation.setDestinationReady();
    }

    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {}
    
    @Override
    public String toString() {
        return "NOP";
    }
    
}
