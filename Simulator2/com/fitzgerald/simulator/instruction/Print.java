package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Print extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -3110719806187502158L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }

    @Override
    public Integer aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1;
    }

    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, operand1);
        reservationStation.setSourceData2Ready();
        reservationStation.setDestinationReady();
    }
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == operand1) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
    }

    @Override
    public String toString() {
        return "PRINT r" + operand1;
    }

}
