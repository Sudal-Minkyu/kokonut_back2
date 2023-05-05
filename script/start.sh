#!/bin/bash

cd /root/kokonut_backend/

# Stop existing process
echo "Stopping existing process..."
pkill -f kokonut*.jar

# 10초간 대기합니다.
sleep 10

source /root/.zshrc

# 새로운 프로세스를 시작합니다.
echo "Starting new process..."
nohup java -jar kokonut-0.0.1-SNAPSHOT.jar &
