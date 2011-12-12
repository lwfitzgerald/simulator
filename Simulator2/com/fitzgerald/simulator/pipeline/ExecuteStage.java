package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.executionstage.ALU;
import com.fitzgerald.simulator.executionstage.BranchUnit;
import com.fitzgerald.simulator.executionstage.LoadStoreUnit;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.ReservationStation;

public class ExecuteStage extends PipelineStage {

    /**
     * Create a new Execute stage
     * @param processor Processor reference
     */
    public ExecuteStage(Processor processor) {
        super(processor);
    }
    
    public void step() {
        ALU[] alus = processor.getALUs();
        LoadStoreUnit[] lsUnits = processor.getLoadStoreUnits();
        BranchUnit[] branchUnits = processor.getBranchUnits();
        
        ReservationStation rs;
        
        for (ALU alu : alus) {
            if (alu.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = processor.getReadyReservationStation(InstructionType.ALU);
                
                if (rs != null) {
                    // Dispatch to ALU
                    alu.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                }
            } else {
                alu.continueExecuting();
            }
        }
        
        for (LoadStoreUnit lsUnit : lsUnits) {
            if (lsUnit.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = processor.getReadyReservationStation(InstructionType.LOADSTORE);
                
                if (rs != null) {
                    // Dispatch to load store unit
                    lsUnit.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                }
            } else {
                lsUnit.continueExecuting();
            }
        }
        
        for (BranchUnit branchUnit : branchUnits) {
            if (branchUnit.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = processor.getReadyReservationStation(InstructionType.BRANCH);
                
                if (rs != null) {
                    // Dispatch to branch unit
                    branchUnit.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                }
            } else {
                branchUnit.continueExecuting();
            }
        }
    }

    @Override
    public void flush() {}
    
}
