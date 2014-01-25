package com.dexter.mtnodometertracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "mtndriversodometer.db";
	public String error = "";
	
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, 1);
	}
	
    @Override
    public void onCreate(SQLiteDatabase db)
    {
    	try
    	{
	        db.execSQL("CREATE TABLE user (_id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, fullname TEXT, ACTIVE INTEGER);");
	        db.execSQL("CREATE TABLE trackRecord (_id INTEGER PRIMARY KEY, tranTime TEXT, initFuelLvl REAL, quantity REAL, finalFuelLvl REAL, odometer REAL, fuelRate REAL, cost REAL);");
	        
	        loadDefaultContent();
	        error = "success";
    	}
    	catch(Exception ex)
    	{
    		error = ex.getMessage();
    	}
    }
    
    private void loadDefaultContent()
    {
    	SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", "administrator");
        cv.put("password", "dexter");
        cv.put("fullname", "Dele Olaore");
        db.insert("user", "_id", cv);
        
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        android.util.Log.v("Constants",
                "Upgrading database which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS trackRecord");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}
