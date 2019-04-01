package com.example.awizom.jihuzur.EmployeeActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter.EmployeePageAdapter;
import com.example.awizom.jihuzur.R;


public class EmployeeBookingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    EmployeePageAdapter pageAdapter;
    TabItem outGoing, history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_booking_activity);

        initview();
    }

    private void initview() {

//        getSupportActionBar().setTitle("My Bookings");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/

        toolbar.setTitle("My Booking");


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);


        tabLayout = findViewById(R.id.tablayout);
        outGoing = findViewById(R.id.outgoing);
        history = findViewById(R.id.history);

        viewPager = findViewById(R.id.viewPager);

        pageAdapter = new EmployeePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(EmployeeBookingsActivity.this,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(EmployeeBookingsActivity.this,
                                R.color.colorPrimary));
                    }
                } else if (tab.getPosition() == 2) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(EmployeeBookingsActivity.this,
                            android.R.color.black));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(EmployeeBookingsActivity.this,
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
