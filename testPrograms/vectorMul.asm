        # Fill array A 1-10
        LDR r0, 0 # Mem counter
        LDR r1, 10 # Loop counter
        LDR r2, 0 # 0 comparator
LoopA:  ST r1, r0, 0
        ADDI r0, r0, 4 # Mem += 4
        SUBI r1, r1, 1 # Loop counter--
        NOP
        BGT r1, r2, LoopA # Loop counter > 0

        # Fill array B 10-1
        LDR r3, 1 # Value counter
        LDR r1, 10 # Loop counter
LoopB:  ST r3, r0, 0
        ADDI r0, r0, 4 # Mem += 4
        SUBI r1, r1, 1 # Loop counter--
        ADDI r3, r3, 1 # Value counter++
        NOP
        BGT r1, r2, LoopB # Loop counter > 0

        # Perform the vector add
        LDR r1, 10 # Loop counter
        LDR r3, 0 # A mem counter
        LDR r4, 36 # B mem counter
VectorL:LD r5, r3, 0 # Load A[i]
        LD r6, r4, 0 # Load B[i]
        NOP
        MUL r7, r5, r6 # A[i] * B[i]
        NOP
        ST r7, r0, 0 # C[i] = A[i] + B[i]
        ADDI r0, r0, 4 # C mem counter += 4
        ADDI r3, r3, 4 # A mem counter += 4
        SUBI r1, r1, 1 # Loop counter--
        ADDI r4, r4, 4 # B mem counter += 4
        BGT r1, r2, VectorL # Loop counter > 0
