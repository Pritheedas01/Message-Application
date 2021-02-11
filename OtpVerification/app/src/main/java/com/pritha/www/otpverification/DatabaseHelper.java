package com.pritha.www.otpverification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase myDb;

    public static final String DATABASE_NAME = "Employee.db";
    public static final String TABLE_NAME = "EMP_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "NAME";
    public static final String COL_3 = "COMPANY";
    public static final String COL_4 = "DESIGNATION";
    public static final String COL_5 = "PHNO";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,COMPANY TEXT,DESIGNATION TEXT," +
                    "PHNO INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }




    public String getData() {
        myDb = this.getReadableDatabase();
        String[] columns=new String[] {COL_1,COL_2,COL_3};
        Cursor cursor=myDb.query(TABLE_NAME,columns,null,null,null,null,null);
        //int iId=cursor.getColumnIndex(COL_1);
        int iName= cursor.getColumnIndex(COL_2);
       // int iPrice= cursor.getColumnIndex(COL_3);
        String result="";
        for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            result=result+
                    //"ID: " +cursor.getString(iId)+"\n"+
                    //"Name: " +cursor.getString(iName)+"\n"+
                    "phno: " +cursor.getString(iName)+ "\n\n";


        }
        myDb.close();
        return result;
    }



    public boolean insertData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getListContents() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);


        return data;
    }
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});}

}

