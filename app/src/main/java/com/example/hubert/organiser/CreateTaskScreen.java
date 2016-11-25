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

import java.util.Date;

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
        TitleText = (EditText) getView().findViewById(R.id.titleText);
        DescriptionText = (EditText) getView().findViewById(R.id.descriptionText);
        DatePick = (DatePicker) getView().findViewById(R.id.datePicker);
        Priority = (SeekBar) getView().findViewById(R.id.seekBar);
        SaveButton = (Button) getView().findViewById(R.id.saveButton);
        SaveButton.setOnClickListener(this);
        return inflater.inflate(R.layout.start_screen, container, false);
    }

    private void saveBtnClicked()
    {
        String title = TitleText.getText().toString();
        String description = DescriptionText.getText().toString();
        int priority = Priority.getProgress();
        int day = DatePick.getDayOfMonth();
        int month = DatePick.getMonth();
        int year = DatePick.getYear();

        if(title == null)
        {
            Toast.makeText(getActivity(),"Please, fill the title first",Toast.LENGTH_SHORT).show();
            return;
        }

        Task ntask = new Task(title, description, day, month, year, priority );
        ntask.AddToLocal();

        Toast.makeText(getActivity(),"Activity added",Toast.LENGTH_SHORT).show();
    }


        /* przy obiektach typu 'Fragment' aby dzialaly przyciski, ustawiamy osobisty dla danego fragmentu 'listener'.
    Nastepnie przy nacisnieciu sprawdzamy id przycisku i wykonujemy odpowiednie dla niego akcje */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveButton:


                break;
        }
    }
}
