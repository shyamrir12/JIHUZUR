package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.awizom.jihuzur.Fragment.ApplianceFragment;
import com.example.awizom.jihuzur.Fragment.FitnessFragment;
import com.example.awizom.jihuzur.Fragment.HomeCleaningFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ApplianceFragment();
            case 1:
                return new FitnessFragment();
            case 2:
                return new HomeCleaningFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}