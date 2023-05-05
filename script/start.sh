#!/bin/bash

# Stop existing process
echo "Stopping existing process..."
pkill -f kokonut-0.0.1-SNAPSHOT.jar

# 10초간 대기합니다.
sleep 10

source ~/.zshrc

# 새로운 프로세스를 시작합니다.
echo "Starting new process..."
nohup java -jar *.jar &
