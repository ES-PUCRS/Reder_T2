#!/bin/bash

if [ ! -d "_class" ]
then
	mkdir _class
fi

javac *.java -d _class

if [ $? -eq 0 ]
then
	java -cp  _class Agent
fi
