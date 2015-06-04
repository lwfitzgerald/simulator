# Calculate and print fibonacci numbers
# Should be run with --no-status to see
# output prints

        LDR r0, 0 # i-2
        LDR r1, 1 # i-1
        LDR r2, 10 # Loop counter
        LDR r3, 0 # 0 comparator
        PRINT r0
        PRINT r1
Loop:   ADD r4, r1, r0 # Fi = Fi-1 + Fi-2
        PRINT r4
        SUBI r2, r2, 1 # Loop counter--
        ADDI r0, r1, 0 # Fi-2 = Fi-1
        ADDI r1, r4, 0 # Fi-1 = Fi
        BGT r2, r3, Loop # Branch if loop counter > 0
