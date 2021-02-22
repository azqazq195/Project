#!/bin/bash

REPOSITORY=/home/ubuntu/apps/Project_Foret_APP2

cd $REPOSITORY/SpringBoot/

echo "> Git Pull"

git pull

echo "> Starting Build Project"

./gradlew build

echo "> Copy Build File"

cp ./build/libs/*.jar $REPOSITORY/

echo "> Check Running Application PID"

CURRENT_PID=$(pgrep -f foret)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "> Cannot Find Running Application"
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 5
fi

echo "> Build New Application"

JAR_NAME=$(ls $REPOSITORY/ |grep 'foret' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME &