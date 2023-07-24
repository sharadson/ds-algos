#!/bin/bash

# ************************************************
# algs4.sh                                     
# Hayk Martirosyan  
# -------------
# Mac OS X installation script for algs4.app
# ************************************************

# The differences between algs4 and introcs installations are these
# three variables, and the exclusion of stdlib.jar.
book="algs4"
course="cos226"
mooc="coursera"
url_base="https://algs4.cs.princeton.edu/mac"
testFile="TestAlgs4"

### COLOR PRINTING FUNCTIONS ###############################################

function normal {
	tput sgr0
}
function red { 
	tput setaf 1
	print "${1}"
	normal
}
function green { 
	tput setaf 2 
	print "${1}"
	normal
}
function blue { 
	tput setaf 4
	print "${1}"
	normal
}
function print {
	echo "${1}"
}

### INTRODUCTION SCREEN ####################################################

# Ensures terminal emulator can handle color
export TERM=xterm-color

clear

green '####################################################################'
green '#                                                                  #'
green '#            d8888 888      .d8888b.   .d8888b.        d8888       #'
green '#           d88888 888     d88P  Y88b d88P  Y88b      d8P888       #'
green '#          d88P888 888     888    888 Y88b.          d8P 888       #'
green '#         d88P 888 888     888         "Y888b.      d8P  888       #'
green '#        d88P  888 888     888  88888     "Y88b.   d88   888       #'
green '#       d88P   888 888     888    888       "888   8888888888      #'
green '#      d8888888888 888     Y88b  d88P Y88b  d88P         888       #'
green '#     d88P     888 88888888 "Y8888P88  "Y8888P"          888       #'
green '#                                                                  #'
green '####################################################################'
green '#                                                                  #'
green '# Java Programming Environment Setup                               #'
green '# for Mac OS X - v4.3                                              #'
green '# Written by Hayk Martirosyan and Kevin Wayne                      #'
green '# Princeton University                                             #'
green '#                                                                  #'
green '####################################################################'

print
print 'Initializing functions and beginning installation...'

### UTILITY FUNCTIONS  #####################################################


# Handy to supress output
null="/dev/null"

function logAndExit {
	
	print
	red 'NOTE: If there were any error messages during this setup, check the'
	red 'troubleshooting section on the website or ask for help.'
	
	print
	print 'A log file of this installation is saved at'
	blue "${install}/log.txt"
	
	print
	green 'You should now close this window.'
	exit
}

# download the file using curl
function download {
	print
	print "Downloading ${3} from"
	blue "${1}"
	print "to"
	blue "${2}"
	downloadQuiet "${1}" "${2}"
}

# download the file using curl (silently unless there is an error)
function downloadQuiet {
    if curl --silent --show-error --fail "${1}" -o "${2}"; then
		return 1
	else
		print
		red "Cannot download ${1}"
		red "Make sure you have a working Internet connection"
		red "and rerun this auto-installer."
	
		logAndExit
	fi
}

# unzip the .zip file
function extractAndDelete {
	print
	print "Extracting zip archive in place at"
	blue "${1}"
	dest=`dirname ${1}`
	unzip -qqo "${1}" -d "${dest}"
	print "and deleting .zip file."
	rm -f "${1}"
}

# make the file executable
function grantExecutablePermissionTo {
    print
    print "Granting executable permission to"
    blue "${1}"
    chmod +x "${1}"
}

# [wayne f16] I don't think this works with wildcards
function deleteOldVersion {
	if [ -d "${1}" ]; then
		print
		print "Deleting old version of ${2} at"
		blue "${1}"
		rm -rf "${1}"
	fi
}

# [wayne f16] not needed if we put algs4.jar in either /Library/Java/Extensions or /usr/local/algs4
function replaceInFile {
	print
	print "Replacing text in file"
	blue "${1}"
	print "from"
	blue "${2}"
	print "to"
	blue "${3}"
	sed -i '' 's|'${2}'|'${3}'|g' "${1}"
}

# create the folder if it doesn't already exist
function createFolderIfNeeded {
	if [ ! -d "${1}" ]; then
		print
		print "Creating ${2} directory at"
		blue "${1}"
		mkdir "${1}"
	fi
}

### INITIALIZATION AND PRE-INSTALLATION SETUP ############################

# create /usr/local if needed
local="/usr/local"
createFolderIfNeeded "${local}" "user local"

# create /usr/local/algs4 if needed
install="${local}/${book}"
createFolderIfNeeded "$install" "installation"

# create /usr/local/bin if needed
bin="${local}/bin"
createFolderIfNeeded "$bin" "bin"

sleep 1


### BEGINNING OF ACTUAL INSTALLATION #####################################

print
red '#### Step 1 - Java #################################################'

# print results of "javac -version" and "java -version"
print
print "% javac -version"
javac -version
print
print "% java -version"
java -version

# assuming output of javac -version command is "javac 1.8.0_111", this will be "8"
JAVAC_VERSION=`javac -version 2>&1 | awk -F. '// {print $2}'`

# assuming output of javac -version command is "javac 9.0.1", this will be "9"
JAVAC_VERSION_ALTERNATIVE=`javac -version 2>&1 | awk '// {print $2}' | awk -F. '// {print $1}'`

if [ "$JAVAC_VERSION" = "8" ] || [ "$JAVAC_VERSION" == "9" ]; then
    print
    print "Java ${JAVAC_VERSION} appears to be properly installed."
elif [ "$JAVAC_VERSION_ALTERNATIVE" == "8" ] || [ "$JAVAC_VERSION_ALTERNATIVE" == "9" ]; then
    print
    print "Java ${JAVAC_VERSION_ALTERNATIVE} appears to be properly installed."
else
    print
    red 'Neither the Java 8 nor Java 9 JDK was detected.'
    red 'Install either the Java 8 or Java 9 JDK; then retry this installer.'
    print
    red 'You can close this window'
    exit
fi


# download java-algs4 and put in /usr/local/bin
javaCMDURL="${url_base}/java-${book}"
javaCMD="${bin}/java-${book}"
download "$javaCMDURL" "$javaCMD" "java execution script"
grantExecutablePermissionTo "$javaCMD"

# download javac-algs4 and put in /usr/local/bin
javacCMDURL="${url_base}/javac-${book}"
javacCMD="${bin}/javac-${book}"
download "$javacCMDURL" "$javacCMD" "javac execution script"
grantExecutablePermissionTo "$javacCMD"

# download java-cos226 and put in /usr/local/bin
javaCMDURL="${url_base}/java-${course}"
javaCMD="${bin}/java-${course}"
download "$javaCMDURL" "$javaCMD" "java-${course} execution script"
grantExecutablePermissionTo "$javaCMD"

# download javac-cos226 and put in /usr/local/bin
javacCMDURL="${url_base}/javac-${course}"
javacCMD="${bin}/javac-${course}"
download "$javacCMDURL" "$javacCMD" "javac-${course} execution script"
grantExecutablePermissionTo "$javacCMD"

# download java-coursera and put in /usr/local/bin
javaCMDURL="${url_base}/java-${mooc}"
javaCMD="${bin}/java-${mooc}"
download "$javaCMDURL" "$javaCMD" "java-${mooc} execution script"
grantExecutablePermissionTo "$javaCMD"

# download javac-coursera and put in /usr/local/bin
javacCMDURL="${url_base}/javac-${mooc}"
javacCMD="${bin}/javac-${mooc}"
download "$javacCMDURL" "$javacCMD" "javac-${mooc} execution script"
grantExecutablePermissionTo "$javacCMD"

print
red '#### Step 2 - Textbook Libraries ##################################'

#stdlibURL="https://introcs.cs.princeton.edu/java/stdlib/stdlib.jar"
#stdlib="${install}/stdlib.jar"
#download "$stdlibURL" "$stdlib" "stdlib.jar"

# download algs4.jar and put in /usr/local/algs4
# extensions="/Library/Java/Extensions"  # if we want to put in /Library/Java/Extensions
algs4URL="https://algs4.cs.princeton.edu/code/algs4.jar"
algs4="${install}/algs4.jar"
download "$algs4URL" "$algs4" "algs4.jar"


print
red '#### Step 3 - Findbugs ############################################'

# download and unzip findbugs.zip
findbugsURL="${url_base}/findbugs.zip"
findbugsZip="${install}/findbugs.zip"
download "$findbugsURL" "$findbugsZip" "findbugs"
extractAndDelete "$findbugsZip"

# download findbugs.xml
findbugsXMLURL="${url_base}/findbugs.xml"
findbugsXML="${install}/findbugs.xml"
download "$findbugsXMLURL" "$findbugsXML" "findbugs.xml configuration file"

# download findbugs-algs4 and put in /usr/local/bin
findbugsCMDURL="${url_base}/findbugs-${book}"
findbugsCMD="${bin}/findbugs-${book}"
download "$findbugsCMDURL" "$findbugsCMD" "findbugs-${book} execution script"
grantExecutablePermissionTo "$findbugsCMD"

# download findbugs-cos226 and put in /usr/local/bin
findbugsCMDURL="${url_base}/findbugs-${course}"
findbugsCMD="${bin}/findbugs-${course}"
download "$findbugsCMDURL" "$findbugsCMD" "findbugs-${course} execution script"
grantExecutablePermissionTo "$findbugsCMD"

# download findbugs-coursera and put in /usr/local/bin
findbugsCMDURL="${url_base}/findbugs-${mooc}"
findbugsCMD="${bin}/findbugs-${mooc}"
download "$findbugsCMDURL" "$findbugsCMD" "findbugs-${mooc} execution script"
grantExecutablePermissionTo "$findbugsCMD"

print
red '#### Step 4 - PMD ############################################'

# download and unzip .zip
pmdURL="${url_base}/pmd.zip"
pmdZip="${install}/pmd.zip"
download "$pmdURL" "$pmdZip" "pmd"
extractAndDelete "$pmdZip"

# download pmd.xml
pmdXMLURL="${url_base}/pmd.xml"
pmdXML="${install}/pmd.xml"
download "$pmdXMLURL" "$pmdXML" "pmd.xml configuration file"

# download pmd-algs4 and put in /usr/local/bin
pmdCMDURL="${url_base}/pmd-${book}"
pmdCMD="${bin}/pmd-${book}"
download "$pmdCMDURL" "$pmdCMD" "pmd-${book} execution script"
grantExecutablePermissionTo "$pmdCMD"

# download pmd-cos226 and put in /usr/local/bin
pmdCMDURL="${url_base}/pmd-${course}"
pmdCMD="${bin}/pmd-${course}"
download "$pmdCMDURL" "$pmdCMD" "pmd-${course} execution script"
grantExecutablePermissionTo "$pmdCMD"

# download pmd-coursera and put in /usr/local/bin
pmdCMDURL="${url_base}/pmd-${mooc}"
pmdCMD="${bin}/pmd-${mooc}"
download "$pmdCMDURL" "$pmdCMD" "pmd-${mooc} execution script"
grantExecutablePermissionTo "$pmdCMD"


print
red '#### Step 5 - Checkstyle ##########################################'

#deleteOldVersion "$checkstyle" "checkstyle"

# download and unzip checkstyle.zip
checkstyleURL="${url_base}/checkstyle.zip"
checkstyleZip="${install}/checkstyle.zip"
download "$checkstyleURL" "$checkstyleZip" "checkstyle"
extractAndDelete "$checkstyleZip"

# download checkstyle-algs4.xml
checkstyleXMLURL="${url_base}/checkstyle-${book}.xml"
checkstyleXML="${install}/checkstyle-${book}.xml"
download "$checkstyleXMLURL" "$checkstyleXML" "checkstyle-${book}.xml configuration file"

# download checkstyle-cos226.xml
checkstyleXMLURL="${url_base}/checkstyle-${course}.xml"
checkstyleXML="${install}/checkstyle-${course}.xml"
download "$checkstyleXMLURL" "$checkstyleXML" "checkstyle-${course}.xml configuration file"

# download checkstyle-coursera.xml
checkstyleXMLURL="${url_base}/checkstyle-${mooc}.xml"
checkstyleXML="${install}/checkstyle-${mooc}.xml"
download "$checkstyleXMLURL" "$checkstyleXML" "checkstyle-${mooc}.xml configuration file"

# download checkstyle-suppressions.xml
checkstyleXMLURL="${url_base}/checkstyle-suppressions.xml"
checkstyleXML="${install}/checkstyle-suppressions.xml"
download "$checkstyleXMLURL" "$checkstyleXML" "checkstyle-suppressions.xml file"

# download checkstyle-algs4 and put in /usr/local/bin
checkstyleCMDURL="${url_base}/checkstyle-${book}"
checkstyleCMD="${bin}/checkstyle-${book}"
download "$checkstyleCMDURL" "$checkstyleCMD" "checkstyle-${book} execution script"
grantExecutablePermissionTo "$checkstyleCMD"

# download checkstyle-cos226 and put in /usr/local/bin
checkstyleCMDURL="${url_base}/checkstyle-${course}"
checkstyleCMD="${bin}/checkstyle-${course}"
download "$checkstyleCMDURL" "$checkstyleCMD" "checkstyle-${course} execution script"
grantExecutablePermissionTo "$checkstyleCMD"

# download checkstyle-coursera and put in /usr/local/bin
checkstyleCMDURL="${url_base}/checkstyle-${mooc}"
checkstyleCMD="${bin}/checkstyle-${mooc}"
download "$checkstyleCMDURL" "$checkstyleCMD" "checkstyle-${mooc} execution script"
grantExecutablePermissionTo "$checkstyleCMD"


print
red '#### Step 6 - DrJava ##############################################'

# download DrJava.zip
drjavaApp="/Applications/DrJava.app"
drjavaZip="/Applications/DrJava.zip"
drjavaURL="${url_base}/DrJava.zip"
deleteOldVersion "$drjavaApp" "DrJava"
download "$drjavaURL" "$drjavaZip" "DrJava"

# unzip DrJava.zip (preserving DrJava.icns icon) and put in /Applications
# (don't call extractAndDelete or you'll lose the icon)
print
print 'Extracting zip archive in place at'
blue "${drjavaZip}"
print 'to create'
blue "${drjavaApp}"
ditto -x -k "$drjavaZip" "/Applications"
rm -f "$drjavaZip"

# download .drjava file and put in ~/.drjava
drjavaConfig=~/.drjava
drjavaConfigURL="${url_base}/.drjava"
download "$drjavaConfigURL" "$drjavaConfig" "DrJava configuration file"

# replaceInFile "${drjavaConfig}" "INSTALL_DIR" "${install}"

# create Desktop shortcut to DrJava
print
print 'Creating a shortcut to DrJava on the desktop...'
ln -sf "$drjavaApp" "${HOME}/Desktop"

print
red '#### Step 7 - Terminal #############################################'

# create Desktop shortcut to Terminal
terminalApp="/Applications/Utilities/Terminal.app"
print
print 'Creating a shortcut to Terminal on the desktop...'
ln -sf "$terminalApp" "${HOME}/Desktop"

print
red '#### Step 8 - Test it out! #########################################'


print
print 'Downloading the test Java program...'
javaTestURL="${url_base}/${testFile}.java"
javaTest="${install}/${testFile}.java"
downloadQuiet "$javaTestURL" "$javaTest"

coverURL="${url_base}/cover.jpg"
cover="${install}/cover.jpg"
downloadQuiet "$coverURL" "$cover"

cd "$install"

print
print 'Installation complete! Compiling test program...'
javac-${book} ${testFile}.java
#javac ${testFile}.java

print 'Test program compiled. Running...'
java-${book} ${testFile}
#java ${testFile}

rm -f "${testFile}.java"
rm -f "${testFile}.class"
rm -f "cover.jpg"

print
green 'If you saw the bullseye and textbook graphic, the installation'
green 'was successful and you are ready to start programming in Java.'
green 'Continue with the introductory tutorial on the booksite.'

say "Congratulations! You are now ready to start programming in Java. Good luck!"


logAndExit
