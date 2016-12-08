package com.example.hubert.organiser;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Hubert on 2016-10-21.
 *
 * wersja tymczasowa, do napisania custom adapter
 */

public class StartScreen extends Fragment {

    ArrayList<String> TaskNames = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.start_screen,container,false);
        ((Tasks)getActivity().getApplication()).setAdapter();
        listView = (ListView) v.findViewById(R.id.tasksListView);
        listView.setAdapter(((Tasks)getActivity().getApplication()).getAdapter());
        DataBase db = new DataBase(getContext());
        Cursor el = db.getTasks();
        while (el.moveToNext()){

            Task ntask = new Task(el.getString(1), el.getString(2), el.getInt(3), el.getInt(4), el.getInt(5), el.getInt(6) );

            ((Tasks)getActivity().getApplication()).addTask(ntask);
        }
        ((Tasks)getActivity().getApplication()).sortTasks();
        ((Tasks)getActivity().getApplication()).updateList();
        return v;
    }

}
