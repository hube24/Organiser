package com.example.hubert.organiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Hubert on 2016-11-25.
 */

public class Tasks {

    List<Task> TaskList = new ArrayList<Task>();

    public void addTask(Task t)
    {
        this.TaskList.add(t);
    }


   public static void Task(List<Task> TaskList) {
        Collections.sort(TaskList, new MyComp());
   }
}