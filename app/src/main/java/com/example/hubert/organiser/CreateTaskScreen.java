package com.example.hubert.organiser;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.List;


/**
 * Created by Hubert on 2016-10-21.
 *
 * Ekran tworzenia nowego zadania
 *
 * Do uzupełnienia
 */

public class CreateTaskScreen extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

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
        reloadSugestions();

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


        if(title.isEmpty())
        {
            Toast.makeText(getActivity(),"Please, fill the title first",Toast.LENGTH_SHORT).show();
            return;
        }

        Task ntask = new Task();
        ntask.setTask(0,title, description, day, month, year, priority );
        db.addTask(ntask);
        ((Tasks)getActivity().getApplication()).clearList();
        ((Tasks)getActivity().getApplication()).loadTasks();
        Toast.makeText(getActivity(),"Task added",Toast.LENGTH_SHORT).show();
        reloadSugestions();
    }
    private void reloadSugestions(){
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> descriptions = new ArrayList<String>();
        DataBase db = new DataBase(getContext());
        String[] setNames = {"title","description"};
        Cursor el = db.getTasks(setNames);
        while (el.moveToNext()){
            titles.add(el.getString(0));
            descriptions.add(el.getString(1));
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
