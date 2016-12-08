package com.example.hubert.organiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BuzekxD on 2016-12-08.
 */

public class DataBase extends SQLiteOpenHelper{
    public DataBase(Context val){
        super(val, "tasks.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks (" +
                "id integer not null primary key autoincrement," +
                "title text," +
                "description text ," +
                "priority integer not null," +
                "day integer not null," +
                "month integer not null," +
                "year integer not null);" );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){}
    public void addTask(String title, String description, int priority, int day, int month, int year){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",title);
        values.put("description",description);
        values.put("priority",priority);
        values.put("day",day);
        values.put("month",month);
        values.put("year",year);
        db.insertOrThrow("tasks",null,values);
    }
    public Cursor getTasks(){ //dodac setNames jako wartość
        String[] setNames = {"id","title","description","priority","day","month","year"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor ret = db.query("tasks",setNames,null,null,null,null,null);
        return ret;
    }
}
