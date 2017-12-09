package iot.fti.fpt.rogobase.dev;

/**
 * Created by doquanghuy on 11/24/17.
 */

public class DevLog {
    private long time;
    private String message;

    public DevLog() {
    }

    public DevLog(long time, String message) {
        this.time = time;
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
