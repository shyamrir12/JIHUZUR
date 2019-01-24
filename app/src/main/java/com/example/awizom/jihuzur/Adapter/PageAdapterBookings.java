package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.awizom.jihuzur.Fragment.HistoryFragment;
import com.example.awizom.jihuzur.Fragment.CutomerCurrentOrderFragment;

public class PageAdapterBookings  extends FragmentPagerAdapter {

    private int numOfTabs;

    public PageAdapterBookings(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CutomerCurrentOrderFragment();
            case 1:
                return new HistoryFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}