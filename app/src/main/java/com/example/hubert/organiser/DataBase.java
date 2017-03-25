package com.example.hubert.organiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BuzekxD on 2016-12-08.
 */


public class DataBase extends SQLiteOpenHelper{
    public DataBase(Context val){
        super(val, "tasks.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table `tips` (" +
                "id integer not null primary key autoincrement," +
                "value text," +
                "type integer not null);" );
        db.execSQL("create table tasks (" +
                "id integer not null primary key autoincrement," +
                "title text," +
                "description text ," +
                "priority integer not null," +
                "day integer not null," +
                "month integer not null," +
                "year integer not null," +
                "checked integer DEFAULT 0);" );
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
        values.put("checked",task.getChecked());
        db.insertOrThrow("tasks",null,values);
    }
    public void addTip(String value, int type){
        SQLiteDatabase db = getWritableDatabase();
        String[] setNames = {"value"};
        String[] args = {value,""+type};
        Cursor el = db.query("tips",setNames,"value=? AND type=?",args,null,null,null,null);
        if(el.getCount()<=0){
            ContentValues values = new ContentValues();
            values.put("value",value);
            values.put("type",type);
            db.insertOrThrow("tips",null,values);
        }
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
        values.put("checked",task.getChecked());
        String[] args = {""+task.getId()};
        db.update("tasks",values,"id=?",args);
    }
    public void setCheckedTask(int id){
        Task task=getTask(id);
        if(task.getChecked()==true) {
            task.setChecked(false);
            Log.d("Buzek","false");
        } else {
            task.setChecked(true);
            Log.d("Buzek","true");
        }
        changeTask(task);
    }
    public Task getTask(int id){
        Task task = new Task();
        SQLiteDatabase db = getReadableDatabase();
        String[] setNames = {"id","title","description","priority","day","month","year","checked"};
        String[] args = {""+id};
        Cursor el = db.query("tasks",setNames,"id=?",args,null,null,null,null);
        if(el!=null){
            el.moveToFirst();
            task.setId(el.getInt(0));
            task.setTitle(el.getString(1));
            task.setDescription(el.getString(2));
            task.setPriority(el.getInt(3));
            task.setDay(el.getInt(4));
            task.setMonth(el.getInt(5));
            task.setYear(el.getInt(6));
            task.setChecked(el.getInt(7)>0);
        }
        return task;
    }
    public Cursor getTasks(String[] setNames){
        SQLiteDatabase db = getReadableDatabase();
        Cursor ret = db.query("tasks",setNames,null,null,null,null,"checked, year, month, day, priority DESC",null);
        return ret;
    }
    public Cursor getTips(int type){
        SQLiteDatabase db = getReadableDatabase();
        String[] setNames = {"value"};
        String[] args = {""+type};
        Cursor ret = db.query("tips",setNames,"type=?",args,null,null,null,null);
        return ret;
    }
}