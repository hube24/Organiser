package com.example.hubert.organiser;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;

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
    private int ProfilId= 1;
    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }
    public void setProfilId(int ProfilId){this.ProfilId=ProfilId;};

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
            switch(ProfilId){
                case 1:
                    el.setPval(0);
                    try {
                        int expecttime = (new JSONObject(el.getDetails())).getInt("Oczekiwany_czas");
                        int t=(expecttime*4);
                        Log.d("expecttime",el.getPriority()+"p + "+t+"t("+expecttime+")");
                        el.setPval(el.getPriority()+t);
                    } catch (Exception e){}
                    if(el.getPval()==0)
                        el.setPval(el.getPriority()*2);
                    Log.d("expecttime1",el.getPval()+"pval");
                    i.set(el);
                    break;
                case 2:
                    el.setPval(0);
                    try {
                        int expecttime = (new JSONObject(el.getDetails())).getInt("Oczekiwany_czas");
                        int t=(expecttime*4);
                        el.setPval(10-el.getPriority()+t);
                    } catch (Exception e){}
                    if(el.getPval()==0)
                        el.setPval(20-el.getPriority()*2);
                    Log.d("expecttime2",el.getPval()+"pval");
                    i.set(el);
                    break;
            }
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