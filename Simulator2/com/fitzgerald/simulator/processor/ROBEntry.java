package com.fitzgerald.simulator.processor;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;

public class ROBEntry {
    
    /**
     * Enum describing the state of the instruction
     */
    public enum EntryState { ISSUED, EXECUTING, FINISHED };
    
    /**
     * Instruction entry is for
     */
    protected Instruction instruction;
    
    /**
     * Value to store in register / memory upon retiring
     */
    protected Integer result;
    
    /**
     * Destination register for the result of
     * this instruction (if applicable)
     */
    protected Integer destRegister;
    
    /**
     * Reference to reservation station holding
     * instruction before executing state
     */
    protected ReservationStation reservationStation;
    
    /**
     * State of the instruction
     */
    protected EntryState state;
    
    /**
     * Represents if this instruction is speculative
     */
    protected boolean speculative;
    
    /**
     * Creates a ROBEntry
     * @param instruction Instruction entry is for
     * @param reservationStation Reservation station
     * containing issued instruction
     */
    public ROBEntry(Instruction instruction,
            ReservationStation reservationStation) {
        
        this.instruction = instruction;
        this.state = EntryState.ISSUED;
        this.result = null;
        this.destRegister = null;
        this.reservationStation = reservationStation;
    }
    
    /**
     * Creates a ROBEntry with given speculativity
     * @param instruction Instruction entry is for
     * @param reservationStation Reservation station
     * containing issued instruction
     * @param speculative Whether instruction execution
     * is speculative or not
     */
    public ROBEntry(Instruction instruction,
            ReservationStation reservationStation, boolean speculative) {

        this(instruction, reservationStation);
        this.speculative = speculative;
    }
    
    /**
     * Set the state of this entry
     * @param state State to set
     */
    public void setState(EntryState state) {
        this.state = state;
        
        /*
         * For safety mostly, unset reservation
         * station reference as we're changing
         * to executing or finished
         */
        reservationStation = null;
    }
    
    /**
     * Returns whether the instruction has finished
     * @return True if finished
     */
    public boolean isFinished() {
        return state == EntryState.FINISHED ? true : false;
    }
    
    /**
     * Returns whether execution of this instruction
     * is speculative
     * @return True if speculative
     */
    public boolean isSpeculative() {
        return speculative;
    }
    
    /**
     * Set whether execution of this instruction is
     * speculative or not
     * @param speculative True if speculative
     */
    public void setSpeculativity(boolean speculative) {
        this.speculative = speculative;
    }
    
    /**
     * Set the destination register for this entry
     * @param registerNum Register number
     */
    public void setDestRegister(int registerNum) {
        this.destRegister = registerNum;
    }
    
    /**
     * Set the result data for this ROB entry
     * @param result Result data to store
     */
    public void setResult(int result) {
        this.result = result;
    }
    
    /**
     * Forward the result in this entry to instructions
     * waiting in the reservation stations and update
     * scoreboard bits
     * @param processor Processor reference
     */
    public void handleFinish(Processor processor) {
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        
        if (instruction.getType() == InstructionType.BRANCH) {
            BranchInstruction branchInstruction = (BranchInstruction) instruction;
            
            if (!branchInstruction.isUnconditional()) {
                // Get the address to branch to if incorrect
                int failAddr = processor.getSpeculateFailAddress();
                
                // Mark as no longer speculating
                processor.stopSpeculating();
                
                if (result == 1) {
                    // Correct
                    
                    // Approve all speculative instructions
                    processor.approveSpeculative();
                } else {
                    // Incorrect
                    
                    recoverWrongDirection(processor, reorderBuffer, failAddr);
                }
            }
        } else {
            Scoreboard scoreboard = processor.getScoreboard();
            
            // Only forward if there is a result
            if (result != null) {
                for (ROBEntry entry : reorderBuffer) {
                    // Do not forward to own entry
                    if (entry != this) {
                        /*
                         * Only forward to instruction which
                         * aren't already executing/finished
                         */
                        if (entry.state == EntryState.ISSUED) {
                            if (entry.destRegister == this.destRegister) {
                                /*
                                 * We've reached the next write to the register
                                 * so cannot forward to any more instructions
                                 */
                                entry.reservationStation.setDestinationReady();
                                
                                // Return without unsetting scoreboard bit
                                return;
                            }
                            
                            // Update reservation station for the entry!
                            entry.instruction.forwardResult(result, destRegister,
                                    entry.reservationStation);
                        }
                    }
                }
                
                // Unset scoreboard bit as no later writes in reservation stations
                scoreboard.setAvailablity(this.destRegister, true);
            }
        }
    }
    
    /**
     * Recover from going the wrong direction on
     * a branch
     * @param processor Processor reference
     * @param reorderBuffer Reorder buffer reference
     * @param failAddr Correct instruction address
     */
    protected void recoverWrongDirection(Processor processor, ReorderBuffer reorderBuffer, int failAddr) {
        // Remove speculative instructions
        reorderBuffer.removeSpeculative();
        
        // Update PC
        RegisterFile registerFile = processor.getRegisterFile();
        registerFile.getRegister(Processor.PC_REG).setValue(failAddr);
        
        // Flush pipeline
        processor.flushPipeline();
    }
    
    
    /**
     * Perform writing of result to register
     */
    public void writeBack(RegisterFile registerFile) {
        if (destRegister != null) {
            registerFile.getRegister(destRegister).setValue(result);
        }
    }
    
    /**
     * Abort execution of this instruction
     * 
     * Called if aborting speculative
     * instruction
     */
    public void abort() {
        if (reservationStation != null) {
            reservationStation.flush();
        }
    }
    
    public String toString() {
        return "[[" + instruction + "], " + state + ", " + (speculative ? "SP" : "NONSP") + "]";
    }
    
}
