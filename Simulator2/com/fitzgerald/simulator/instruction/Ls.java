package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Ls extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -6295258259288146573L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }

    @Override
    public Integer aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 << srcData2;
    }

    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialSetupReg(registerFile, scoreboard, robEntry, reservationStation);
    }

    @Override
    public String toString() {
        return "LS r" + operand1 +
               ", r" + operand2 +
               ", r" + operand3;
    }

}
