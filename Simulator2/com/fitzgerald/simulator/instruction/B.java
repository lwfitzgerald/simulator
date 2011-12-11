package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class B extends BranchInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 3946843299323568039L;
    
    @Override
    public void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // dstimm
        
        updateReservationStationImm(registerFile, scoreboard, reservationStation);
    }

    /*@Override
    public boolean execute(Processor processor, RegisterFile registerFile,
            ALU alu, BranchUnit branchUnit, MemoryController memoryController, ExecuteStage executeStage) {
        
        registerFile.getRegister(Processor.PC_REG).setNextValue(operand1);
        
        return true;
    }*/

    @Override
    public boolean branchCondition(Integer srcData1, Integer srcData2) {
        // Unconditional branch
        return true;
    }

    @Override
    public int branchCalculation(Integer srcData1, Integer srcData2) {
        return srcData1;
    }
    
    @Override
    public boolean isUnconditional() {
        return true;
    }
    
    @Override
    public int getBranchAddress(int currentPC) {
        return operand1;
    }
    
    @Override
    public String toString() {
        return "B " + operand1;
    }

}
