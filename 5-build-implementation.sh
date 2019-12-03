#!/bin/bash

./mvnw -pl frontend frontend:npm@quasar-build

./mvnw -pl implementation clean package -DskipTests quasar:dev