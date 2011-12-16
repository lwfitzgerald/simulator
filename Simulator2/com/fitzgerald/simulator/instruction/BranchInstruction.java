package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.ROBEntry;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;
import com.fitzgerald.simulator.processor.Scoreboard;

public abstract class BranchInstruction extends Instruction {

    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1686053093849707573L;
    
    public static final int NUM_CYCLES_REQUIRED = 1;
    
    /**
     * Called by the branch unit
     * Checks whether the branch should be taken
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData1 Source data 2 or null if N/A
     * @return True if branch should be taken, false
     * otherwise
     */
    public abstract boolean branchCondition(Integer srcData1, Integer srcData2);
    
    /**
     * Return if the branch is unconditional
     * @return True if unconditional
     */
    public abstract boolean isUnconditional();
    
    /**
     * Get the branch address of this branch
     * @param currentPC Current PC value
     * @return Branch address of this branch
     */
    public abstract int getBranchAddress(int currentPC);
    
    @Override
    public void initialSetup(RegisterFile registerFile,
            Scoreboard scoreboard, ROBEntry robEntry, Integer branchAddr, ReservationStation reservationStation) {
        
        if (isUnconditional()) {
            // dstimm
            initialSetupImm(registerFile, scoreboard, branchAddr, reservationStation);
        } else {
            initialSetupReg(registerFile, scoreboard, branchAddr, reservationStation);
        }
    }
    
    /**
     * Initial setup for a reservation station for a
     * src1reg, src2reg, dstimm instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param branchAddr Calculated branch address
     * @param reservationStation Reservation station reference
     */
    protected void initialSetupReg(RegisterFile registerFile,
            Scoreboard scoreboard, Integer branchAddr,
            ReservationStation reservationStation) {
        
        // src1reg, src2reg, dstimm
        
        initialFetchSource1Reg(registerFile, scoreboard, reservationStation, operand1);
        initialFetchSource2Reg(registerFile, scoreboard, reservationStation, operand2);
        initialSetDestination(registerFile, scoreboard, branchAddr, reservationStation);
    }
    
    /**
     * Initial setup for a reservation station for a
     * dstimm instruction
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param branchAddr Calculated branch address
     * @param reservationStation Reservation station reference
     */
    protected void initialSetupImm(RegisterFile registerFile,
            Scoreboard scoreboard, Integer branchAddr,
            ReservationStation reservationStation) {
        
        // dstimm
        
        reservationStation.setSourceData1Ready();
        reservationStation.setSourceData2Ready();
        initialSetDestination(registerFile, scoreboard, branchAddr,
                reservationStation);
    }
    
    /**
     * Set the destination of the branch in the
     * reservation station
     * @param registerFile Register file reference
     * @param scoreboard Scoreboard reference
     * @param branchAddr Calculated branch address
     */
    private void initialSetDestination(RegisterFile registerFile,
            Scoreboard scoreboard, Integer branchAddr,
            ReservationStation reservationStation) {
        
        /*
         * Don't set ROB entry destRegister as it's a
         * branch address not a register!
         */
        
        if (reservationStation.getDestination() == null) {
            // Store in reservation station
            reservationStation.setDestination(branchAddr);
        }
    }
    
    @Override
    public void forwardResult(Integer result, Integer destRegister,
            ReservationStation reservationStation) {
        
        if (isUnconditional()) {
            // No register reads for unconditional branches
            return;
        }
        
        // src1reg, src2reg, dstimm
        
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
        if (isUnconditional()) {
            return this.getClass().getSimpleName().toUpperCase() + " " + operand1;
        }
        
        return this.getClass().getSimpleName().toUpperCase() + " r" + operand1 +
                ", r"  + operand2 + 
                ", " + operand3;
    }
    
}
