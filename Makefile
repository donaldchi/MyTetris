#Makefile
ALLSOURCE = src/*.java
JAVAC = /usr/bin/javac

.PHONY: all
all: tetris
tetris:
	${JAVAC} ${ALLSOURCE}
	mv src/*.class  class/

.PHONY: clean

clean:
	rm -rf class/*.class
