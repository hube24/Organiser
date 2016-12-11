package com.example.hubert.organiser;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by Hubert on 2016-11-25.
 *
 * dodane inty do obslugi DataBase
 */

public class Task{
    private int id;
    private String title = new String();
    private String description = new String();
    private int priority;
    private int day;
    private int month;
    private int year;

    public void setTask(int nid, String ntitle, String ndescription, int npriority, int nday, int nmonth, int nyear )
    {
        id = nid;
        title = ntitle;
        description = ndescription;
        priority = npriority;
        day = nday;
        month = nmonth;
        year = nyear;
    }
    public int getId() {return id;}
    public void setId(int id){this.id = id;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}
    public String getDescription(){return description;}
    public void setDescription(String description){this.description = description;}
    public int getPriority(){return priority;}
    public void setPriority(int priority){this.priority = priority;}
    public int getDay(){return day;}
    public void setDay(int day){this.day = day;}
    public int getMonth(){return month;}
    public void setMonth(int month){this.month = month;}
    public int getYear(){return year;}
    public void setYear(int year){this.year = year;}


    public static class Comparators {

        public static Comparator<Task> Prior = new Comparator<Task>() {

            @Override
            public int compare(Task obfirst, Task obsecond) {
                int i = obfirst.getYear() - obsecond.getYear();
                if (i == 0) {
                    i = obfirst.getMonth() - obsecond.getMonth();
                    if(i==0){
                        i = obfirst.getDay() - obsecond.getDay();
                        if(i==0){
                            i= obsecond.getPriority() - obfirst.getPriority();
                        }
                    }
                }
                return i;
            }

        };
    }

}
