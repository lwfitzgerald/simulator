package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public class Mul extends ALUInstruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -2426553606105936083L;

    @Override
    public int getALUCyclesRequired() {
        // Multiplication requires 2 cycles
        return 2;
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
        
        if (result == null) {
            // More cycles required
            return false;
        }
        
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(result);
        return true;
    }*/
    
    @Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 * srcData2;
    }

    @Override
    public String toString() {
        return "MUL r" + operand1 +
               ", r" + operand2 +
               ", r" + operand3;
    }

}
