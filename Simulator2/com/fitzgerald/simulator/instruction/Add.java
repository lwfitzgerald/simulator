package com.fitzgerald.simulator.instruction;

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
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationReg(registerFile, scoreboard, reservationStation);
    }
    
    /*@Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        
        byte[] result = alu.performOperation(executeStage);
        
        // Set value as register's next value
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(result);
        
        // Completes in 1 cycle so return true
        return true;
    }*/
    
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
