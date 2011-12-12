package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Add extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -2371581904490721607L;
    
    @Override
    public int getALUCyclesRequired() {
        return 1;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialSetupReg(registerFile, scoreboard, robEntry, reservationStation);
    }
    
    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 + srcData2;
    }
    
    @Override
    public String toString() {
        return "ADD r" + operand1 +
                ", r" + operand2 +
                ", r" + operand3;
    }

}
