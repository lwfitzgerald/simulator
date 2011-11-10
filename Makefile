all: javacode benchmarks

javacode:
	javac `find . -name "*.java"`

benchmarks: javacode
	java com.fitzgerald.simulator.assembler.Assembler testPrograms/vectorAdd.asm testPrograms/vectorAdd.bin

clean:
	rm `find . -name "*.class"`
	rm testPrograms/*.bin
