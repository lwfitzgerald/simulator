To compile the simulator and test programs run:

    make

To execute the simulator run:

    java com.fitzgerald.simulator.processor.Simulator benchmarks/benchmark.asm

where benchmark.asm is the filename of a benchmark in the benchmarks directory.
All benchmarks have a brief comment at the top explaining their purpose.

The simulator also takes a range of arguments, run as:

    java com.fitzgerald.simulator.processor.Simulator ASMFILE args

    --step  Optional argument enabling stepping of cycles

    --no-status Disable status output at the end of each step

    --no-branch-table   Optional argument disabling the branch result table when predicting

    --fd-width=NUM  Optional argument setting fetch/decode width, 3 by default

    --num-alus=NUM  Optional argument setting number of ALUs, 3 by default

    --num-ls-units=NUM  Optional argument setting number of Load/Store units, 2 by default

    --num-branch-units=NUM  Optional argument setting number of Branch units, 2 by default

    --help  Show this message

The launchers directory contains shell scripts to run programs in a particular way for the presentation
