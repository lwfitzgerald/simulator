# This program is used to demonstrate how some
# programs can benefit from larger
# fetch/decode widths.

        LDR r0, 100 # Loop counter
        LDR r7, 0 # 0 comparator
        
Loop:   ADDI r1, r1, 1
        ADDI r2, r2, 2
        ADDI r3, r3, 3
        SUBI r4, r4, 4
        SUBI r5, r5, 5
        SUBI r6, r6, 6

        SUBI r0, r0, 1 # Decrement loop counter
        BNE r0, r7, Loop
