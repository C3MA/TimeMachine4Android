package de.c3ma.timemachine4android;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

import android.app.Service;
import android.util.Log;

/**
 * created at 08.07.2012 - 13:50:59<br />
 * creator: ollo<br />
 * project: SMS2fullcircle<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class ReceivingSocket extends Thread implements Constants {
    private Socket client = null;
    private Scanner socketIn = null;
    private ServerSocket mServer = null;
    private PrintWriter mSocketOut = null;
    private Service mParentService;
    
    /**
     * 
     * @param parentService service, that opens this socket.
     * (Needed for sending Broadcast)
     */
    public ReceivingSocket(final Service parentService) {
        this.mParentService = parentService;
    }
    
    public void run() {
        try {
            // initialize server socket
            mServer = new ServerSocket(PORT);
            Log.d(TAG, "Server created");
        } catch (SocketTimeoutException e) {
            // print out TIMEOUT
            String connectionStatus = "Connection has timed out! Please try again";
            Log.e(TAG, connectionStatus);
            mServer = null; //there was an error, so don't wait for clients
        } catch (IOException e) {
            Log.e(TAG, "" + e);
            mServer = null; //there was an error, so don't wait for clients
        }
        
        /* as long we have a connection, we will try to send SMS data */
        while (mServer != null && !mServer.isClosed()) {
            Log.d(TAG, "Wait for a client");
            try {
                // attempt to accept a connection
                client = mServer.accept();
                socketIn = new Scanner(client.getInputStream());
                mSocketOut = new PrintWriter(client.getOutputStream(), true);
                    receiverData();
            } catch (SocketTimeoutException e) {
                // print out TIMEOUT
                String connectionStatus = "Connection has timed out! Please try again";
                Log.e(TAG, connectionStatus);
            } catch (IOException e) {
                Log.d(TAG, "Exception: " + e.getMessage() );
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        String connectionStatus = "------- Connection Thread has ended --------";
        Log.d(TAG, connectionStatus);

        // close the server socket
        try {
            if (mServer != null)
                mServer.close();
        } catch (IOException ec) {
            Log.e(TAG, "Cannot close server socket" + ec);
        } 
    }

    private void receiverData() throws IOException {
        if (client != null) {
            String socketData = "";
            // print out success
            String connectionStatus = "connected!";
            Log.d(TAG, connectionStatus);
            
            while (!client.isClosed() && socketIn.hasNext()) {
                socketData = socketIn.nextLine();
                if (socketData.startsWith(NET_MSG)) {
                    String msg = socketData.substring(NET_MSG.length());
                    Log.i(TAG, msg);
                } else if (socketData.startsWith(NET_QUIT)) {
                    send2Client("Bye bye");
                    client.close();
                    mServer.close();
                    mParentService.stopSelf();
                } else {
                    send2Client(NET_FAIL);
                }
                
                socketData = "";
            }
        }
    }
    
    public synchronized void send2Client(String line) {
        mSocketOut.println(line);
    }
    
    public void close() throws IOException {
        mServer.close();
    }
    
    public boolean isConnected() {
        if (mServer == null)
            return false;
        return (mSocketOut != null);
    }

    public boolean isBound() {
        if (mServer == null)
            return false;
        return mServer.isBound();
    }
}
