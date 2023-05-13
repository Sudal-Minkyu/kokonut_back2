#!/bin/bash
sudo su -
cd /root/kokonut_backend/

# 프로세스 종료
pkill -9 $(ps -ef | grep java | awk '{print $2}')

export KOKONUT_JWT_SECRET=$(aws secretsmanager get-secret-value --secret-id KOKONUT_JWT_SECRET --query SecretString --output text)

source /root/kokonut_backend/.zshrc

# 10초간 대기합니다.
sleep 10

# 파일 삭제
#rm /root/kokonut_backend/kokonut*.jar
# 새로운 파일 복사
cp /opt/codedeploy-agent/deployment-root/$DEPLOYMENT_GROUP_ID/$DEPLOYMENT_ID/deployment-archive/kokonut*.jar /root/kokonut_backend/kokonut*.jar

# source /root/kokonut_backend/.zshrc
# 새로운 프로세스 시작

nohup java -jar /root/kokonut_backend/kokonut-0.0.1-SNAPSHOT.jar > /root/kokonut_backend/vite.log 2>&1 &

exit
