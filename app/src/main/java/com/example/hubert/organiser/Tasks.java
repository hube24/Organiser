package com.example.hubert.organiser;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Hubert on 2016-11-25.
 */

public class Tasks extends Application{


    List<Task> TaskList = new ArrayList<Task>();
    taskListViewAdapter adapter;
    List<Task> HistoryTaskList = new ArrayList<>();
    historyTaskListViewAdapter historyAdapter;

    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }

    public void setAdapter()
    {
        adapter = new taskListViewAdapter(this, R.layout.tasks_list_element, TaskList);
        historyAdapter = new historyTaskListViewAdapter(this, R.layout.history_task_list_element, HistoryTaskList);
    }
    public void clearList(){
        TaskList.clear();
        HistoryTaskList.clear();
//        Log.d("tasks","clear");
    }
    public void updateList()
    {
        adapter.notifyDataSetChanged();
        historyAdapter.notifyDataSetChanged();
    }

    public  taskListViewAdapter getAdapter()
    {
        return  adapter;
    }
    public historyTaskListViewAdapter getHistoryAdapter()
    {
        return historyAdapter;
    }

    public void profilingList(){
        for (final ListIterator<Task> i = TaskList.listIterator(); i.hasNext();) {
            Task el = i.next();
            el.setPval(0);
            try {
                int expecttime = (new JSONObject(el.getDetails())).getInt("Oczekiwany_czas");
                int t=el.getPriority();
                t=(expecttime*4);
                Log.d("expecttime",el.getPriority()+"p + "+t+"t("+expecttime+")");
                el.setPval(el.getPriority()+t);
            } catch (Exception e){}
            if(el.getPval()==0)
                el.setPval(el.getPriority()*2);
            Log.d("expecttime",el.getPval()+"pval");
            i.set(el);
        }
        Collections.sort(this.TaskList, Task.Comparators.Prior);
    }
    public void loadTasks(){
        DataBase db = new DataBase(getApplicationContext());
        String[] setNames = {"id","title","description","priority","day","month","year","time","details","checked"};
        Cursor el = db.getTasks(setNames);
        while (el.moveToNext()){
            Task ntask = new Task();
            ntask.setTask(el.getInt(0), el.getString(1), el.getString(2), el.getInt(3), el.getInt(4), el.getInt(5), el.getInt(6), el.getInt(7), el.getString(8), el.getInt(9)>0 );

            if(ntask.getChecked()){
                this.HistoryTaskList.add(ntask);
            }else{
                this.TaskList.add(ntask);
            }
        }
        profilingList();
        updateList();
    }

    private void debug()
    {
        for(Task t: TaskList) {
            Log.d("tasks","title="+t.getTitle()+", prior="+t.getPriority() + ", Date="+ t.getDay() + "-" + t.getMonth() + "-" + t.getYear());
        }
    }
}