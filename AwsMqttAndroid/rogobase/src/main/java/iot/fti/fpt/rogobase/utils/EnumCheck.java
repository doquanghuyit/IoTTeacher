package iot.fti.fpt.rogobase.utils;

/**
 * Created by doquanghuy on 10/8/17.
 */

public class EnumCheck {
    public static boolean contains(Class<? extends Enum> clazz,String val)
    {
        Object[] arr = clazz.getEnumConstants();
        for(Object e: arr)
        {
            if(((Enum) e).name().equals(val))
            {
                return true;
            }
        }
        return false;
    }
}
