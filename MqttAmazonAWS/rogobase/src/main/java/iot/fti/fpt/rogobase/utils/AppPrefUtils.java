package iot.fti.fpt.rogobase.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by doquanghuy on 3/28/17.
 */

public class AppPrefUtils {

    private SharedPreferences sharedpreferences;

    public AppPrefUtils(Context context,String prefName)
    {
        sharedpreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    }

    public void removePropertie(String propertie)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(propertie);
    }


    public String getString(String propertie)
    {
        return sharedpreferences.getString(propertie,null);
    }

    public String getString(String propertie, String defaultValue)
    {
        return sharedpreferences.getString(propertie,defaultValue);
    }

    public void setString(String propertie, String value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(propertie, value);
        editor.commit();
    }

    public int getInt(String propertie)
    {
        return sharedpreferences.getInt(propertie,-1);
    }

    public int getInt(String propertie, int defaultValue)
    {
        return sharedpreferences.getInt(propertie,defaultValue);
    }

    public void setInt(String propertie, int value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(propertie, value);
        editor.commit();
    }

    public long getLong(String propertie)
    {
        return sharedpreferences.getLong(propertie,-1);
    }

    public long getLong(String propertie, long defaultValue)
    {
        return sharedpreferences.getLong(propertie,defaultValue);
    }

    public void setLong(String propertie, long value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putLong(propertie, value);
        editor.commit();
    }




    public float getFloat(String propertie)
    {
        return sharedpreferences.getFloat(propertie,-1);
    }

    public float getFloat(String propertie, float defaultValue)
    {
        return sharedpreferences.getFloat(propertie,defaultValue);
    }
    public void setFloat(String propertie, float value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(propertie, value);
        editor.commit();
    }


    public boolean getBoolean(String propertie)
    {
        return sharedpreferences.getBoolean(propertie,false);
    }

    public boolean getBoolean(String propertie, boolean defaultValue)
    {
        return sharedpreferences.getBoolean(propertie,defaultValue);
    }
    public void setBoolean(String propertie, boolean value)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(propertie, value);
        editor.commit();
    }
}
