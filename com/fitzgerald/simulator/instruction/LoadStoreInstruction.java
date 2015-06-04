package com.fitzgerald.simulator.instruction;

import com.fitzgerald.simulator.processor.Memory;

public abstract class LoadStoreInstruction extends Instruction {

    public enum LoadStoreType { LOAD, STORE };
    
    /**
     * Serialisation ID
     */
    private static final long serialVersionUID = 1040461511489248016L;
    
    public static final int NUM_CYCLES_REQUIRED = 4;
    
    /**
     * Get the type of this load store
     * (load or store)
     * @return Load or store
     */
    public abstract LoadStoreType getLSType();
    
    /**
     * Get the address this instruction loads
     * or stores from
     * @param reservationStation RS holding instruction
     * @return address this instruction loads
     * or stores from
     */
    public abstract int getLSAddress(Integer srcData1, Integer srcData2, Integer dest);
    
    /**
     * Perform the load/store
     * @param srcData1 Source data 1 or null if N/A
     * @param srcData2 Source data 2 or null if N/A
     * @param dest Destination or null if N/A
     * @param memory Memory reference
     * @return Result or null if N/A
     */
    public abstract Integer memoryOperation(Integer srcData1, Integer srcData2,
            Integer dest, Memory memory);
}
