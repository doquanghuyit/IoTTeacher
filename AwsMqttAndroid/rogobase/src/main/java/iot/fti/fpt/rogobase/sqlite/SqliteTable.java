package iot.fti.fpt.rogobase.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by doquanghuy on 6/8/17.
 */

public abstract class SqliteTable<T> extends SQLiteOpenHelper {

    public SqliteTable(Context context,
                       String DATABASE_NAME,
                       int DATABASE_VERSION)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String[] columns;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(queryCreateTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + getTableName());
        onCreate(sqLiteDatabase);
    }

//    abstract protected String getTableName();
    abstract protected Class getTableEnumerationClass();
    abstract protected Enum getPrimaryColumn();
    abstract protected SqliteDataType getColumnDataType(String columnName);
    abstract protected ContentValues conversionToContentValues(T t, String[] columns);
    abstract protected T conversionToObject(Cursor c, String[] columns);



    protected String[] getTableColumn()
    {
        if(columns == null)
        {
            Object[] arr = getTableEnumerationClass().getEnumConstants();
            columns = new String[arr.length];
            for(int i = 0;i <arr.length;i++)
            {
                columns[i]= arr[i].toString();
            }
        }
        return columns;
    }
    private String queryCreateTable()
    {
        String CREATE_TABLE_QUERY ="CREATE TABLE "+ getTableName() + "(";
        Object[] arr = getTableEnumerationClass().getEnumConstants();
        for(Object e: arr)
        {
            if(((Enum) e).equals(getPrimaryColumn()))
            {
                CREATE_TABLE_QUERY+= e.toString()+" "+ getColumnDataType(e.toString()).toString()+" PRIMARY KEY"+",";
            }else
            {
                CREATE_TABLE_QUERY+= e.toString()+" "+ getColumnDataType(e.toString()).toString()+",";
            }
        }
        CREATE_TABLE_QUERY=CREATE_TABLE_QUERY.substring(0,CREATE_TABLE_QUERY.length()-1)+")";
        return CREATE_TABLE_QUERY;
    }

    protected String getTableName()
    {
        return getTableEnumerationClass().getSimpleName();
    }


    public T get(long id)
    {
        Cursor c = this.getReadableDatabase().rawQuery(genQuerySelectItemById(id), null);
        return getObject(c,getTableColumn());
    }

    public T get(int id)
    {
        Cursor c = this.getReadableDatabase().rawQuery(genQuerySelectItemById(id), null);
        return getObject(c,getTableColumn());
    }
    public T get(String id)
    {
        Cursor c = this.getReadableDatabase().rawQuery(genQuerySelectItemById(id), null);
        return getObject(c,getTableColumn());
    }

    public void insert(T t)
    {
        try {
            ContentValues contentValues = conversionToContentValues(t,getTableColumn());
            this.getWritableDatabase().insert(getTableName(), null, contentValues);
        }catch (Exception e)
        {
        }
    }

    private String[] mergeWithColumnPrimary(String[] columns)
    {
        String[] columnsMerge = new String[columns.length+1];
        for(int i = 0; i< columns.length;i++)
        {
            columnsMerge[i] = columns[i];
        }
        columnsMerge[columns.length] = getPrimaryColumn().toString();
        return columnsMerge;
    }

    public void update(String id,T t, String[] columns)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = conversionToContentValues(t, mergeWithColumnPrimary(columns));
            db.update(getTableName(),contentValues, getPrimaryColumn() + " = \"" + id+"\"",null);
        }catch (Exception e)
        {
            Log.e(this.getClass().getSimpleName(),"update 2:: RogoBleInfo Exception::",e);
        }
    }

    public void update(int id,T t, String[] columns)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = conversionToContentValues(t, mergeWithColumnPrimary(columns));
            db.update(getTableName(),contentValues, getPrimaryColumn() + " = "+id,null);
        }catch (Exception e)
        {
            Log.e(this.getClass().getSimpleName(),"update 2:: RogoBleInfo Exception::",e);
        }
    }

    public void update(long id,T t, String[] columns)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = conversionToContentValues(t,mergeWithColumnPrimary(columns));
            db.update(getTableName(),contentValues, getPrimaryColumn() + " = "+id,null);
        }catch (Exception e)
        {
            Log.e(this.getClass().getSimpleName(),"update 2:: RogoBleInfo Exception::",e);
        }
    }

    public void updateOverConditionQuery(T t,String conditionQuery, String[] columns)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = conversionToContentValues(
                t, mergeWithColumnPrimary(columns));
        db.update(getTableName(),contentValues, conditionQuery,null);
    }

    public boolean delete(String id)
    {
        try {
            this.getWritableDatabase().delete(getTableName(), getPrimaryColumn() + " = ?",
                    new String[] { String.valueOf(id) });
            return get(id) == null;
        }catch (Exception e)
        {
            return false;
        }
    }

    public boolean delete(int id)
    {
        try {
            this.getWritableDatabase().delete(getTableName(), getPrimaryColumn() + " = ?",
                    new String[] { String.valueOf(id) });
            return get(id) == null;
        }catch (Exception e)
        {
            return false;
        }
    }

    public boolean delete(long id)
    {
        try {
            this.getWritableDatabase().delete(getTableName(), getPrimaryColumn() + " = ?",
                    new String[] { String.valueOf(id) });
            return get(id) == null;
        }catch (Exception e)
        {
            return false;
        }
    }

    public void deleteOverConditionQuery(String conditionQuery)
    {
        this.getWritableDatabase().delete(getTableName(),conditionQuery,null);
    }

    public boolean isExists(String id)
    {
        return get(id) != null;
    }

    public boolean isExists(int id)
    {
        return get(id) != null;
    }

    public boolean isExists(long id)
    {
        return get(id) != null;
    }


    public ArrayList<T> getItems(String query)
    {
        Cursor c = this.getReadableDatabase().rawQuery(query, null);
        ArrayList<T> items = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // adding to todo list
                items.add(conversionToObject(c,getTableColumn()));
            } while (c.moveToNext());
        }
        return items;
    }

    public ArrayList<T> getAllItems()
    {
        Cursor c = this.getReadableDatabase().rawQuery(genQueryGetAll(), null);
        ArrayList<T> items = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                // adding to todo list
                items.add(conversionToObject(c,getTableColumn()));
            } while (c.moveToNext());
        }
        return items;
    }





    protected String genQuerySelectItemById(String id)
    {
        return  "SELECT  * FROM " + getTableName() +" WHERE "+ getPrimaryColumn()+ " = \"" + id+"\"";
    }

    protected String genQuerySelectItemById(long id)
    {
        return  "SELECT  * FROM " + getTableName() +" WHERE "+ getPrimaryColumn()+ " = "+id;
    }

    protected String genQuerySelectItemById(int id)
    {
        return  "SELECT  * FROM " + getTableName() +" WHERE "+ getPrimaryColumn()+ " = "+id;
    }

    protected String genConditonById(String id)
    {
        return  getPrimaryColumn() + " = \"" + id+"\"";
    }

    protected String genConditonById(long id)
    {
        return  getPrimaryColumn() + " = "+id;
    }

    protected String genConditonById(int id)
    {
        return  getPrimaryColumn() + " = "+id;
    }


    public String genQueryGetAll()
    {
        return  "SELECT  * FROM " + getTableName() ;
    }

    protected int getInt(Cursor c,Enum column)
    {
        return c.getInt(c.getColumnIndex(column.toString()));
    }

    protected long getLong(Cursor c,Enum column)
    {
        return  c.getLong(c.getColumnIndex(column.toString()));
    }
    protected String getString(Cursor c,Enum column)
    {
        return  c.getString(c.getColumnIndex(column.toString()));
    }

    private T getObject(Cursor c, String[] columns)
    {
        if (c != null)
        {
            if(c.getCount()==1)
            {
                c.moveToFirst();
                return conversionToObject(c,columns);
            }else return null;
        }else return null;
    }

}
