package iot.fti.fpt.rogobase.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by doquanghuy on 4/16/17.
 */

public class TimeConversionUtil {
    private SimpleDateFormat format;


    public TimeConversionUtil(SimpleDateFormat format)
    {
        this.format=format;
    }

    public TimeConversionUtil(String timeFormat)
    {
        format = new SimpleDateFormat(timeFormat);
    }
    public TimeConversionUtil()
    {
        format = new SimpleDateFormat("dd-MM-yyyy");
    }

    public String getCurrentDay(long time)
    {
        return getCurrentDay(new Date(time));
    }

    public String getCurrentDay(Date date)
    {
        return format.format(date);
    }

    public String[] getTime(Date date)
    {
        return format.format(date).split("//");
    }

    public static long getMinutesDiff(long start,long end)
    {
        long diff = end - start;
        return  diff / (60 * 1000) % 60;
    }

    public static long getSecondsDiff(long start,long end)
    {
        long diff = end - start;
        return diff / 1000 % 60;
    }

    public static long getHourssDiff(long start,long end)
    {
        long diff = end - start;
        return diff / (60 * 60 * 1000);
    }
}
