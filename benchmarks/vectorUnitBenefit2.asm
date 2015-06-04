# This program produces the same results as vectorUnitBenefit1.asm
# but without the use of the vector operations

        LDR r0, 1
        LDR r1, 2
        LDR r2, 3
        LDR r3, 4
        LDR r7, 100 # Loop counter
        LDR r8, 0 # 0 comparator

Loop:   MUL r5, r0, r1 # Equivalent vector op
        MUL r6, r2, r3
        MUL r4, r5, r6

        ADDI r9, r9, 45 # Other non-vectorisable ALU ops
        XOR r10, r10, r10
        AND r11, r11, r12

        SUBI r7, r7, 1 # Decrement loop counter
        BNE r7, r8, Loop
