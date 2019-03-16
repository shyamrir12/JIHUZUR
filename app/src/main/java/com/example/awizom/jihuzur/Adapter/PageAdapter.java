package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.awizom.jihuzur.CustomerActivity.CustomerFragment.CustomerElectricianFragment;
import com.example.awizom.jihuzur.Fragment.CustomerCarpenterFragment;
import com.example.awizom.jihuzur.Fragment.PaintingFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private static int count = 2;
    private int numOfTabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new CustomerElectricianFragment();
            case 1:
                return new CustomerCarpenterFragment();
            case 2:
                return new PaintingFragment();
            case 3:
               return new PaintingFragment();
//            case 4:
//                return new PaintingFragment();
//            case 5:
//                //return new PaintingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {

            case 0:
                return "Electrician";
            case 1:
                return "Plumber";
            case 2:
                return "Carpenter";
            case 3:
                return "Ac Repair & Fix";
//            case 4:
//                return "Carpenter";
//            case 5:
//                return "Tutor";
            default:

        }
        return null;
    }
}