package com.example.hubert.organiser;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Hubert on 2017-02-03.
 */

public class TaskHistory extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.task_history_screen,container,false);
        ((Tasks)getActivity().getApplication()).setAdapter();
        listView = (ListView) v.findViewById(R.id.tasksHistoryListView);
        listView.setAdapter(((Tasks)getActivity().getApplication()).getHistoryAdapter());
        ((Tasks)getActivity().getApplication()).clearList();
        ((Tasks)getActivity().getApplication()).loadTasks();
        return v;
    }
}
