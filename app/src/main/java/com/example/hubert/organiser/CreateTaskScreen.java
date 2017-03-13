package com.example.hubert.organiser;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Hubert on 2016-10-21.
 *
 * Ekran tworzenia nowego zadania
 *
 * Do uzupełnienia
 */

public class CreateTaskScreen extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{

    AutoCompleteTextView  TitleText;
    AutoCompleteTextView  DescriptionText;
    DatePicker DatePick;
    SeekBar Priority;
    TextView ActualPriority;
    Button SaveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_task_screen, container, false);

        TitleText = (AutoCompleteTextView) v.findViewById(R.id.titleText);
        DescriptionText = (AutoCompleteTextView) v.findViewById(R.id.descriptionText);
        DatePick = (DatePicker) v.findViewById(R.id.datePicker);
        Priority = (SeekBar) v.findViewById(R.id.seekBar);
        Priority.setOnSeekBarChangeListener(this);
        ActualPriority = (TextView) v.findViewById(R.id.actual);
        SaveButton = (Button) v.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(this);
        reloadTips();

        return  v;
    }


    private void saveBtnClicked()
    {
        DataBase db = new DataBase(getContext());
        String title = TitleText.getText().toString();
        String description = DescriptionText.getText().toString();
        int priority = Priority.getProgress();
        int day = DatePick.getDayOfMonth();
        int month = DatePick.getMonth();
        int year = DatePick.getYear();
        Log.d("ymd",year+" "+month+" "+day);


        if(title.isEmpty())
        {
            Toast.makeText(getActivity(),"Please, fill the title first",Toast.LENGTH_SHORT).show();
            return;
        }
        int mostOftenDay=db.mostOften(title);
        if(mostOftenDay!=db.dayOfWeek(year,month,day)){
            int d1 = day-1, d2 = day+1;
            Calendar c1 = Calendar.getInstance(),c2=Calendar.getInstance();
            Date tempDate1 = new Date(year-1900,month,day);
            c1.setTime(tempDate1);
            c1.add(Calendar.DATE,-1);
            tempDate1=c1.getTime();
            while(mostOftenDay!=db.dayOfWeek(tempDate1.getYear()+1900,tempDate1.getMonth(),tempDate1.getDate())){
                c1.setTime(tempDate1);
                c1.add(Calendar.DATE,-1);
                tempDate1=c1.getTime();
            }
            Date tempDate2 = new Date(year-1900,month,day);
            c1.setTime(tempDate2);
            c1.add(Calendar.DATE,1);
            tempDate2=c1.getTime();
            while(mostOftenDay!=db.dayOfWeek(tempDate2.getYear()+1900,tempDate2.getMonth(),tempDate2.getDate())){
                c1.setTime(tempDate2);
                c1.add(Calendar.DATE,1);
                tempDate2=c1.getTime();
            }
            final Date today = new Date(),toChange;
            if(tempDate1.before(today))
                 toChange=tempDate2;
            else
                toChange=tempDate1;
//            DatePick.updateDate(toChange.getYear(),toChange.getMonth(),toChange.getDate());
            int tyear=toChange.getYear()+1900;
            Toast.makeText(getActivity(),"I recommend date "+toChange.getDate()+"."+toChange.getMonth()+"."+tyear,Toast.LENGTH_SHORT).show();
        }

        Task ntask = new Task();
        ntask.setTask(0,title, description, priority, day, month, year,false);
        db.addTask(ntask);
        db.addTip(title,0);
        db.addTip(description,1);
        ((Tasks)getActivity().getApplication()).clearList();
        ((Tasks)getActivity().getApplication()).loadTasks();
        Toast.makeText(getActivity(),"Task added",Toast.LENGTH_SHORT).show();
        reloadTips();
    }
    private void reloadTips(){
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
        DataBase db = new DataBase(getContext());
        Cursor el = db.getTips(0);
        while (el.moveToNext()){
            titles.add(el.getString(0));
        }
        Cursor el2 = db.getTips(1);
        while (el2.moveToNext()){
            descriptions.add(el2.getString(0));
        }
        db.close();
        TitleText.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.sugestion_element,R.id.textViewElement,titles));
        DescriptionText.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.sugestion_element,R.id.textViewElement,descriptions));
    }

        /* przy obiektach typu 'Fragment' aby dzialaly przyciski, ustawiamy osobisty dla danego fragmentu 'listener'.
    Nastepnie przy nacisnieciu sprawdzamy id przycisku i wykonujemy odpowiednie dla niego akcje */

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { //Tutaj wyswietlam wartosc priority w actual
        ActualPriority.setText(""+progress);
    }
    public void onStartTrackingTouch(SeekBar seekBar) {}
    public void onStopTrackingTouch(SeekBar seekBar) {}
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveBtnClicked();
                break;
        }
    }
}