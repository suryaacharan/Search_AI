#!/bin/sh
if [ ! -f ./CS205Project-1.0-SNAPSHOT.jar ]; then
    mvn clean install
    sleep 5
fi
clear
java -jar CS205Project-1.0-SNAPSHOT.jar