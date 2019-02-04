package com.example.awizom.jihuzur;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPageAdapterBookings;

public class MyBokingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    CustomerPageAdapterBookings pageAdapter;
    TabItem outGoing,history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mybooking);

        initview();
    }

    private void initview() {


//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle("My Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tabLayout = findViewById(R.id.tablayout);
        outGoing = findViewById(R.id.outgoing);
        history = findViewById(R.id.history);

        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new CustomerPageAdapterBookings(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorAccent));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MyBokingsActivity.this,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MyBokingsActivity.this,
                                R.color.colorPrimary));
                    }
                } else if (tab.getPosition() == 2) {
//                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            android.R.color.darker_gray));
                    tabLayout.setBackgroundColor(ContextCompat.getColor(MyBokingsActivity.this,
                            android.R.color.black));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(MyBokingsActivity.this,
                                android.R.color.black));
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