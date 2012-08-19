package de.c3ma.timemachine4android;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.c3ma.timemachine4android.list.MessageListCursorAdapter;
import de.c3ma.timemachine4android.persitance.LoggerHelper;

public class LastActionListActivity extends ListActivity implements Constants {
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_logs);
    }
    
    @Override
    protected void onResume() {
        super.onResume();

        SQLiteDatabase db = new LoggerHelper(this).getReadableDatabase();
        Cursor cur = LoggerHelper.getCursorLastMessages(db);
        setListAdapter(new MessageListCursorAdapter(this, R.layout.message_item, cur));
        db.close();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lastaction, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.show_all_logs:
                Intent intent = new Intent(this, AllLogsListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}