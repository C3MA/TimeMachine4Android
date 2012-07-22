#!/bin/bash
# android-light-backup.sh
# For restoreing all backuped files must be pushed back to the device via adb
# T H I S  S C R I P T  W A S  B U I L D  F O R  A N D R O I D  2.*  D E V I C E S

if [ $# -ne 1 ]; then
 echo -e "Usage $0 <Backup target path>\t(Will be created if not exists)"
 exit 1
fi

adb devices | grep -v devices | grep device >> /dev/null
if [ $? -ne 0 ]; then
 echo "No Device was added"
 exit 1
fi

function sendMsg {
 echo "msg=$1" | nc localhost 1234 
}

# create a forwarding of TCP packages
adb forward tcp:1234 tcp:1234
# start the service on the device
adb shell "am startservice -n de.c3ma.timemachine4android/de.c3ma.timemachine4android.SocketServer"

sendMsg "Hello World `date`"

echo "quit" | nc localhost 1234
