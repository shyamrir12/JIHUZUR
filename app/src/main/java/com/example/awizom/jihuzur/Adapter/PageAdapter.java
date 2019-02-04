package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.awizom.jihuzur.CustomerActivity.CustomerFragment.CustomerHomeServiceFragment;
import com.example.awizom.jihuzur.Fragment.ApplianceFragment;
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
                return new CustomerHomeServiceFragment();
            case 1:
                return new ApplianceFragment();
            case 2:
                return new HomeCleaningFragment();
            case 3:
                return new CustomerHomeServiceFragment();
            case 4:
                return new ApplianceFragment();
            case 5:
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