package com.example.hubert.organiser;

/**
 * Created by filip on 27.11.2016.
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MyComp {

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