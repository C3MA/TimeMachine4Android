package de.c3ma.timemachine4android.persitance;

import java.util.Date;

/**
 * created at 22.07.2012 - 21:42:55<br />
 * creator: ollo<br />
 * project: TimeMachine4Android<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public class LogMsg {

    private Date mCreateDate;
    private String mMsg;
    
    public LogMsg(long createdTimestamp, String msg) {
        this.mCreateDate = new Date(createdTimestamp);
        this.mMsg = msg;
    }

    public Date getCreateDate() {
        return mCreateDate;
    }
    public String getMsg() {
        return mMsg;
    }
    
}
