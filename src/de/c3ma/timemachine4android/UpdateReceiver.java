package de.c3ma.timemachine4android;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import de.c3ma.timemachine4android.persitance.LoggerHelper;

/**
 * created at 23.07.2012 - 22:42:05<br />
 * creator: ollo<br />
 * project: TimeMachine4Android<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class UpdateReceiver extends BroadcastReceiver implements Constants {

    @Override
    public void onReceive(Context ctx, Intent intent) {
        String msg = intent.getExtras().getString(INTENT_MSG);
        Log.v(TAG, "Action = " + intent.getAction()  
                + " msg=" + msg);
        /* There is a message send from the backuping host, so there must something be exported */
        if (msg != null && msg.length() > 0) {
            store(ctx, msg);
        }
    }

    private void store(Context ctx, String msg) {
        /** put the old stuff into the database **/
        SQLiteDatabase dbLogger = new LoggerHelper(ctx).getWritableDatabase();
        
        if (LoggerHelper.insert(dbLogger, new Date(), msg) <= 0)
        {
            Log.e(TAG, "The message could not be stored.");
        }

        if (dbLogger != null)
            dbLogger.close();
    }

}
