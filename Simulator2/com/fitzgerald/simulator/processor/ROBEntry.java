package com.fitzgerald.simulator.processor;

import java.util.Iterator;

import com.fitzgerald.simulator.instruction.BranchInstruction;
import com.fitzgerald.simulator.instruction.Instruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.instruction.LoadStoreInstruction;
import com.fitzgerald.simulator.instruction.Print;

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
     * If this instruction accesses memory
     * (read or write) it is to this address
     */
    protected Integer memAddr;
    
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
        this.memAddr = null;
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
     * Get the state of this entry
     * @return State of entry
     */
    public EntryState getState() {
        return state;
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
     * Returns whether the instruction is ready
     * to retire
     * @return True if read
     */
    public boolean isReadyToRetire() {
        return state == EntryState.FINISHED && !speculative;
    }
    
    /**
     * Return if the instruction is ready to be dispatched
     * from the reservation station
     * @return True if ready to be dispatched
     */
    public boolean isReadyForDispatch() {
        if (reservationStation != null) {
            return reservationStation.isReadyForDispatch();
        }
        
        return true;
    }
    
    /**
     * Get the instruction in this entry
     * @return Instruction
     */
    public Instruction getInstruction() {
        return instruction;
    }
    
    /**
     * Get the RS if any storing the
     * data of this entry
     * @return Reservation station or null
     * if not in one
     */
    public ReservationStation getRS() {
        return reservationStation;
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
     * Set the mem address for the instruction
     * in this entry
     * @param addr Address
     */
    public void setMemAddr(int addr) {
        this.memAddr = addr;
    }
    
    /**
     * Get the mem address for the instruction
     * in this entry
     * @return Mem address
     */
    public int getMemAddr() {
        if (memAddr == null) {
            LoadStoreInstruction lsInstruction = (LoadStoreInstruction) instruction;
            Integer srcData1 = reservationStation.getSourceData1();
            Integer srcData2 = reservationStation.getSourceData2();
            Integer dest = reservationStation.getDestination();
            
            memAddr = lsInstruction.getLSAddress(srcData1, srcData2, dest); 
        }
        
        return memAddr;
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
     * waiting in the reservation stations
     * @param processor Processor reference
     */
    public void handleFinish(Processor processor) {
        // Mark as finished
        state = EntryState.FINISHED;
        
        forwardResult(processor);
    }
    
    /**
     * Update speculation state and update
     * scoreboard
     * 
     * @param processor Processor reference
     * @param itr ROB iterator
     */
    public void handleRetire(Processor processor, Iterator<ROBEntry> itr) {
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        Scoreboard scoreboard = processor.getScoreboard();
        
        if (instruction.getType() == InstructionType.BRANCH) {
            // Branch so deal with speculation if necessary
            
            BranchInstruction branchInstruction = (BranchInstruction) instruction;
            
            if (!branchInstruction.isUnconditional()) {
                // Get the address to branch to if incorrect
                int failAddr = processor.getSpeculateFailAddress();
                
                // Mark as no longer speculating
                processor.stopSpeculating();
                
                // Remove this entry from the ROB
                itr.remove();
                
                if (result == 1) {
                    // Correct
                    
                    // Increment correct prediction count
                    processor.incrementCorrectDirectionCount();
                    
                    // Approve all speculative instructions
                    processor.approveSpeculative();
                } else {
                    // Incorrect
                    
                    recoverWrongDirection(processor, reorderBuffer, failAddr);
                }
            } else {
                // Unconditional so we've already branched
                
                // Remove this entry from the ROB
                itr.remove();
            }
        } else {
            // Non branch so sort out scoreboard
            
            boolean afterThisEntry = false;
            boolean laterWrite = false;
            
            for (ROBEntry entry : reorderBuffer) {
                if (!afterThisEntry) {
                    if (entry == this) {
                        afterThisEntry = true;
                        continue;
                    }
                } else {
                    if (entry.state == EntryState.ISSUED) {
                        if (entry.destRegister == this.destRegister) {
                            laterWrite = true;
                            break;
                        }
                    }
                }
            }
            
            if (!laterWrite) {
                if (destRegister != null) {
                    // Unset scoreboard bit as no later writes in reservation stations
                    scoreboard.setAvailablity(this.destRegister, true);
                }
            }
            
            itr.remove();
        }
    }
    
    /**
     * Forwards the result of this instruction
     * to applicable later instructions
     * @param processor Processor reference
     */
    public void forwardResult(Processor processor) {
        ReorderBuffer reorderBuffer = processor.getReorderBuffer();
        
        if (instruction.getType() != InstructionType.BRANCH) {
            // Only forward if there is a result
            if (destRegister != null) {
                boolean afterThisEntry = false;
                
                for (ROBEntry entry : reorderBuffer) {
                    if (!afterThisEntry) {
                        if (entry == this) {
                            afterThisEntry = true;
                            continue;
                        }
                    } else {
                        /*
                         * Only forward to instruction which
                         * aren't already executing/finished
                         */
                        if (entry.state == EntryState.ISSUED) {
                            // Update reservation station for the entry!
                            entry.instruction.forwardResult(result, destRegister,
                                    entry.reservationStation);
                            
                            if (entry.destRegister == this.destRegister) {
                                /*
                                 * We've reached the next write to the register
                                 * so cannot forward to any more instructions
                                 */
                                entry.reservationStation.setDestinationReady();
                                
                                return;
                            }
                        }
                    }
                }
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
        // Increment counter
        processor.incrementIncorrectDirectionCount();
        
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
        if (instruction instanceof Print) {
            System.out.println("Program print: " + result);
        }
        
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
