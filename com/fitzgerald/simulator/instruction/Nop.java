package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Nop extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -5850254084069555927L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }

    @Override
    public Integer aluOperation(Integer srcData1, Integer srcData2) {
        // Not really applicable, but we have to return something
        return null;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        reservationStation.setSourceData1Ready();
        reservationStation.setSourceData2Ready();
    }

    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {}
    
    @Override
    public String toString() {
        return "NOP";
    }
    
}
