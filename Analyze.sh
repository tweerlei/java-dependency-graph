#!/bin/sh

CLASSPATH=./target/classes:~/.m2/repository/org/ow2/asm/asm/6.2.1/asm-6.2.1.jar

java -classpath "$CLASSPATH" de.tweerlei.analyzer.Analyze "$@"
