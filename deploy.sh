#!/bin/bash

ECHO "ARGS: $1"

cd ./backend

gradle run --args=$1