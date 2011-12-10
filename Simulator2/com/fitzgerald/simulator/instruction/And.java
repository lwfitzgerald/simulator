package com.fitzgerald.simulator.instruction;

public class And extends LogicalInstruction {

	/**
	 * Serialisation ID
	 */
	private static final long serialVersionUID = -562527121778039619L;

	@Override
    public int aluOperation(Integer srcData1, Integer srcData2) {
        return srcData1 & srcData2;
    }

}
