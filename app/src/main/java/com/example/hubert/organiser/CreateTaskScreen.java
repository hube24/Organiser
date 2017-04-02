package com.example.hubert.organiser;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


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
            new OptionObject("Przedmiot naukowy","Spinner","{Options: [\"J. Polski\",\"J. Angielski\",\"J. Niemiecki\",\"J. Francuski\",\"J. Hiszpański\",\"Matematyka\",\"Historia\",\"Informatyka\",\"Chemia\",\"Biologia\",\"Religia\",\"WF\",\"Fizyka\",\"Geografia\",\"EDB\",\"GW\"],Default : 1}","[\"szkola\",\"nauka\",\"uczyc\",\"zadanie\",\"domowe\",\"odrobic\",\"praca\",\"wyklad\",\"przedmiot\",\"lekcje\",\"lekcja\",\"zajecia\",\"zaleglosci\",\"edukacja\",\"uczelnia\",\"nauczyciel\",\"kolko\",\"egzamin\",\"sprawdzian\",\"kartkowka\",\"klasowka\",\"klasowe\",\"klasa\",\"wywiadowka\",\"korki\",\"korepetycje\"]")
            ,new OptionObject("Lokalizacja","MapView","","[\"zakupy\",\"kupic\",\"spotkanie\",\"spotkac,\"impreza\",\"rozmowa\",\"lekarz\",\"wizyta\",\"rehabilitacja\",\"zabieg\",\"komisja\",\"towarzyskie\"\",\"odwiedzic\",\"podejsc\",\"odebrac\",\"randka\",\"nocleg\",\"postoj\",\"miejsce\",\"lokalizacja\",\"gdzie\",\"wyklad\",\"prelekcja\",\"koncert\",\"wieczor\",\"wydarzenie\",\"mecz\",\"rozgrywka\",\"sparing\",\"konferencja\"]")
            ,new OptionObject("Oczekiwany czas","NumberInput","","[\"zadanie\"]")
            //,new OptionObject("","","","")
    };
    String coords;
    String address;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_task_screen, container, false);
        TitleText = (AutoCompleteTextView) v.findViewById(R.id.titleText);
        DescriptionText = (AutoCompleteTextView) v.findViewById(R.id.descriptionText);
        OptionsLayout = (LinearLayout) v.findViewById(R.id.OptionsField);
        DatePick = (DatePicker) v.findViewById(R.id.datePicker);
        TimePick = (TimePicker) v.findViewById(R.id.timePicker);
        TimePick.setIs24HourView(true);
        Priority = (SeekBar) v.findViewById(R.id.seekBar);
        Priority.setOnSeekBarChangeListener(this);
        ActualPriority = (TextView) v.findViewById(R.id.actual);
        SaveButton = (Button) v.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(this);
        reloadTips();
        OptionSpinner = (Spinner) v.findViewById(R.id.OptionsSpinner);
        makeOptionSpinner();
        OptionSpinner.setOnItemSelectedListener(this);
        return  v;
    }
    private void makeOptionSpinner(){
        int leng=0;
        for(int i=0;i<OptionTab.length;i++)
            if(!OptionTab[i].clicked)
                leng++;
        Log.d("spinner","Elementy: "+leng);
        String[] values = new String[leng+1];
        values[0]="Dodaj szczegóły:";
        int j=0;
        for(int i=0;i<OptionTab.length;i++)
            if(!OptionTab[i].clicked)
                values[i-j+1]=OptionTab[i].name;
            else
                j++;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, values);
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
        int time = TimePick.getCurrentHour()*60+TimePick.getCurrentMinute();
        String details ="{";
        for(int i=0;i<OptionTab.length;i++) {
            if (OptionTab[i].clicked) {
                details+=OptionTab[i].name.replaceAll(" ","_");
                details+=": ";
                switch (OptionTab[i].type) {
                    case "Spinner":
                        View myView = OptionsLayout.findViewById(R.id.element_spinner);
                        Spinner optSpinner = (Spinner) myView.findViewById(R.id.spinner);
                        details+=("\""+optSpinner.getSelectedItem().toString()+"\"");
                        break;
                    case "MapView":
                        details+=("\""+coords+"\"");
                        break;
                    case "NumberInput":
                        View myView2 = OptionsLayout.findViewById(R.id.element_expectedtime);
                        EditText optEdittext = (EditText) myView2.findViewById(R.id.value);
                        details+=("\""+optEdittext.getText().toString()+"\"");
                        break;
                }
                details+=",";
            }
        }
        details = details.substring(0, details.length()-1);
        details+="}";
        Log.d("db adding",details);

        if(title.isEmpty())
        {
            Toast.makeText(getActivity(),"Please, fill the title first",Toast.LENGTH_SHORT).show();
            return;
        }

        Task ntask = new Task();
        ntask.setTask(0,title, description, priority, day, month, year, time, details, false);
        db.addTask(ntask);
        db.addTip(title,0);
        db.addTip(description,1);
        ((Tasks)getActivity().getApplication()).clearList();
        ((Tasks)getActivity().getApplication()).loadTasks();
        Toast.makeText(getActivity(),"Task added",Toast.LENGTH_SHORT).show();
        reloadTips();

        TitleText.setText("");
        DescriptionText.setText("");
        Priority.setProgress(1);
        DatePick.setActivated(false);
        DatePick.setActivated(true);
        TimePick.setActivated(false);
        TimePick.setActivated(true);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this);
        ft.attach(this);
        ft.commit();
        for(int i=0;i<OptionTab.length;i++)
            if (OptionTab[i].clicked)
                OptionTab[i].click();
        OptionsLayout.removeAllViews();
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

    public int getIdByPosition(int position){
        for(int i=0;i<OptionTab.length;i++){
            if(!OptionTab[i].clicked)
                position--;
            if(position==0)
                return i;
        }
        Log.d("spinner","Error? 89");
        return (OptionTab.length-1);
    }
    public void addOptionELement(final int id){
        switch (OptionTab[id].type){
            case "Spinner":
                OptionTab[id].click();
                makeOptionSpinner();
                try {
                    JSONArray temp = (new JSONObject(OptionTab[id].options).getJSONArray("Options"));
                    String[] values = new String[temp.length()];
                    if (temp != null) {
                        int len = temp.length();
                        for (int i=0;i<len;i++){
                            values[i]=(temp.get(i).toString());
                        }
                    }
                    Log.d("new View","13");
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.element_spinner, OptionsLayout, false);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, values);
                    TextView opttextView = (TextView) v.findViewById(R.id.name);
                    opttextView.setText(OptionTab[id].name);
                    Spinner optSpinner = (Spinner) v.findViewById(R.id.spinner);
                    optSpinner.setAdapter(dataAdapter);
                    ImageButton optButton = (ImageButton) v.findViewById(R.id.closeButton);
                    optButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            View myView = OptionsLayout.findViewById(R.id.element_spinner);
                            ViewGroup parent = (ViewGroup) myView.getParent();
                            parent.removeView(myView);
                            OptionTab[id].click();
                            Log.d("new View","Kliknieto");
                            makeOptionSpinner();
                        }
                    });
                    OptionsLayout.addView(v);
                    Log.d("new View","123");
                } catch (Exception e){}
                break;
            case "MapView":
                openMapActivity();
                break;
            case "NumberInput":
                OptionTab[id].click();
                makeOptionSpinner();
                try {
                    LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.element_expectedtime, OptionsLayout, false);

                    TextView opttextView = (TextView) v.findViewById(R.id.name);
                    opttextView.setText(OptionTab[id].name);
                    ImageButton optButton = (ImageButton) v.findViewById(R.id.closeButton);
                    optButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            View myView = OptionsLayout.findViewById(R.id.element_expectedtime);
                            ViewGroup parent = (ViewGroup) myView.getParent();
                            parent.removeView(myView);
                            OptionTab[id].click();
                            Log.d("new View","Kliknieto3");
                            makeOptionSpinner();
                        }
                    });
                    OptionsLayout.addView(v);
                    Log.d("new View","5");
                } catch (Exception e){}
                break;
        }
//        TextView tvv=new TextView(getContext());
//        tvv.setText("textview");
//        OptionsLayout.addView(tvv);
    }
//funkcja otwierająca aztivity z mapa
    private void openMapActivity()
    {
        Intent mapIntent = new Intent(getActivity(), MapsActivity.class);
        startActivityForResult(mapIntent,1);
    }

// funkcja czekajaca na zwrotna informacje z mapActivity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                coords = data.getStringExtra("coords");
                address = data.getStringExtra("address");
                OptionTab[1].click();
                makeOptionSpinner();
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.element_location, OptionsLayout, false);

                TextView opttextView = (TextView) v.findViewById(R.id.name);
                opttextView.setText("Lokalizacja: "+address);

                ImageButton optButton = (ImageButton) v.findViewById(R.id.closeButton);
                optButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View myView = OptionsLayout.findViewById(R.id.element_location);
                        ViewGroup parent = (ViewGroup) myView.getParent();
                        parent.removeView(myView);
                        OptionTab[1].click();
                        Log.d("new View","Kliknieto2");
                        makeOptionSpinner();
                    }
                });
                OptionsLayout.addView(v);
                Log.d("new View","4");

                ////....................................////
                //tutaj dopisanie wpolrzednych do database//
                //i dopisanie adresu gdzies w create task///
                ////....................................////

                Toast.makeText(getActivity(),"saving localisation: "+ address ,Toast.LENGTH_SHORT).show();
                //
            }
        }
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long idunused) {
//        String item = parent.getItemAtPosition(position).toString();
        if(position==0)
            return;
        int id=getIdByPosition(position);
        Log.d("spinner","Selected: " + position + "/" + id);
        addOptionELement(id);
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