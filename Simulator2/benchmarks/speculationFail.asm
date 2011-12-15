        # Fetched cycle 1
        LDR r0, 10 # Loop counter
        LDR r1, 0 # 0 comparator
        LDR r2, 1 # Value to be shifted

        # Fetched cycle 2
        LDR r3, 1 # Shift distance
        LDR r5, 1 # Set indicator

Loop:   LS r2, r2, r3 # Shift

        # Fetched cycle 3
        SUBI r0, r0, 1 # Decrement loop counter
        BNE r5, r1, Zero # If indicator == 1
        ADDI r5, r5, 1 # Increment indicator

        # Cannot be fetched cycle 4
        # as only 1 level of speculation
        BNE r0, r1, Loop
        B End

Zero:   LDR r5, 0 # Zero r5
        B Loop

End:    NOP
