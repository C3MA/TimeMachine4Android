package de.c3ma.timemachine4android.list;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import de.c3ma.timemachine4android.R;
import de.c3ma.timemachine4android.persitance.DBConstants;

/**
 * created at 22.07.2012 - 22:10:32<br />
 * creator: ollo<br />
 * project: TimeMachine4Android<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class MessageListCursorAdapter extends SimpleCursorAdapter {

    private int layout;
    
    private static final String[] displayFields = new String[] { DBConstants.LOG_DATE ,DBConstants.LOG_MSG };
    private static final int[] displayViews = new int[] { R.id.msg_date, R.id.msg_text };
    
    public MessageListCursorAdapter(Context context, int layout, Cursor c) {
        super(context, layout, c, displayFields, displayViews);        
        this.layout = layout;
    }
    
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Cursor c = getCursor();

        final LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(layout, parent, false);

        /* fill here the data */
        int dateCol = c.getColumnIndex(DBConstants.LOG_DATE);
        String date = c.getString(dateCol);
        TextView tvDate = (TextView) v.findViewById(R.id.msg_date);
        if (tvDate != null) {
            tvDate.setText(date);
        }
        
        int msgCol = c.getColumnIndex(DBConstants.LOG_MSG);
        TextView tvMsg = (TextView) v.findViewById(R.id.msg_text);
        String msg = c.getString(msgCol);
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
        int dateCol = c.getColumnIndex(DBConstants.LOG_DATE);
        String date = c.getString(dateCol);
        TextView tvDate = (TextView) v.findViewById(R.id.msg_date);
        if (tvDate != null) {
            tvDate.setText(date);
        }
        
        int msgCol = c.getColumnIndex(DBConstants.LOG_MSG);
        TextView tvMsg = (TextView) v.findViewById(R.id.msg_text);
        String msg = c.getString(msgCol);
        if (tvMsg != null) {
            tvMsg.setText(msg);
        }
    }
}
