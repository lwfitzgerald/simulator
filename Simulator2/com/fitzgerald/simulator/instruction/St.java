package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.Memory;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class St extends LoadStoreInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7554310606728502465L;

    @Override
    public LoadStoreType getLSType() {
        return LoadStoreType.STORE;
    }

    @Override
    public int getLSAddress(Integer srcData1, Integer srcData2, Integer dest) {
        return srcData2 + dest;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, operand1);
        initialFetchSource2Reg(registerFile, scoreboard, reservationStation, operand2);
        reservationStation.setDestinationReady();
    }
    
    @Override
    public Integer memoryOperation(Integer srcData1, Integer srcData2,
            Integer dest, Memory memory) {
        
        memory.store(getLSAddress(srcData1, srcData2, dest), srcData1);
        
        return null;
    }

    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == operand1) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
        
        if (destRegister == operand2) {
            reservationStation.setSourceData2(result);
            reservationStation.setSourceData2Ready();
        }
    }
    
    @Override
    public String toString() {
        return "ST r" + operand1 + 
               ", r" + operand2 +
               ", " + operand3;
    }

}
