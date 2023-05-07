#!/bin/bash

cd /root/kokonut_backend/


# 프로세스 종료
PID=$(pgrep -f kokonut*.jar)
kill $PID

# 10초간 대기합니다.
sleep 10

# 파일 삭제
sudo rm *.jar

# 새로운 파일 복사
sudo cp /opt/codedeploy-agent/deployment-root/$DEPLOYMENT_GROUP_ID/$DEPLOYMENT_ID/deployment-archive/kokonut*.jar /root/kokonut_backend/kokonut*.jar

sudo source ~/.zshrc

# 새로운 프로세스 시작
sudo nohup java -jar kokonut-0.0.1-SNAPSHOT.jar 1>vite.stdout 2>vite.stderr &

exit
