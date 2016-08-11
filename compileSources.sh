#!/bin/bash
echo "Building mode.utility ..."
javac -d build -cp build:extjars/toml4j-0.7.1.jar packages/model/utility/*.java
echo "Building model.hugo ..."
javac -d build -cp build packages/model/hugo/*.java
echo "Building CurrentDir ..."
javac -cp build *.java
echo "---Building done.---"
