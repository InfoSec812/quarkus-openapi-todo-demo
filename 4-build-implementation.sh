#!/bin/bash

./mvnw -pl implementation,server,javaclient clean package -DskipTests

java -jar implementation/target/todo-implementation-1.0.0-SNAPSHOT-runner.jar