package de.c3ma.timemachine4android.persitance;

/**
 * created at 22.07.2012 - 21:35:22<br />
 * creator: ollo<br />
 * project: TimeMachine4Android<br />
 * $Id: $<br />
 * @author ollo<br />
 */
public interface DBConstants {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "logs";
    
    public static final String LOG_ID = "id";
    public static final String LOG_DATE = "date";
    public static final String LOG_MSG = "msg";
}
