package com.example.smstest;

import java.math.BigDecimal;

import android.database.*;
import android.database.sqlite.*;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
 
//http://www.giantflyingsaucer.com/blog/?p=1342
public class SQLiteAssistant extends SQLiteOpenHelper
{
    private static final String DB_NAME = "usingsqlite.db";
    private static final int DB_VERSION_NUMBER = 1;
    private static final String DB_TABLE_NAME = "Money_Requests";
    private static final String DB_COLUMN_1_NAME = "contact_name";
 
    private static final String DB_CREATE_SCRIPT = "create table " + DB_TABLE_NAME +
                            " (_id integer primary key autoincrement, contact_name text not null, contact_num text not null, transaction_side text not null, amt decimal not null, description text, date_incur date not null    );)";
    
 
    private SQLiteDatabase sqliteDBInstance = null;
 
    public SQLiteAssistant(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION_NUMBER);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // TODO: Implement onUpgrade
    }
 
    @Override
    public void onCreate(SQLiteDatabase sqliteDBInstance)
    {
        Log.i("onCreate", "Creating the database...");
        sqliteDBInstance.execSQL(DB_CREATE_SCRIPT);
    }
 
    public void openDB() throws SQLException
    {
        Log.i("openDB", "Checking sqliteDBInstance...");
        if(this.sqliteDBInstance == null)
        {
            Log.i("openDB", "Creating sqliteDBInstance...");
            this.sqliteDBInstance = this.getWritableDatabase();
        }
    }
 
    public void closeDB()
    {
        if(this.sqliteDBInstance != null)
        {
            if(this.sqliteDBInstance.isOpen())
                this.sqliteDBInstance.close();
        }
    }
 
    public long insertReq(String name, String num, String trans, String amt, String desc, String date)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("contact_name", name);
        contentValues.put("contact_num",num);
        contentValues.put("transaction_side",trans);
        contentValues.put("amt",amt);
        contentValues.put("description",desc);
        contentValues.put("date_incur",date);
        
          
        Log.i(this.toString() + " - insertReq", "Inserting: " + name);
        return this.sqliteDBInstance.insert(DB_TABLE_NAME, null, contentValues);
    }
 
    public boolean removeReq(String Req)
    {
        int result = this.sqliteDBInstance.delete(DB_TABLE_NAME, "country_name='" + Req + "'", null);
 
        if(result > 0)
            return true;
        else
            return false;
    }
 
    public long updateCountry(String oldCountryName, String newCountryName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DB_COLUMN_1_NAME, newCountryName);
        return this.sqliteDBInstance.update(DB_TABLE_NAME, contentValues, "country_name='" + oldCountryName + "'", null);
    }
 
    public String[][] getAllReqs()
    {
        Cursor cursor = this.sqliteDBInstance.query(DB_TABLE_NAME, null, null, null, null, null, null);
 
        if(cursor.getCount() >0)
        {
            String[][] str = new String[cursor.getCount()][6];
            int i = 0;
 
            while (cursor.moveToNext())
            {
                 str[i][0] = cursor.getString(cursor.getColumnIndex("contact_name"));
                 str[i][1] = cursor.getString(cursor.getColumnIndex("contact_num"));
                 str[i][2] = cursor.getString(cursor.getColumnIndex("transaction_side"));
                 str[i][3] = cursor.getString(cursor.getColumnIndex("amt"));
                 str[i][4] = cursor.getString(cursor.getColumnIndex("description"));
                 str[i][5] = cursor.getString(cursor.getColumnIndex("date_incur"));
        
                 i++;
             }
            return str;
        }
        else
        {
            return new String[][] {};
        }
    }
}