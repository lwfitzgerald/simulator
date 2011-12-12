package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class ALUInstruction extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = -9152250193527886943L;
    
    /**
     * Returns the number of cycles required
     * for this instruction to complete
     * @return Number of cycles required to complete
     */
    public abstract int getALUCyclesRequired();
    
    /**
     * Called by the ALU
     * Describes the operation performed by the ALU
     * for this instruction
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @return Result of operation
     */
    public abstract int aluOperation(Integer srcData1, Integer srcData2);

    /**
     * Update the reservation station for a
     * dstreg, src1reg, src2reg instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    protected void updateReservationStationReg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // dstreg, src1reg, src2reg
        
        updateReservationStationDestination(registerFile, scoreboard, reservationStation);
        updateReservationStationSource1Reg(registerFile, scoreboard, reservationStation);
        updateReservationStationSource2Reg(registerFile, scoreboard, reservationStation);
    }
    
    /**
     * Update the reservation station for a
     * dstreg, src1reg, src2imm instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    protected void updateReservationStationImm(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // dstreg, src1reg, src2imm
        
        updateReservationStationDestination(registerFile, scoreboard, reservationStation);
        updateReservationStationSource1Reg(registerFile, scoreboard, reservationStation);
        updateReservationStationSource2Imm(scoreboard, reservationStation);
    }
    
    /**
     * Update the source 1 register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    private void updateReservationStationSource1Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationSource1Reg(registerFile, scoreboard,
                reservationStation, operand2);
    }
    
    /**
     * Update the source 2 register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    private void updateReservationStationSource2Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        updateReservationStationSource2Reg(registerFile, scoreboard,
                reservationStation, operand3);
    }
    
    /**
     * Update the source 1 immediate data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    private void updateReservationStationSource2Imm(Scoreboard scoreboard,
            ReservationStation reservationStation) {
        
        // Store immediate operand
        if (reservationStation.getSourceData2() == null) {
            // Store in reservation station
            reservationStation.setSourceData2(operand3);
            
            // Set as ready
            reservationStation.setSourceData2Ready();
        }
    }
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (destRegister == operand2) {
            reservationStation.setSourceData1(result);
            reservationStation.setSourceData1Ready();
        }
        
        if (destRegister == operand3) {
            reservationStation.setSourceData2(result);
            reservationStation.setSourceData2Ready();
        }
    }
    
}
