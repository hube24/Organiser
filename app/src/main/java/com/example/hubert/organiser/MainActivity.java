package com.example.hubert.organiser;

/**
 * Created by Hubert on 2016-10-21.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity {
    ViewPager viewpager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewpager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter padapter = new PagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(padapter);
        startService(new Intent(this, GpsService.class));
        Log.d("Activity","onCreate");
    }
    @Override
    public void onStop(){
//        Log.d("Activity","onStop");
        ((Tasks)(MainActivity.this).getApplication()).clearList();
        super.onStop();
    }
    @Override
    public void onPause(){
//        Log.d("Activity","onPause");
        super.onPause();
    }
    @Override
    public void onResume(){
//        Log.d("Activity","onResume");
        super.onResume();
    }
}
