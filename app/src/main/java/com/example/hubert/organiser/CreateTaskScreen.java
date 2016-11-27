package com.example.hubert.organiser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;


/**
 * Created by Hubert on 2016-10-21.
 *
 * Ekran tworzenia nowego zadania
 *
 * Do uzupełnienia
 */

public class CreateTaskScreen extends Fragment implements View.OnClickListener {

    EditText TitleText;
    EditText DescriptionText;
    DatePicker DatePick;
    SeekBar Priority;
    Button SaveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_task_screen, container, false);

        TitleText = (EditText) v.findViewById(R.id.titleText);
        DescriptionText = (EditText) v.findViewById(R.id.descriptionText);
        DatePick = (DatePicker) v.findViewById(R.id.datePicker);
        Priority = (SeekBar) v.findViewById(R.id.seekBar);
        SaveButton = (Button) v.findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(this);

        return  v;
    }

    private void saveBtnClicked()
    {
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


        Task ntask = new Task(title, description, day, month, year, priority );

        ((Tasks)getActivity().getApplication()).addTask(ntask);
        ((Tasks)getActivity().getApplication()).sortTasks();

        Toast.makeText(getActivity(),"Task added",Toast.LENGTH_SHORT).show();
    }


        /* przy obiektach typu 'Fragment' aby dzialaly przyciski, ustawiamy osobisty dla danego fragmentu 'listener'.
    Nastepnie przy nacisnieciu sprawdzamy id przycisku i wykonujemy odpowiednie dla niego akcje */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:
                saveBtnClicked();
                break;
        }
    }
}
