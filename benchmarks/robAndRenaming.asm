# This program demonstrates maximum throughput as well
# as resolution of RAW, WAR and WAW dependencies using
# renaming and the reorder buffer

        LD r7, r10, 0 # Two loads to same address
        LD r8, r11, 0 # Allowed to both execute
        LDR r8, 1 # WAW dependency with previous LDR r0

        # 3x ALU ops
        LDR r0, 5
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
