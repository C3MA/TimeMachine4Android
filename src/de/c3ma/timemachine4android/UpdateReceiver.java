package de.c3ma.timemachine4android;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;

import de.c3ma.timemachine4android.persitance.LoggerHelper;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        if (intent.getAction().toLowerCase().equals("chmod")) {
            actionChmod(ctx);   
        } else if (intent.getAction().toLowerCase().equals("import")) {
            actionImport(ctx);
        }
    }

    private void actionImport(Context ctx) {
        /** put the old stuff into the database **/
        SQLiteDatabase dbLogger = new LoggerHelper(ctx).getWritableDatabase();
        
        try {
            LoggerHelper.moveInformationFromFile2DB(ctx, dbLogger, "input.txt");
        } catch (IOException e) {
            Log.e(TAG, "Could not read file " + e.getMessage());
        } catch (ParseException e) {
            Log.e(TAG, "The date could not be extracted " + e.getMessage());
        }

        if (dbLogger != null)
            dbLogger.close();
    }

    private void actionChmod(Context ctx) {
        try {
            extractIncludedFile(R.raw.store_msg, "store_msg.sh", ctx);
        } catch (Exception e) {
            Log.e(TAG, "Could not extract the script from raw (" + e.getMessage() + ")");
        }
    }
    
    public void extractIncludedFile(int resourceid, String filename, final Context ctx) throws Exception {
        InputStream is = ctx.getResources().openRawResource(resourceid);
        FileOutputStream fos = ctx.openFileOutput(filename, Context.MODE_WORLD_READABLE);
        byte[] bytebuf = new byte[1024];
        int read;
        while ((read = is.read(bytebuf)) >= 0) {
            fos.write(bytebuf, 0, read);
        }
        is.close();
        fos.getChannel().force(true);
        fos.flush();
        fos.close();
    }

}
