package com.example.hubert.organiser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Hubert on 2016-11-25.
 */

class MyComparator implements Comparator<Task> {

    @Override
    public int compare(Task obfirst, Task obsecond) {
        if (obfirst.getPriority() > obsecond.getPriority()) {
            return -1;
        } else if (obfirst.getPriority() < obsecond.getPriority()) {
            return 1;
        } else if (obfirst.getDyear() < obsecond.getDyear()){
            return 1;
        }else if(obfirst.getDyear() > obsecond.getDyear()){
            return -1;
        }else if(obfirst.getDmonth() < obsecond.getDmonth()) {
            return 1;
        } else if(obfirst.getDmonth() > obsecond.getDmonth()){
            return -1;
        }else if(obfirst.getDday() < obsecond.getDday()){
            return 1;
        }else if(obfirst.getDday() > obsecond.getDday()) {
            return -1;
        }

        return 0;
    }
}

public class Tasks {

    public static void Task(String[] args) {
        List<Task> TaskList = new ArrayList<Task>();
        TaskList.add(new Task("FC Barcelona", "ops", 1, 1, 1, 1));
        TaskList.add(new Task("Arsenal FC", "w?", 1, 2, 3, 4));
        TaskList.add(new Task("Chelsea", "sss", 1, 2, 3, 2));

        Collections.sort(TaskList, new MyComparator());

        for (Task obie : TaskList) {
            System.out.println(obie.Title + ": " + obie.Priority + " PRIORYTET");
        }

    }

}