package de.c3ma.timemachine4android.persitance;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * created at 22.07.2012 - 21:34:54<br />
 * creator: ollo<br />
 * project: TimeMachine4Android<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class LoggerHelper extends SQLiteOpenHelper implements DBConstants {

    
    public static final String TABLE_NAME = "messages";
    static final String TABLE_CREATE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                LOG_ID + " INTEGER PRIMARY KEY, " +
                LOG_DATE + " created_date date, " +
                LOG_MSG + " TEXT);";
    
    public LoggerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

    public static long insert(SQLiteDatabase db, final String msg) {
     // set the format to sql date time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        Date date = new Date();
        ContentValues values = new ContentValues();
        values.put(LOG_DATE, dateFormat.format(date));
        values.put(LOG_MSG, msg);
        return (db.insert(TABLE_NAME, null, values ));
    }
    
    /**
     * 
     * @param db
     * @return list of found messages.
     */
    public static LogMsg[] searchAllMessages(SQLiteDatabase db) {
        final String selection = null;
        final String[] selectionArgs = null;
        Cursor c = db.query(true, TABLE_NAME, new String[] { LOG_DATE, LOG_MSG }, selection, selectionArgs, null, null,
                null, null);
        LogMsg[] list = new LogMsg[c.getCount()];
        for (int i = 0; i < list.length; i++) {
            if (!c.moveToNext()) /* this code should never be reached, because it is ended via the c.getCount() */
                return new LogMsg[0];
            list[i] = new LogMsg(c.getLong(c.getColumnIndex(LOG_DATE)), c.getString(c.getColumnIndex(LOG_MSG)));
        }
        return list;
    }
    
    public static Cursor getCursorAllMessages(SQLiteDatabase db) {
        final String selection = null;
        final String[] selectionArgs = null;
        return db.query(true, TABLE_NAME, new String[] { LOG_DATE, LOG_MSG }, selection, selectionArgs, null, null,
                null, null);
        
    }
}
