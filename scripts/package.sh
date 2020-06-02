#!/bin/bash

# ----------------------------------------------
# Package structure
#
# recengine-[VERSION]-[ENVIRONMENT].zip/tar.gz
#     |- recengine-[VERSION]-[ENVIRONMENT]
#           |- recengine.sh
#           |- bin
#           |   |- recengine.jar
#           |- conf
#               |- application.properties
#               |- logback.xml
#               |- override.properties
#               |- version
#
# ---------------------------------------------

# ----------------
# Script arguments
# ----------------
# ARG 1 - Target directory
# ARG 2 - Artifact id
# ARG 3 - Artifact version
# ARG 4 - Package type - values should be "zip" or "tar.gz"

TARGET_FOLDER=$1
ARTIFACT_ID=$2
ARTIFACT_VERSION=$3
PACKAGE_TYPE=$4
PROFILE=$5

# Package name to be created
PACKAGE_NAME=${ARTIFACT_ID}-${ARTIFACT_VERSION}-${PROFILE}
# Package directory
PACKAGE_DIR="${TARGET_FOLDER}/${PACKAGE_NAME}"

setColor()
{
    tput setaf "$1" 2>/dev/null
}

resetColor()
{
    tput sgr0 2>/dev/null
}

echoProgress()
{
    setColor 6
    printf "%-70s" "$1..."
    resetColor
    return 0
}

echoOK()
{
    setColor 2
    printf "OK"
    if [ ! -z "$1" ]
    then
            resetColor
            printf "%s" " [$1]"
    fi
    printf "\n"
    resetColor
    return 0
}

# ------------------------------
# Create the directory structure
# ------------------------------
echoProgress "Creating package directory structure"

mkdir -p "$PACKAGE_DIR"
mkdir -p "$PACKAGE_DIR/bin"
mkdir -p "$PACKAGE_DIR/conf"

echoOK "Package directory structure"

# --------------------------------------------
# Copy the contents to the package directories
# --------------------------------------------
echoProgress "Copying contents to package directory structure"

cp "${TARGET_FOLDER}/${ARTIFACT_ID}-${ARTIFACT_VERSION}.jar" "$PACKAGE_DIR/bin/recengine.jar"
cp scripts/recengine.sh "$PACKAGE_DIR/."
cp src/main/resources/application.properties "$PACKAGE_DIR/conf/."
cp src/main/resources/logback.xml "$PACKAGE_DIR/conf/."
cp "src/main/environmentConfigs/override-${PROFILE}.properties" "$PACKAGE_DIR/conf/override.properties"

# Write a file to keep the artifact version
echo $ARTIFACT_VERSION > "$PACKAGE_DIR/conf/version"

echoOK "Content copy"

# --------------------------------------------------------
# Add logback.xml reference to application.properties file
# --------------------------------------------------------
printf "\nlogging.config=conf/logback.xml\n" >> "$PACKAGE_DIR/conf/application.properties"

echoProgress "Creating final package"

# -----------------------------------
# Generate the zip or tar.gz packages
# -----------------------------------
if [ $PACKAGE_TYPE = "tar.gz" ]; then
  cd $TARGET_FOLDER && tar -zcvf "$PACKAGE_NAME.tar.gz" "$PACKAGE_NAME"
else
  cd $TARGET_FOLDER && zip -r "$PACKAGE_NAME.zip" "$PACKAGE_NAME"
fi

echoOK "Final package"