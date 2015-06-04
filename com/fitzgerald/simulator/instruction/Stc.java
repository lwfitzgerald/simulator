package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.Memory;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Stc extends LoadStoreInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7387641905906737577L;

    @Override
    public LoadStoreType getLSType() {
        return LoadStoreType.STORE;
    }

    @Override
    public int getLSAddress(Integer srcData1, Integer srcData2, Integer dest) {
        return srcData1 + dest;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, operand1);
        initialSetSource2Imm(scoreboard, reservationStation);
        
        reservationStation.setDestination(operand2);
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
    public Integer memoryOperation(Integer srcData1, Integer srcData2,
            Integer dest, Memory memory) {
        
        memory.store(getLSAddress(srcData1, srcData2, dest), srcData2);
        
        return null;
    }

    @Override
    public String toString() {
        return "STC r" + operand1 +
               ", " + operand2 +
               ", " + operand3;
    }

}
