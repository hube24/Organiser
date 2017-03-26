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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
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
class OptionObject {
    public String name;
    public String type;
    public String options;
    public String keys;
    public boolean clicked=false;
    public OptionObject(String n, String t, String o,String k){
        this.name=n;
        this.type=t;
        this.options=o;
        this.keys=k;
    }
    public void click(){
        if (this.clicked)
            this.clicked=false;
        else
            this.clicked=true;
    }
}


public class CreateTaskScreen extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, Spinner.OnItemSelectedListener
{
    LinearLayout OptionsLayout;
    AutoCompleteTextView  TitleText;
    AutoCompleteTextView  DescriptionText;
    DatePicker DatePick;
    TimePicker TimePick;
    SeekBar Priority;
    TextView ActualPriority;
    Button SaveButton;
    Spinner OptionSpinner;
    OptionObject[] OptionTab={
            new OptionObject("Przedmiot naukowy","Spinner","{“Options”: [“J. Polski”,”J. Angielski”,”J. Niemiecki”,”J. Francuski”,”J. Hiszpański”,”Matematyka”,”Historia”,”Informatyka”,”Chemia”,”Biologia”,”Religia”,”WF”,”Fizyka”,”Geografia”,”EDB”,”GW”],”Default” : 1}","[“szkola”,”nauka”,”uczyc”,”zadanie”,”domowe”,”odrobic”,”praca”,”wyklad”,”przedmiot”,”lekcje”,”lekcja”,”zajecia”,”zaleglosci”,”edukacja”,”uczelnia”,”nauczyciel”,”kolko”,”egzamin”,”sprawdzian”,”kartkowka”,”klasowka”,”klasowe”,”klasa”,”wywiadowka”,”korki”,”korepetycje”]")
            ,new OptionObject("Lokalizacja","MapView","","[“zakupy”,”kupic”,”spotkanie”,”spotkac,”impreza”,”rozmowa”,”lekarz”,”wizyta”,”rehabilitacja”,”zabieg”,”komisja”,”towarzyskie””,”odwiedzic”,”podejsc”,”odebrac”,”randka”,”nocleg”,”postoj”,”miejsce”,”lokalizacja”,”gdzie”,”wyklad”,”prelekcja”,”koncert”,”wieczor”,”wydarzenie”,”mecz”,”rozgrywka”,”sparing”,”konferencja”]")
            //,new OptionObject("","","","")
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_task_screen, container, false);
        OptionsLayout = (LinearLayout) v.findViewById(R.id.OptionsField);
        TitleText = (AutoCompleteTextView) v.findViewById(R.id.titleText);
        DescriptionText = (AutoCompleteTextView) v.findViewById(R.id.descriptionText);
        DatePick = (DatePicker) v.findViewById(R.id.datePicker);
        TimePick = (TimePicker) v.findViewById(R.id.timePicker);
        TimePick.setIs24HourView(true);
        Priority = (SeekBar) v.findViewById(R.id.seekBar);
        Priority.setOnSeekBarChangeListener(this);
        ActualPriority = (TextView) v.findViewById(R.id.actual);
        SaveButton = (Button) v.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(this);
        reloadTips();
        Button testButton=(Button) v.findViewById(R.id.testButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvv=new TextView(getContext());
                tvv.setText("textview");
                OptionsLayout.addView(tvv);
            }
        });
        OptionSpinner = (Spinner) v.findViewById(R.id.OptionsSpinner);
        makeOptionSpinner();
        OptionSpinner.setOnItemSelectedListener(this);
        return  v;
    }
    private void makeOptionSpinner(){
        String[] values = new String[OptionTab.length+1];
        int j=0;
        for(int i=0;i<OptionTab.length;i++)
            if(!OptionTab[i].clicked)
                values[i-j]=OptionTab[i].name;
            else
                j++;
        String[] values2 = new String[OptionTab.length+1-j];
        values2[0]="Nic";
        for(int i=0;i<OptionTab.length-j;i++)
            values2[i+1]=values[i];
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, values2);
        OptionSpinner.setAdapter(dataAdapter);

        Log.d("spinner","Generated");

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


        if(title.isEmpty())
        {
            Toast.makeText(getActivity(),"Please, fill the title first",Toast.LENGTH_SHORT).show();
            return;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("spinner","Selected: " + position + "/" + id);
//        String item = parent.getItemAtPosition(position).toString();
        if(position==0)
            return;
//        OptionTab[position-1].click();
        makeOptionSpinner();
        OptionSpinner.setSelection(0);
    //    Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        //  Auto-generated method stub
    }
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