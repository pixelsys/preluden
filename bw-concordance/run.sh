#!/bin/sh
mvn exec:java -Dexec.mainClass="bw.exercise.Concordance" -Dexec.args="$1" 