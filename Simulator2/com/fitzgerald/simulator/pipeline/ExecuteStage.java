package com.fitzgerald.simulator.pipeline;

import com.fitzgerald.simulator.executionstage.ALU;
import com.fitzgerald.simulator.executionstage.BranchUnit;
import com.fitzgerald.simulator.executionstage.LoadStoreUnit;
import com.fitzgerald.simulator.instruction.ALUInstruction;
import com.fitzgerald.simulator.instruction.Instruction.InstructionType;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.ReservationStation;

public class ExecuteStage {

    public void step(Processor processor, RegisterFile registerFile,
            ALU[] alus, LoadStoreUnit[] lsUnits, BranchUnit[] branchUnits) {
        
        ReservationStation rs;
        
        for (ALU alu : alus) {
            if (alu.isIdle()) {
                // Idle so dispatch new instruction from reservation station
                
                // Get ready instruction
                rs = processor.getReadyReservationStation(InstructionType.ALU);
                
                // Dispatch to ALU
                alu.startExecuting((ALUInstruction) rs.getInstruction(), rs.getSourceData1(),
                        rs.getSourceData2(), rs.getDestination(), rs.getRobEntry());
            } else {
                alu.continueExecuting();
            }
        }
        
        for (LoadStoreUnit lsUnit : lsUnits) {
            
        }
        
        for (BranchUnit branchUnit : branchUnits) {
            
        }
    }
    
}
