#!/bin/bash

TARGET_FOLDER=$1
ARTIFACT_ID=$2
ARTIFACT_VERSION=$3
PACKAGE_NAME=$4
PACKAGE_TYPE=$5

PACKAGE_DIR="$TARGET_FOLDER/$PACKAGE_NAME"

mkdir -p "$PACKAGE_DIR"
mkdir -p "$PACKAGE_DIR/bin"
mkdir -p "$PACKAGE_DIR/conf"

cp "${TARGET_FOLDER}/${ARTIFACT_ID}-${ARTIFACT_VERSION}.jar" "$PACKAGE_DIR/bin/recengine.jar"
cp scripts/recengine.sh "$PACKAGE_DIR/."
cp src/main/resources/application.properties "$PACKAGE_DIR/conf/."

if [ $PACKAGE_TYPE = "tar.gz" ]; then
  cd $TARGET_FOLDER && tar -zcvf "$PACKAGE_NAME.tar.gz" "$PACKAGE_NAME"
else
  cd $TARGET_FOLDER && zip -r "$PACKAGE_NAME.zip" "$PACKAGE_NAME"
fi