package com.example.hubert.organiser;

/**
 * Created by filip on 27.11.2016.
 */

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;

public class wysw extends Activity {
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);


        listView = (ListView) findViewById(R.id.list);

}
