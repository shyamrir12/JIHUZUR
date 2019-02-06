package com.example.awizom.jihuzur.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.Adapter.PageAdapter;
import com.example.awizom.jihuzur.R;

public class SearchFragment extends Fragment {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem appliance;
    TabItem massage;
    TabItem homecleaning;
    private String catagoryName="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     View view= inflater.inflate(R.layout.activity_menu,container,false);
       initView(view);
       return  view;
    }
    private void initView(View view)
    {

        tabLayout = view.findViewById(R.id.tablayout);
        appliance = view.findViewById(R.id.applianceRepair);
        massage =view.findViewById(R.id.massageFitness);
        homecleaning =view.findViewById(R.id.homeCleaning);
        viewPager =view.findViewById(R.id.viewPager);

        pageAdapter = new PageAdapter(getFragmentManager(), tabLayout.getTabCount(), catagoryName);
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorAccent));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),
                            R.color.colorAccent));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(),
                                R.color.colorAccent));
                    }
                } else if (tab.getPosition() == 2) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            android.R.color.darker_gray));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),
                            android.R.color.darker_gray));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(),
                                android.R.color.darker_gray));
                    }
                } else {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorPrimary));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(getActivity(),
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(),
                                R.color.colorPrimaryDark));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}
