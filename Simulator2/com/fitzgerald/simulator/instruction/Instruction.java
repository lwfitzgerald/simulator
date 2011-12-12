package com.fitzgerald.simulator.instruction;

import java.io.Serializable;

import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class Instruction implements Serializable {
    
    /**
     * Enum representing type of instruction
     */
    public enum InstructionType { ALU, BRANCH, LOADSTORE };
    
    /**
     * Serialising ID
     */
    private static final long serialVersionUID = 570193615900542137L;
    
    /**
     * Operand 1
     */
    protected int operand1;
    
    /**
     * Operand 2
     */
    protected int operand2;
    
    /**
     * Operand 3
     */
    protected int operand3;
    
    /**
     * Attempt to fetch required register values
     * and update scoreboard
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    public abstract void updateReservationStation(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation);
    
    /**
     * Update the source 1 register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     * @param registerNum Register number
     */
    protected void updateReservationStationSource1Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation, int registerNum) {
        
        // Attempt to fetch source data 1
        if (reservationStation.getSourceData1() == null
                && scoreboard.isAvailable(registerNum)) {
            
            // Get value
            int sourceData1 = registerFile.getRegister(registerNum).getCurrentValue();
            
            // Store in reservation station
            reservationStation.setSourceData1(sourceData1);
            
            // Set as ready
            reservationStation.setSourceData1Ready();
        }
    }
    
    /**
     * Update the source 2 register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     * @param registerNum Register number
     */
    protected void updateReservationStationSource2Reg(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation, int registerNum) {
        
        // Attempt to fetch source data 1
        if (reservationStation.getSourceData2() == null
                && scoreboard.isAvailable(registerNum)) {
            
            // Get value
            int sourceData2 = registerFile.getRegister(registerNum).getCurrentValue();
            
            // Store in reservation station
            reservationStation.setSourceData2(sourceData2);
            
            // Set as ready
            reservationStation.setSourceData2Ready();
        }
    }
    
    /**
     * Update the destination register data for the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param reservationStation Reservation station reference
     */
    protected void updateReservationStationDestination(RegisterFile registerFile,
            Scoreboard scoreboard, ReservationStation reservationStation) {
        
        // Attempt to claim destination register
        if (reservationStation.getDestination() == null
                && scoreboard.isAvailable(operand1)) {

            // Claim in scoreboard
            scoreboard.setAvailablity(operand1, false);
            
            // Set as ready
            reservationStation.setDestinationReady();
        }
    }
    
    /**
     * Forward the result of an instruction to an
     * instruction of this type in the given reservation
     * station
     * @param result Result to forward
     * @param destRegister Destination register
     * @param reservationStation Reservation station holding
     * this instruction
     */
    public abstract void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation);
    
    /**
     * Set the value of an operand for this instruction
     * @param operandNo Operand to set
     * @param value Value to set operand to
     */
    public void setOperand(int operandNo, int value) {
        switch (operandNo) {
        case 1:
            operand1 = value;
            break;
        case 2:
            operand2 = value;
            break;
        case 3:
            operand3 = value;
        }
    }
    
    /**
     * Get the type of the instruction
     * @return Enum representing type of instruction
     */
    public InstructionType getType() {
        if (this instanceof ALUInstruction) {
            return InstructionType.ALU;
        } else if (this instanceof LoadStoreInstruction) {
            return InstructionType.LOADSTORE;
        } else if (this instanceof BranchInstruction) {
            return InstructionType.BRANCH;
        }
        
        return null;
    }
    
    /**
     * Returns a string representation of the instruction
     */
    public abstract String toString();
}
