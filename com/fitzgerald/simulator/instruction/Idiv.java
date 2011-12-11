package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.pipeline.DecodeStage;
import com.fitzgerald.simulator.pipeline.ExecuteStage;
import com.fitzgerald.simulator.processor.ALU;
import com.fitzgerald.simulator.processor.BranchUnit;
import com.fitzgerald.simulator.processor.MemoryController;
import com.fitzgerald.simulator.processor.Processor;
import com.fitzgerald.simulator.processor.RegisterFile;
import com.fitzgerald.simulator.processor.Util;

public class Idiv extends Instruction {

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = -8837315849558287407L;

	@Override
	public int getALUCyclesRequired() {
		// Integer divide requires 2 cycles
		return 2;
	}

	@Override
	public void decode(RegisterFile registerFile, DecodeStage decodeStage) {
		byte[] sourceData1 = registerFile.getRegister(Util.bytesToInt(operand2)).getCurrentValue();
        byte[] sourceData2 = registerFile.getRegister(Util.bytesToInt(operand3)).getCurrentValue();
        
        /*
         * Do a deep copy of the source data and store the
         * reference as the next source data value for this
         * instruction
         */
        decodeStage.setSourceData1(sourceData1.clone());
        decodeStage.setSourceData2(sourceData2.clone());
	}

	@Override
	public boolean execute(Processor processor, RegisterFile registerFile,
			ALU alu, BranchUnit branchUnit, MemoryController memoryController,
			ExecuteStage executeStage) {
		
		byte[] result = alu.performOperation(executeStage);
        
        if (result == null) {
            // More cycles required
            return false;
        }
        
        registerFile.getRegister(Util.bytesToInt(operand1)).setNextValue(result);
        return true;
	}

	@Override
	public byte[] aluOperation(ExecuteStage executeStage) {
		int srcInt1 = Util.bytesToInt(executeStage.getSourceData1());
        int srcInt2 = Util.bytesToInt(executeStage.getSourceData2());
        int result = srcInt1 / srcInt2;
        
        return Util.intToBytes(result);
	}

	@Override
	public boolean branchCondition(ExecuteStage executeStage) {
		// Not applicable
		return false;
	}

	@Override
	public byte[] branchCalculation(ExecuteStage executeStage) {
		// Not applicable
		return null;
	}

	@Override
	public String toString() {
		return "IDIV r" + Util.bytesToInt(operand1) +
	               ", r" + Util.bytesToInt(operand2) +
	               ", r" + Util.bytesToInt(operand3);
	}

}