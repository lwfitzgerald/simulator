        # Calculate x^y
        
        LDR r0, 2 # x
        LDR r1, 10 # y
        LDR r2, 1 # result
        LDR r3, 0 # 0 comparator
        LDR r4, 1 # 1 for ANDing
        LDR r5, 2 # 2 for division
        
        BEQ r1, r3, End
While:  AND r6, r1, r4 # y & 1
        BNE r6, r3, Odd

Even:   MUL r0, r0, r0 # x = x * x
        IDIV r1, r1, r5 # y = y / 2
        BNE r1, r3, While # Branch if y != 0
        B End

Odd:    MUL r2, r0, r2 # result = x * result
        SUBI r1, r1, 1 # y--
        BNE r1, r3, While # Branch if y != 0

End:    NOP
