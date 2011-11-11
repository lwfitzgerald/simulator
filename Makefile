ASSEMBLER=java com.fitzgerald.simulator.assembler.Assembler

all: javacode benchmarks

javacode:
	javac `find . -name "*.java"`

benchmarks: javacode
	$(ASSEMBLER) testPrograms/vectorAdd.asm testPrograms/vectorAdd.bin
	$(ASSEMBLER) testPrograms/vectorMul.asm testPrograms/vectorMul.bin
	$(ASSEMBLER) testPrograms/fibonacci.asm testPrograms/fibonacci.bin

clean: cleanbenchmarks
	rm `find . -name "*.class"`

cleanbenchmarks:
	rm testPrograms/vectorAdd.bin
	rm testPrograms/vectorMul.bin
	rm testPrograms/fibonacci.bin
