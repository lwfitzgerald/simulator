package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Sub extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -12971856879705209L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }
    
    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationReg(registerFile, scoreboard, reservationStation);
    }

    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 - srcData2;
    }

    @Override
    public String toString() {
        return "SUB r" + operand1 + 
               ", r" + operand2 + 
               ", r" + operand3;
    }

}
