package com.iot.fti.fpt.awscloudservice.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.iot.fti.fpt.awscloudservice.iot.AwsIotMqttConfig;

import iot.fti.fpt.rogobase.sqlite.SqliteDataType;
import iot.fti.fpt.rogobase.sqlite.SqliteTable;

/**
 * Created by doquanghuy on 10/27/17.
 */

public class AwsIotConfTable extends SqliteTable<AwsIotMqttConfig> {

    private final static String DATABASE_NAME = "";
    private final static int DATABASE_VERSION =1;

    private enum TABLEAWSCONF
    {
        ID
    }


    public AwsIotConfTable(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }


    @Override
    protected Class getTableEnumerationClass() {
        return TABLEAWSCONF.class;
    }

    @Override
    protected Enum getPrimaryColumn() {
        return TABLEAWSCONF.ID;
    }

    @Override
    protected SqliteDataType getColumnDataType(String columnName) {
        try {
            TABLEAWSCONF column = TABLEAWSCONF.valueOf(columnName);
            switch (column)
            {
            }
        }catch (Exception e)
        {

        }

        return null;
    }

    @Override
    protected ContentValues conversionToContentValues(AwsIotMqttConfig awsIotMqttConfig, String[] colums) {
        return null;
    }

    @Override
    protected AwsIotMqttConfig conversionToObject(Cursor c, String[] colums) {
        return null;
    }
}
