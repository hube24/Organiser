package com.example.hubert.organiser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Hubert on 2016-10-21.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 1:
                return new StartScreen();
            case 0:
                return new CreateTaskScreen();
            default: break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}


