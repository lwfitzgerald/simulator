# This program demonstrates the benefit of the branch
# history table prediction mechanism
#
# To demonstrate run using --no-branch-table and
# without the flag

            LDR r0, 100 # Loop counter
            LDR r1, 1
            LDR r2, 0 # 0 comparator
            LDR r3, 1 # Shift distance

Loop:       LS r1, r1, r3 # Perform left shift to multiply by 2
            SUBI r0, r0, 1 # Decrement loop counter
            BNE r0, r2, FauxLoop # Branch forward to trip up worse prediction
            B End

FauxLoop:   B Loop # Branch to the actual beginning of the loop

End:        NOP
