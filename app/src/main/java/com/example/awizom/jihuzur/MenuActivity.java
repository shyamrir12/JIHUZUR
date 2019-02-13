package com.example.awizom.jihuzur;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.Adapter.PageAdapter;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem appliance,massage,homecleaning,painting,tutors,movingHome;
//    TabItem massage;
//    TabItem homecleaning;
//    TabItem painting;
//    TabItem tutors;
//    TabItem movingHome;

    private String catagoryName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initview();
    }

    private void initview() {


//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setTitle("Home Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        catagoryName = String.valueOf(getIntent().getIntExtra("CategoryName",0));
        tabLayout = findViewById(R.id.tablayout);
        appliance = findViewById(R.id.HomeServices);
        massage = findViewById(R.id.Appliance);
        homecleaning = findViewById(R.id.HomeCleaning);

         painting  = findViewById(R.id.Painting);
         tutors  = findViewById(R.id.Tutor);
         movingHome  = findViewById(R.id.Moving);

        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if(catagoryName.contains("4")){
            TabLayout.Tab tab = tabLayout.getTabAt(4);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }else if(catagoryName.equals("5")){
            TabLayout.Tab tab = tabLayout.getTabAt(5);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else if(catagoryName.equals("3")){
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else if(catagoryName.equals("2")){
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else if(catagoryName.equals("1")){
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else if(catagoryName.equals("0")){
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                if (tab.getPosition() == 1) {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            R.color.colorAccent));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorPrimary));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                R.color.colorPrimary));
//                    }
//                } else if (tab.getPosition() == 2) {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            android.R.color.darker_gray));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            android.R.color.black));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                android.R.color.black));
//                    }
//                }else if (tab.getPosition() == 3) {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            R.color.colorAccent));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorPrimary));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                R.color.colorPrimary));
//                    }
//                } else if (tab.getPosition() == 4) {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            android.R.color.darker_gray));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            android.R.color.black));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                android.R.color.black));
//                    }
//                }else if (tab.getPosition() == 5) {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            android.R.color.darker_gray));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            android.R.color.black));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                android.R.color.black));
//                    }
//                } else {
////                    toolbar.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
////                            R.color.colorPrimary));
//                    tabLayout.setBackgroundColor(ContextCompat.getColor(MenuActivity.this,
//                            R.color.colorPrimary));
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getWindow().setStatusBarColor(ContextCompat.getColor(MenuActivity.this,
//                                R.color.colorPrimaryDark));
//                    }
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

    }
}