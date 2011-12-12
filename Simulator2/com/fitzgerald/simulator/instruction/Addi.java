package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Addi extends Add {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3348748444887990566L;

    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        // dstreg, src1reg, src2imm
        initialSetupImm(registerFile, scoreboard, robEntry, reservationStation);
    }

    @Override
    public String toString() {
        return "ADDI r" + operand1 + 
               ", r" + operand2 + 
               ", " + operand3;
    }

}
