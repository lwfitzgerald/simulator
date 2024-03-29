package com.fitzgerald.simulator.pipeline;

import java.util.LinkedList;
import java.util.Queue;

import com.fitzgerald.simulator.executionstage.ALU;
import com.fitzgerald.simulator.executionstage.BranchUnit;
import com.fitzgerald.simulator.executionstage.LoadStoreUnit;
import com.fitzgerald.simulator.executionstage.VectorUnit;
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
        VectorUnit[] vectorUnits = processor.getVectorUnits();
        
        ReservationStation rs;
        
        // First build a buffer of instructions for each execution unit
        Queue<ReservationStation> aluRSs = new LinkedList<ReservationStation>();
        
        for (int i=0; i < Processor.numALUs; i++) {
            if ((rs = processor.getReadyReservationStation(InstructionType.ALU, aluRSs)) != null) {
                aluRSs.add(rs);
            } else {
                break;
            }
        }
        
        Queue<ReservationStation> lsRSs = new LinkedList<ReservationStation>();
        
        for (int i=0; i < Processor.numLoadStoreUnits; i++) {
            if ((rs = processor.getReadyReservationStation(InstructionType.LOADSTORE, lsRSs)) != null) {
                lsRSs.add(rs);
            } else {
                break;
            }
        }
        
        Queue<ReservationStation> branchRSs = new LinkedList<ReservationStation>();
        
        for (int i=0; i < Processor.numBranchUnits; i++) {
            if ((rs = processor.getReadyReservationStation(InstructionType.BRANCH, branchRSs)) != null) {
                branchRSs.add(rs);
            } else {
                break;
            }
        }
        
        Queue<ReservationStation> vectorRSs = new LinkedList<ReservationStation>();
        
        for (int i=0; i < Processor.numVectorUnits; i++) {
            if ((rs = processor.getReadyReservationStation(InstructionType.VECTOR, vectorRSs)) != null) {
                vectorRSs.add(rs);
            } else {
                break;
            }
        }
        
        for (ALU alu : alus) {
            if (alu.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = aluRSs.poll();
                
                if (rs != null) {
                    // Dispatch to ALU
                    alu.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                    
                    // Increment executed counter
                    processor.incrementExecutedCount();
                    
                    // Clear reservation station
                    rs.flush();
                }
            } else {
                alu.continueExecuting();
            }
        }
        
        for (LoadStoreUnit lsUnit : lsUnits) {
            if (lsUnit.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = lsRSs.poll();
                
                if (rs != null) {
                    // Dispatch to load store unit
                    lsUnit.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                    
                    // Increment executed counter
                    processor.incrementExecutedCount();
                    
                    // Clear reservation station
                    rs.flush();
                }
            } else {
                lsUnit.continueExecuting();
            }
        }
        
        for (BranchUnit branchUnit : branchUnits) {
            if (branchUnit.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = branchRSs.poll();
                
                if (rs != null) {
                    // Dispatch to branch unit
                    branchUnit.startExecuting(rs.getInstruction(), rs.getSourceData1(),
                            rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
                    
                    // Increment executed counter
                    processor.incrementExecutedCount();
                    
                    // Clear reservation station
                    rs.flush();
                }
            } else {
                branchUnit.continueExecuting();
            }
        }
        
        for (VectorUnit vectorUnit : vectorUnits) {
            if (vectorUnit.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = vectorRSs.poll();
                
                if (rs != null) {
                    // Dispatch to Vector unit
                    vectorUnit.startExecuting(rs.getInstruction(),
                            rs.getSourceData1(), rs.getSourceData2(),
                            rs.getVectorSrcData3(), rs.getVectorSrcData4(),
                            rs.getDestination(), rs.getRobEntry());
                    
                    // Increment executed counter
                    processor.incrementExecutedCount();
                    
                    // Clear reservation station
                    rs.flush();
                }
            } else {
                vectorUnit.continueExecuting();
            }
        }
    }

    @Override
    public void flush() {}
    
}
