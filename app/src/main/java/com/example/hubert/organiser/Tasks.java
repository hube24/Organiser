package com.example.hubert.organiser;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hubert on 2016-11-25.
 */

public class Tasks extends Application{

    private  List <Task> TasksList = new ArrayList<Task>();
    public void addTask(Task t)
    {
        this.TasksList.add(t);
    }


    //TODOSave to xml


    //TODOLoad from xml


    //TODOSort by priority

}
