package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Subi extends Sub {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7082676089212708779L;
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == operand2) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialSetupImm(registerFile, scoreboard, robEntry, reservationStation);
    }

}
