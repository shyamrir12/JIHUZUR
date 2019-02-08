package com.example.awizom.jihuzur.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.awizom.jihuzur.CustomerActivity.CustomerFragment.CustomerHomeServiceFragment;
import com.example.awizom.jihuzur.Fragment.ApplianceFragment;
import com.example.awizom.jihuzur.Fragment.PaintingFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String catagoryNames="";

    public PageAdapter(FragmentManager fm, int numOfTabs, String catagoryName) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.catagoryNames = catagoryName;
    }

    @Override
    public Fragment getItem(int position) {
//        if(catagoryNames.equals("Home Cleaning & Repairs")){
//            return new CustomerHomeServiceFragment();
//        }else if(catagoryNames.equals("Appliance & Repairs")) {
//            return new ApplianceFragment();
//        }
        switch (position) {

            case 0:
                return new CustomerHomeServiceFragment();
            case 1:
                return new ApplianceFragment();
            case 2:
                return new PaintingFragment();
            case 3:
                return new PaintingFragment();
            case 4:
                return new PaintingFragment();
            case 5:
                return new PaintingFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}