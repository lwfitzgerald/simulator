package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Ldr extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -7013572713573792144L;

    @Override
    public int getALUCyclesRequired() {
        return 1;
    }
    
    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialClaimDestination(registerFile, scoreboard, robEntry,
                reservationStation);
        
        // Store immediate operand
        // Store in reservation station
        reservationStation.setSourceData1(operand2);
        
        // Set as ready
        reservationStation.setSourceData1Ready();
        
        // No second src operand
        reservationStation.setSourceData2Ready();
    }

    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        // Source is immediate so nothing to do here
    }
    
    @Override
    public String toString() {
        return "LDR r" + operand1 + 
               ", " + operand2;
    }

}
