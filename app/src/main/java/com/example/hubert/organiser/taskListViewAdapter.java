package com.example.hubert.organiser;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Point;
import android.os.Debug;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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


    private PopupWindow taskInfoPopup;
    private LayoutInflater popupLayoutInflater;
    Task tsk = new Task();
    DataBase db = new DataBase(getContext().getApplicationContext());
    Context tasksContext;


    public taskListViewAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public taskListViewAdapter(Context context, int resource, List<Task> items) {
        super(context, resource, items);
        tasksContext = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.tasks_list_element, parent, false);
        }


         tsk = getItem(position);

        if (tsk != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.textViewElement);
            final TextView tt2 = (TextView) v.findViewById(R.id.textViewTimeLeft);
            CheckBox checkBox = (CheckBox) v.findViewById(R.id.lvCheckBox);
            checkBox.setChecked(tsk.getChecked());
            checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //usuwanie zadania
                    if(isChecked) {
                        //db.deleteTask(tsk.getId());
                        db.setCheckedTask(tsk.getId());
                        ((Tasks) tasksContext.getApplicationContext()).clearList();
                        ((Tasks) tasksContext.getApplicationContext()).loadTasks();
                        Log.d("task checked", "checked pos" + Integer.toString(position));
                    }
                }
            });

            /////////// Popupz informacjami o danym zadaniu z listy ////////////

            WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            final int popwidth = (int)(size.x * 0.7);
            final int popheight = (int)(size.y * 0.7);

            final String poptitle = "Title : " + tsk.getTitle();
            final String popdescription = "Description : " + tsk.getDescription();
            final String poppriority = "Priority : " + Integer.toString(tsk.getPriority());
            final String popdeadline = "Deadline : " + getCurrentDeadline();
            final String popbefore = "Tasks before: " + Integer.toString(position);

            LinearLayout toInfoBtn = (LinearLayout) v.findViewById(R.id.toTaskInfoButton);
            toInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupLayoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View container = (View) popupLayoutInflater.inflate(R.layout.taskinfo_popup,null);

                    // przekazywanie danych o task do popup'a
                    TextView popTitleTW = (TextView) container.findViewById(R.id.popupTitle);
                    TextView popDescriptionTW = (TextView) container.findViewById(R.id.popupDescription);
                    TextView popPriorityTW = (TextView) container.findViewById(R.id.popupPriority);
                    TextView popDeadlineTW = (TextView) container.findViewById(R.id.popupDeadline);
                    TextView popBeforeTW = (TextView) container.findViewById(R.id.popupBefore);

                    popTitleTW.setText(poptitle);
                    popDescriptionTW.setText(popdescription);
                    popPriorityTW.setText(poppriority);
                    popDeadlineTW.setText(popdeadline);
                    popBeforeTW.setText(popbefore);

                    //tworzenie nowego popup window
                    taskInfoPopup = new PopupWindow(container,popwidth,popheight,true);
                    //pokazywanie popup'a
                    taskInfoPopup.showAtLocation(v, Gravity.CENTER, 0,0);
                    //ustawianie listenera usuwajacego popupa
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            taskInfoPopup.dismiss();
                            return true;
                        }
                    });
                }
             //////////koniec popup'a /////////////////////////////////////

            });

            if (tt1 != null) {
                // Log.d("textviewfound","testviewfound" + tsk.getTitle());
                tt1.setText(tsk.getTitle());
            }

            if (tt2 != null) {

                tt2.setText(getCurrentDeadline());
            }
        }
        return v;
    }


    String getCurrentDeadline()
    {
        //roznica czasu miedzy deadlinem zadania a dzisiaj
        Calendar tasktime = Calendar.getInstance();  //task time
        tasktime.set(tsk.getYear(),tsk.getMonth(),tsk.getDay());
        //Log.d("taskdate", Integer.toString(tsk.getYear())  + " " +  Integer.toString(tsk.getMonth()) + " " +   Integer.toString(tsk.getDay()));
        Calendar currtime = Calendar.getInstance(); //current time
        long diff = tasktime.getTimeInMillis() -currtime.getTimeInMillis(); //result in millis

        long hours= (diff / ( 60 * 60 * 1000));
        Log.d("diff",Integer.toString((int)hours));
        if(hours>-1 && hours<1){ return "today!";} else
        if(hours>23 && hours<25){ return "tommorow";} else
         return (Integer.toString((int)(hours/24))+" days");

    }

}