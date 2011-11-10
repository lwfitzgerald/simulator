all:
	javac `find . -name "*.java"`

clean:
	rm `find . -name "*.class"`
