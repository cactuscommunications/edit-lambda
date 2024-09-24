#!/bin/bash

# Function to extract the version from pom.xml
get_version_from_pom() {
    version=$(xmlstarlet sel -t -v "/project/version" pom.xml 2>/dev/null)

    if [ -z "$version" ]; then
        version=$(grep -m 1 '<version>' pom.xml | sed -e 's/.*<version>\(.*\)<\/version>.*/\1/')
    fi

    # Trim any whitespace (optional)
    version=$(echo "$version" | xargs)

    echo "$version"
}

# Prompt for the version
read -p "Enter build version (current version is $(get_version_from_pom)): " input_version

# Determine the version to use
if [ -n "$input_version" ]; then
    version="${input_version}-snapshot"
else
    base_version=$(get_version_from_pom)
    if [ -z "$base_version" ]; then
        echo "No version found in pom.xml. Exiting..."
        exit 1
    fi
    version="${base_version}-snapshot"
    echo "Using version from pom.xml: $version"
fi
mvn versions:set -DnewVersion="$version" -DgenerateBackupPoms=false

mvn clean package clean package -DskipTests -Dmaven.test.skip=true
rm dependency-reduced-pom.xml
# Run the Maven command to update the version and build the project
#mvn versions:set -Drevision="$version" -DnewVersion="$version" -DgenerateBackupPoms=false clean package -DskipTests

# Check if the Maven command was successful
if [ $? -ne 0 ]; then
    echo "Maven command failed. Exiting..."
    exit 1
fi

rm dependency-reduced-pom.xml
echo "Build successful. Version updated to $version."