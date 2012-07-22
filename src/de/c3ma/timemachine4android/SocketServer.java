package de.c3ma.timemachine4android;

import java.io.IOException;

import android.app.Service;
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

    private ReceivingSocket mConnection = new ReceivingSocket();
    
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
    }
}

