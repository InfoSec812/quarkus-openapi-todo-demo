#!/bin/bash

./mvnw -pl jsclient clean build-helper:parse-version frontend:install-node-and-npm@installNode openapi-generator:generate@api-client-sources frontend:npm@install-deps frontend:npm@transpile-api-client
