package com.example.hubert.organiser;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Hubert on 2016-11-25.
 */

public class Tasks extends Application{

    List<Task> TaskList = new ArrayList<Task>();

    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }

    public void sortTasks()
    {
        Collections.sort(this.TaskList, Task.Comparators.Prior);
        debug();
    }
    
    private void debug()
    {
        for (Task t: TaskList) {
            Log.d("tasks","title="+t.Title+", prior="+t.Priority + ", Date="+ t.Dday + "-" + t.Dmonth + "-" + t.Dyear);
        }
    }
}