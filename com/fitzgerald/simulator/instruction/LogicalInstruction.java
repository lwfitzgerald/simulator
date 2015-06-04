package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class LogicalInstruction extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 8593570749637314725L;

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
    public String toString() {
        return this.getClass().getSimpleName().toUpperCase() + " r" + operand1 +
                ", r" + operand2 +
                ", r" + operand3;
    }

}
