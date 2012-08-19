package de.c3ma.timemachine4android;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import de.c3ma.timemachine4android.list.MessageListCursorAdapter;
import de.c3ma.timemachine4android.persitance.LoggerHelper;

public class TimeMachine4AndroidActivity extends ListActivity implements Constants {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = new LoggerHelper(this).getReadableDatabase();
        Cursor cur = LoggerHelper.getCursorAllMessages(db);
        setListAdapter(new MessageListCursorAdapter(this, R.layout.message_item, cur));
        db.close();
    }
}