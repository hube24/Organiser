package com.example.hubert.organiser;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Hubert on 2016-11-25.
 */

public class Tasks extends Application{


    List<Task> TaskList = new ArrayList<Task>();
    ArrayList<String> TaskNames = new ArrayList<>();
    ArrayAdapter<String> adapter;


    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }

    public void setAdapter()
    {
        adapter = new ArrayAdapter<String>(this, R.layout.tasks_list_element, R.id.textViewElement ,TaskNames);
    }
    public void clearList(){
        TaskList.clear();
//        Log.d("tasks","clear");
    }
    public  void  updateList()
    {
        TaskNames.clear();

        for (Task t:this.TaskList) {
            TaskNames.add(t.getTitle());
        }

        adapter.notifyDataSetChanged();
    }

    public  ArrayAdapter<String> getAdapter()
    {
        return  adapter;
    }

    public void sortTasks()
    {
        Collections.sort(this.TaskList, Task.Comparators.Prior);
//        debug();
    }
    public void loadTasks(){
        DataBase db = new DataBase(getApplicationContext());
        Cursor el = db.getTasks();
        while (el.moveToNext()){
            Task ntask = new Task();
            ntask.setTask(el.getInt(0), el.getString(1), el.getString(2), el.getInt(3), el.getInt(4), el.getInt(5), el.getInt(6) );
            addTask(ntask);
        }
        sortTasks();
        updateList();
        db.close();
    }
    
    private void debug()
    {
        for (Task t: TaskList) {
            Log.d("tasks","title="+t.getTitle()+", prior="+t.getPriority() + ", Date="+ t.getDay() + "-" + t.getMonth() + "-" + t.getYear());
        }
    }
}