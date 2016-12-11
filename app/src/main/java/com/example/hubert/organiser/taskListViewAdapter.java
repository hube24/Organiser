
package com.example.hubert.organiser;

import android.content.ClipData;
import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Hubert on 2016-12-09.
 *
 * Customowy adapter do listView w StartScreen'ie
 *
 */

public class taskListViewAdapter extends ArrayAdapter<Task>{



    public taskListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public taskListViewAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tasks_list_element, parent, false);
        }


        final Task tsk = getItem(position);

        if (tsk != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.textViewElement);
            TextView tt2 = (TextView) v.findViewById(R.id.textViewTimeLeft);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.lvCheckBox);

            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //usuwanie zadania
                    //Log.d("task checked","checked pos"+Integer.toString(position));
                }
            });
            if (tt1 != null) {
                Log.d("textviewfound","testviewfound" + tsk.getTitle());
                tt1.setText(tsk.getTitle());
            }

            if (tt2 != null) {

                //roznica czasu miedzy deadlinem zadania a dzisiaj
                    Calendar tasktime = Calendar.getInstance();  //task time
                    tasktime.set(tsk.getYear(),tsk.getMonth(),tsk.getDay());
                    //Log.d("taskdate", Integer.toString(tsk.getYear())  + " " +  Integer.toString(tsk.getMonth()) + " " +   Integer.toString(tsk.getDay()));
                    Calendar currtime = Calendar.getInstance(); //current time
                    long diff = tasktime.getTimeInMillis() -currtime.getTimeInMillis(); //result in millis
                    long days = (diff / (24 * 60 * 60 * 1000))-1;
                    if(days<1 && days>-1){ tt2.setText("today!");} else
                    if(days==1){ tt2.setText("tommorow");} else
                    { tt2.setText(Integer.toString((int)days)+" days");  }

                }
            }
        return v;
        }




}

