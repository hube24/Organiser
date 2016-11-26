package com.example.hubert.organiser;

import java.util.Date;

/**
 * Created by Hubert on 2016-11-25.
 */

public class Task {

    public  String Title = new String();
    public  String Description = new String();
    public  int Priority;
    public  int Dday,Dmonth,Dyear;

    public Task(String ntitle, String ndescription, int nDday, int nDmonth, int nDyear , Integer npriority)
    {
        Title = ntitle;
        Description = ndescription;
        Priority = npriority;
        Dday = nDday;
        Dmonth = nDmonth;
        Dyear = nDyear;
    }

    public int getPriority() {
        return Priority;
    }

    public int getDday() {
        return Dday;
    }

    public int getDmonth() {
        return Dmonth;
    }

    public int getDyear() {
        return Dyear;
    }

}
