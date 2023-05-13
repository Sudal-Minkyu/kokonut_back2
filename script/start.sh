#!/bin/bash

cd /root/kokonut_backend/

ps -ef | grep java|awk '{print $2}'
# 프로세스 종료

kill -9 ps -ef | grep java|awk '{print $2}'

# 10초간 대기합니다.
sleep 15

# 파일 삭제
#rm *.jar

# 새로운 파일 복사
cp /opt/codedeploy-agent/deployment-root/$DEPLOYMENT_GROUP_ID/$DEPLOYMENT_ID/deployment-archive/kokonut*.jar /root/kokonut_backend/kokonut*.jar

source /root/kokonut_backend/.zshrc

# 새로운 프로세스 시작
nohup java -jar kokonut-0.0.1-SNAPSHOT.jar 1>vite.stdout 2>vite.stderr &

exit

