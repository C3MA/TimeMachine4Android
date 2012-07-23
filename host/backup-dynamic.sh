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
 adb shell "sh data/data/de.c3ma.timemachine4android/files/store_msg.sh $@" 
}

# secure, that the binary is executable
adb shell "am broadcast -n  de.c3ma.timemachine4android/de.c3ma.timemachine4android.UpdateReceiver -a chmod"
# Make the script for generating the information executable
adb shell "chmod 777 /data/data/de.c3ma.timemachine4android/files/store_msg.sh"

# extract a globalcounter from the homefolder
if [ -f $HOME/.android-backup-counter ]; then
counter=`cat $HOME/.android-backup-counter`
else
# start with zero (because the value is incremeted automatically)
counter=0 
fi
counter=`expr $counter + 1`
echo $counter > $HOME/.android-backup-counter
formatedCounter=$(printf "%.6d" "$counter")
FOLDER=$1/${formatedCounter}at`date +%Y-%m-%d`-light/

mkdir -p $FOLDER
echo "----- Target: $FOLDER -----"
# Essential: People, SMS+MMS, Alarms
adb pull /data/data/com.android.providers.telephony/databases/mmssms.db $FOLDER
adb pull /data/data/com.android.providers.telephony/databases/telephony.db $FOLDER
adb pull /data/data/com.android.providers.contacts/databases/contacts2.db $FOLDER
adb pull /data/data/com.android.deskclock/databases/alarms.db $FOLDER
# Mail
adb pull /data/data/com.fsck.k9/databases/preferences_storage $FOLDER
adb pull /data/data/com.fsck.k9/shared_prefs/com.fsck.k9_preferences.xml $FOLDER
# NetCounter
adb pull /data/data/net.jaqpot.netcounter/databases/network.db $FOLDER
adb pull /data/data/net.jaqpot.netcounter/shared_prefs/net.jaqpot.netcounter_preferences.xml $FOLDER

sendMsg "Backuped `du -hs $FOLDER` at $HOSTNAME"
sendMsg "Space is `df -h | grep disk0`"

# move all information from the textfile to the database
adb shell "am broadcast -n  de.c3ma.timemachine4android/de.c3ma.timemachine4android.UpdateReceiver -a import"
