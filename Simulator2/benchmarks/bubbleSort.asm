# This program implements bubble sort

            # Initially set up the array to be sorted
            LDR r10, 0 # Address to store in

            STC r10, 0, 10
            STC r10, 4, 4
            STC r10, 8, 28
            STC r10, 12, 16
            STC r10, 16, 29
            STC r10, 20, 47
            STC r10, 24, 2
            STC r10, 28, 1
            STC r10, 32, 20
            STC r10, 36, 7

            # Sort!
            LDR r0, 10 # n = 10
            LDR r11, 4 # 4 multiplier
            LDR r12, 0 # 0 comparator

RepeatLoop: LDR r1, 0 # swapped = false

            LDR r2, 1 # i = 1

ForLoop:    SUBI r3, r2, 1 # r3 = i - 1
            MUL r4, r3, r11 # r4 = (i - 1) * 4
            MUL r5, r2, r11 # r5 = i * 4
            LD r6, r4, 0 # Load A[i-1]
            LD r7, r5, 0 # Load A[i]

            BLEQ r6, r7, EndForLoop # Branch to ending if A[i-1] <= A[i]

            # A[i-1] > A[i] so swap
Swap:       ST r6, r5, 0 # A[i] = A[i-1]
            ST r7, r4, 0 # A[i-1] = A[i]
            ADDI r1, r1, 1 # swapped++

EndForLoop: ADDI r2, r2, 1 # i++
            BNE r2, r0, ForLoop # Branch back if i != n

EndRepLoop: BNE r1, r12, RepeatLoop # Branch if swapped > 0

            # Load and print results
            LDR r0, 0 # i = 0
            LDR r2, 40 # 40 comparator

PrintLoop:  LD r1, r0, 0 # r1 = A[i]
            PRINT r1
            ADDI r0, r0, 4 # i += 4
            BNE r0, r2, PrintLoop 
