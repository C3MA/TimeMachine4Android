package de.c3ma.timemachine4android;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * created at 30.06.2012 - 12:23:48<br />
 * creator: ollo<br />
 * project: SMS2fullcircle<br />
 * $Id: $<br />
 * This Service handles incoming SMS and writes them to the Host PC.
 * @author ollo<br />
 */
public class SocketServer extends Service implements Constants {

    private final SocketBinder mBinder = new SocketBinder();

    private ReceivingSocket mConnection = new ReceivingSocket(this);
    
    /**
     * All functionality, that is accessible by the visible application.
     * @author ollo
     */
    public class SocketBinder extends Binder {

        public boolean isConnected() {
            return mConnection.isConnected();
        }

        /**
         * This function is needed by the other service, that receives all SMS
         * @param msg
         * @throws IOException
         */
        public void sendMessage(String msg) throws IOException {
            if (mConnection == null)
                throw new IOException("The Communication was closed.");
            mConnection.send2Client(msg);
        }
    }

    @Override
    public IBinder onBind(Intent intentParam) {
        return mBinder;
    }
    
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        try {
            mConnection.close();
        } catch (IOException e) {
            Toast.makeText(this, TAG + " NOT Stopped: "+ e.getMessage(), 
                    Toast.LENGTH_LONG).show();    
        }
    }

    @Override
    public void onStart(Intent intentParam, int startid) {
        if (!mConnection.isAlive())
            mConnection.start();
        
        try {
            extractIncludedFile(R.raw.store_msg, "store_msg.sh", this);
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

