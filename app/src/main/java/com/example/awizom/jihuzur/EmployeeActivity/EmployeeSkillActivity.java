package com.example.awizom.jihuzur.EmployeeActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.awizom.jihuzur.Adapter.EmployeeSkillServiceAdapter;
import com.example.awizom.jihuzur.CustomerActivity.NewCustomerHome;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.Model.Skill;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class EmployeeSkillActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addSkill;
    String result = "";
    String[] catalogname = {"--Select Catalog--", "Home Cleaning & Repairs", "Appliance & Repairs"};
    String catalogs, catalogid, serviceid;
    List<Catalog> categorylist;
    List<Skill>  serviceListforshow;
    List<Service> serviceList;
    RecyclerView recyclerView;
    EmployeeSkillServiceAdapter employeeSkillServiceAdapter;
    Spinner categoryspin, servicesspin;
    private String[] category, service;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_skill);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        String employeeNmae = SharedPrefManager.getInstance(EmployeeSkillActivity.this).getUser().getName();
        toolbar.setTitle("Select Skill");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              intent=new Intent(EmployeeSkillActivity.this,EmployeeHomePage.class);
              startActivity(intent);
            }
        });
        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        intitView();
    }

    private void intitView() {

        recyclerView = findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getEmployeeSkill();
        addSkill = (FloatingActionButton) findViewById(R.id.addSkill);
        addSkill.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getEmployeeSkill();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent intent =new Intent(this,EmployeeHomePage.class);
            startActivity(intent);
        }
            return super.onKeyDown(keyCode, event);

    }
    public void getEmployeeSkill() {

        String employeeid = SharedPrefManager.getInstance(EmployeeSkillActivity.this).getUser().getID();
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new EmployeeOrderHelper.GetEmployeeSkill().execute(employeeid.toString()).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Skill>>() {
            }.getType();
            serviceListforshow = new Gson().fromJson(result, listType);
            employeeSkillServiceAdapter = new EmployeeSkillServiceAdapter(EmployeeSkillActivity.this, serviceListforshow);
            recyclerView.setAdapter(employeeSkillServiceAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addSkill.getId()) {
            intent = new Intent(EmployeeSkillActivity.this, NewCustomerHome.class);
            intent = intent.putExtra("EmployeeSkill","EmployeeSkill");
            intent = intent.putExtra("CategoryName",5);
            startActivity(intent);
           // addskillforEmpDialog();

        }
    }



}
