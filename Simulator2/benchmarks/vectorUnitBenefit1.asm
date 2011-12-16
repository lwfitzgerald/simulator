# This program demonstrates the benefit of using the vector
# operations when compared against the results of
# vectorunitBenefit2.asm

        LDR r0, 1
        LDR r1, 2
        LDR r2, 3
        LDR r3, 4
        LDR r5, 100 # Loop counter
        LDR r6, 0 # 0 comparator

        # r0/r1 = 0x00000001 = 1
        # r2/r3 = 0x00020003 = 131075
Loop:   VECMUL r4, 1, 131075 # Vector instructions

        ADDI r8, r8, 45 # Other non-vectorisable ALU ops
        XOR r9, r9, r9
        AND r10, r10, r11

        SUBI r5, r5, 1 # Decrement loop counter
        BNE r5, r6, Loop
