all: javacode benchmarks

javacode:
	javac `find . -name "*.java"`

benchmarks: javacode
	java com.fitzgerald.simulator.assembler.Assembler testPrograms/vectorAdd.asm testPrograms/vectorAdd.bin

clean: cleanbenchmarks
	rm `find . -name "*.class"`

cleanbenchmarks:
	rm testPrograms/vectorAdd.bin
