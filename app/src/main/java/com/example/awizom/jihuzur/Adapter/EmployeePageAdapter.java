package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.awizom.jihuzur.Fragment.EmployeeCurrentOrderFragment;
import com.example.awizom.jihuzur.Fragment.EmployeeHistoryCurrentFragment;

public class EmployeePageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public EmployeePageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new EmployeeCurrentOrderFragment();
            case 1:
                return new EmployeeHistoryCurrentFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}