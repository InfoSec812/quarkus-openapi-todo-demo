#!/bin/bash

./mvnw -pl frontend clean frontend:install-node-and-npm@installNode build-helper:parse-version frontend:npm@set-version frontend:npm@3-npm-install-client-sdk frontend:npm@2-npm-install