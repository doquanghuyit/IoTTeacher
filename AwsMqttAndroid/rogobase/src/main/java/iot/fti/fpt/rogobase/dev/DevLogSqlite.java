package iot.fti.fpt.rogobase.dev;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import iot.fti.fpt.rogobase.sqlite.SqliteDataType;
import iot.fti.fpt.rogobase.sqlite.SqliteTable;

/**
 * Created by doquanghuy on 11/24/17.
 */

public class DevLogSqlite extends SqliteTable<DevLog> {
    private static final String DATABASE_NAME="DevLogSqlite_ver1";
    private static final int DATABASE_VERSION =1;
    private enum DevLogTable
    {
        TIME,MESSAGE
    }

    public DevLogSqlite(Context context) {
        super(context, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    protected Class getTableEnumerationClass() {
        return DevLogTable.class;
    }

    @Override
    protected Enum getPrimaryColumn() {
        return DevLogTable.TIME;
    }

    @Override
    protected SqliteDataType getColumnDataType(String columnName) {

        try {
            DevLogTable devLogTable = DevLogTable.valueOf(columnName);
            switch (devLogTable)
            {
                case TIME:
                    return SqliteDataType.INTEGER;
                case MESSAGE:
                    return SqliteDataType.TEXT;
                default:
                    return SqliteDataType.TEXT;

            }
        }catch (Exception e)
        {
            return SqliteDataType.TEXT;
        }
    }

    @Override
    protected ContentValues conversionToContentValues(DevLog devLog, String[] columns) {
        ContentValues contentValues = new ContentValues();
        for (String column : columns)
        {
            DevLogTable columnTable = DevLogTable.valueOf(column);
            switch (columnTable)
            {
                case TIME:
                    contentValues.put(column,devLog.getTime());
                    break;
                case MESSAGE:
                    contentValues.put(column,devLog.getMessage());
                    break;
            }

        }
        return contentValues;
    }

    @Override
    protected DevLog conversionToObject(Cursor c, String[] columns) {
        DevLog devLog = new DevLog();

        for (String column : columns)
        {
            DevLogTable columnTable = DevLogTable.valueOf(column);
            switch (columnTable)
            {
                case TIME:
                    devLog.setTime(getInt(c,columnTable));
                    break;
                case MESSAGE:
                    devLog.setMessage(getString(c,columnTable));
                    break;
            }

        }
        return devLog;
    }
}
