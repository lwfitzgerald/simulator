        LDR r0, 10
        LDR r1, 0

Loop:   BEQ r0, r1, End
        SUBI r0, r0, 1
        B Loop

End:    ADDI r0, r0, 1
