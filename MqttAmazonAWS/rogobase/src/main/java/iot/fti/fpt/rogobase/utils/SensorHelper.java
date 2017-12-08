package iot.fti.fpt.rogobase.utils;

/**
 * Created by doquanghuy on 10/26/17.
 */

public class SensorHelper {


    public static int[] readTempHumid(byte[] scandRecord)
    {
        return null;
    }

    public static int byte_array_to_int16_t(byte[] a_b)
    {
        int ret = a_b[0] * 256;
        ret += ((int) a_b[1]) & 0xff;
        return ret;
    }

}
