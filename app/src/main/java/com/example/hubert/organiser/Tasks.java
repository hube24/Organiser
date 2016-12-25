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
    taskListViewAdapter adapter;


    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }

    public void setAdapter()
    {
        adapter = new taskListViewAdapter(this,R.layout.tasks_list_element,TaskList);
    }
    public void clearList(){
        TaskList.clear();
//        Log.d("tasks","clear");
    }
    public  void  updateList()
    {
        adapter.notifyDataSetChanged();
    }

    public  taskListViewAdapter getAdapter()
    {
        return  adapter;
    }

    /*      STARE SORTOWANIE
    public void sortTasks()
    {
        Collections.sort(this.TaskList, Task.Comparators.Prior);
        debug();
    }*/
    public void loadTasks(){
        DataBase db = new DataBase(getApplicationContext());
        String[] setNames = {"id","title","description","priority","day","month","year","checked"};
        Cursor el = db.getTasks(setNames);
        while (el.moveToNext()){
            Task ntask = new Task();
            ntask.setTask(el.getInt(0), el.getString(1), el.getString(2), el.getInt(3), el.getInt(4), el.getInt(5), el.getInt(6), el.getInt(7)>0 );
            addTask(ntask);
        }
        updateList();
    }

    private void debug()
    {
        for (Task t: TaskList) {
            Log.d("tasks","title="+t.getTitle()+", prior="+t.getPriority() + ", Date="+ t.getDay() + "-" + t.getMonth() + "-" + t.getYear());
        }
    }
}