package iot.fti.fpt.rogobase.utils;

/**
 * Created by doquanghuy on 10/18/16.
 */
public class DataConversionUtil {



    public static byte[] hexStringToByteArray(String hex) {
        if(hex.length()%2>0)
        {
            hex="0"+hex;
        }

        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i+1), 16));
        }
        return data;
    }

    public static int bytesToInt(byte[] bytes) {
        if(bytes.length==2)
        {
            int i = ((bytes[0] & 0xff) | (short) (bytes[1] << 8));
            return i;
        }else return 0;
    }

    public static int byteToInt(byte b) {
        return (int) b;
    }
}
