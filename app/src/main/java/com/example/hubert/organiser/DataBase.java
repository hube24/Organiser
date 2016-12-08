package com.example.hubert.organiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    public void addTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",task.getTitle());
        values.put("description",task.getDescription());
        values.put("priority",task.getPriority());
        values.put("day",task.getDay());
        values.put("month",task.getMonth());
        values.put("year",task.getYear());
        db.insertOrThrow("tasks",null,values);
    }
    public void deleteTask(int id){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {""+id};
        db.delete("tasks","id=?",args);
        Log.d("DataBase","Deleted id="+id);
    }
    public void changeTask(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title",task.getTitle());
        values.put("description",task.getDescription());
        values.put("priority",task.getPriority());
        values.put("day",task.getDay());
        values.put("month",task.getMonth());
        values.put("year",task.getYear());
        String[] args = {""+task.getId()};
        db.update("tasks",values,"id=?",args);
    }
    public Task getTask(int id){
        Task task = new Task();
        SQLiteDatabase db = getReadableDatabase();
        String[] setNames = {"id","title","description","priority","day","month","year"};
        String[] args = {""+id};
        Cursor el = db.query("tasks",setNames,"nr=?",args,null,null,null,null);
        if(el!=null){
            el.moveToFirst();
            task.setId(el.getInt(0));
            task.setTitle(el.getString(1));
            task.setDescription(el.getString(2));
            task.setPriority(el.getInt(3));
            task.setDay(el.getInt(4));
            task.setMonth(el.getInt(5));
            task.setYear(el.getInt(6));
        }
        return task;
    }
    public Cursor getTasks(){ //dodac setNames jako wartość
        String[] setNames = {"id","title","description","priority","day","month","year"};
        SQLiteDatabase db = getReadableDatabase();
        Cursor ret = db.query("tasks",setNames,null,null,null,null,null);
        return ret;
    }
}
