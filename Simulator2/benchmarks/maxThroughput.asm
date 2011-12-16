# This program demonstrates maximum throughput of the
# processor with default parameters

        LD r7, r7, 0 # Two loads to same address
        LD r8, r8, 0 # Allowed to both execute
        LDR r0, 1 # ALU op

        # 3x ALU ops + WAW dependency
        LDR r0, 5 # WAW dependency with previous LDR r0
        LDR r1, 2
        ADDI r2, r2, 3

        # 3x ALU ops
        LDR r3, 4
        LDR r4, 5
        LDR r5, 6

        # 3x ALU ops + RAW dependency + WAR dependency
        # RAW dependency with r0, r1, r2
        # WAR dependency with memory operations
        OR r6, r0, r1
        OR r7, r1, r2
        OR r8, r2, r3

        # 3x ALU ops
        AND r9, r0, r1
        AND r10, r2, r3
        AND r11, r3, r4
