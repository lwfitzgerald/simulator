package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.Memory;
import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

/**
 * Perform an indexed load from memory using a register operand
 * as the base address and an immediate operand as the offset
 */
public class Ld extends LoadStoreInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 7256535789909345398L;
    
    @Override
    public LoadStoreType getLSType() {
        return LoadStoreType.LOAD;
    }

    @Override
    public int getLSAddress(Integer srcData1, Integer srcData2, Integer dest) {
        return srcData1 + srcData2;
    }
    
    @Override
    public void initialSetup(RegisterFile registerFile, Scoreboard scoreboard,
            ROBEntry robEntry, Integer branchAddr,
            ReservationStation reservationStation) {
        
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, operand2);
        
        initialSetSource2Imm(scoreboard, reservationStation);
        
        initialClaimDestination(registerFile, scoreboard, robEntry,
                reservationStation);
    }

    @Override
    public Integer memoryOperation(Integer srcData1, Integer srcData2,
            Integer dest, Memory memory) {
        
        return memory.load(getLSAddress(srcData1, srcData2, dest));
    }
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == operand2) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
    }
    
    @Override
    public String toString() {
        return "LD r" + operand1 + 
               ", r" + operand2 +
               ", " + operand3;
    }

}
