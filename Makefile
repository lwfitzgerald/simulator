all: javacode

javacode:
	javac `find . -name "*.java"`
	chmod 755 launchers/*

clean:
	rm `find . -name "*.class"`

