package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Idiv extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -8837315849558287407L;

    @Override
    public int getALUCyclesRequired() {
        // Integer divide requires 2 cycles
        return 2;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialSetupReg(registerFile, scoreboard, robEntry, reservationStation);
    }

    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 / srcData2;
    }

    @Override
    public String toString() {
        return "IDIV r" + operand1 +
                ", r" + operand2 +
                ", r" + operand3;
    }

}
