#!/bin/bash

# The installation directory
name=algs4

# Executing directory of the script
myDir="$( cd "$( dirname "$0" )" && pwd )"

# Runs the installation script, teeing to a log file
bash "${myDir}"/"${name}".sh 2>&1 | tee ~/log.txt

# Takes out the color coding from the log file
ESC=`echo -e "\033"`
sed -iE 's,'${ESC}[\[][0-9]*[a-zA-Z]',,g' ~/log.txt 2> /dev/null
rm ~/log.txtE

# Moves the log to its final location
mv ~/log.txt /usr/local/${name}/log.txt 2> /dev/null

# Sets the owner of .drjava back to the original,
# since this script runs under root.
chown -R ${1} ~/.drjava 2> /dev/null

# chown -R ${1} /Applications/DrJava.app 2> /dev/null
# chown -R ${1} ~/Library/Java/Extensions 2> /dev/null
