package com.example.awizom.jihuzur;

import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.Adapter.PageAdapter;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    TabItem electrician, carpenter, plumber, ac_repair_fix;


    private String catagoryName="",employeeSkill="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initview();
    }

    private void initview() {


        getSupportActionBar().setTitle("Home Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        catagoryName = String.valueOf(getIntent().getIntExtra("CategoryName",0));
        employeeSkill = getIntent().getStringExtra("EmployeeSkill");

        tabLayout = findViewById(R.id.tablayout);
        electrician = findViewById(R.id.Electrician);
        carpenter = findViewById(R.id.Carpenter);
        plumber = findViewById(R.id.Plumber);

         ac_repair_fix = findViewById(R.id.Ac_repair);
//         tutors  = findViewById(R.id.Tutor);
//         movingHome  = findViewById(R.id.Moving);

        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        if(catagoryName.contains("2")){
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }else if(catagoryName.equals("3")){
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        } else if(catagoryName.equals("1")){
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }
        else if(catagoryName.equals("0")){
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }
//        else if(catagoryName.equals("1")){
//            TabLayout.Tab tab = tabLayout.getTabAt(1);
//            tab.select();
//            tabLayout.setupWithViewPager(viewPager);
//            viewPager.setCurrentItem(tab.getPosition());
//        }
//        else if(catagoryName.equals("0")){
//            TabLayout.Tab tab = tabLayout.getTabAt(0);
//            tab.select();
//            tabLayout.setupWithViewPager(viewPager);
//            viewPager.setCurrentItem(tab.getPosition());
//        }
        else {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
            tabLayout.setupWithViewPager(viewPager);
            viewPager.setCurrentItem(tab.getPosition());
        }

    }
}