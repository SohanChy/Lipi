#!/bin/bash
echo "Building mode.utility ..."
javac -d build -cp build:extjars/* packages/model/utility/*.java
echo "Building model.hugo ..."
javac -d build -cp build packages/model/hugo/*.java
echo "Building CurrentDir ..."
javac -cp build *.java
echo "---Building done.---"
