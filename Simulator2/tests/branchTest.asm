        LDR r0, 10
        LDR r5, 0

Loop:   LDR r1, 10
        ADDI r2, r2, 1

InnerL: LDR r6, 10
        ADDI r10, r10, 1

Inner2: ADDI r11, r11, 1
        SUBI r6, r6, 1
        BNE r6, r5, Inner2

        SUBI r1, r1, 1
        BNE r1, r5, InnerL

        SUBI r0, r0, 1
        BNE r0, r5, Loop
