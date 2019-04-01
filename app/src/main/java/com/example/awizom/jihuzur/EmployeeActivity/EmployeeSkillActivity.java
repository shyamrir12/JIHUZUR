package com.example.awizom.jihuzur.EmployeeActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.awizom.jihuzur.Adapter.EmployeeSkillServiceAdapter;
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
                onBackPressed();
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
            intent = new Intent(EmployeeSkillActivity.this, MenuActivity.class);
            intent = intent.putExtra("EmployeeSkill","EmployeeSkill");
            intent = intent.putExtra("CategoryName",5);
            startActivity(intent);
           // addskillforEmpDialog();

        }
    }

    public void addskillforEmpDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_skill_layout, null);
        dialogBuilder.setView(dialogView);
        final Spinner catalogspin = (Spinner) dialogView.findViewById(R.id.catalog);
        categoryspin = (Spinner) dialogView.findViewById(R.id.category);
        categoryspin.setVisibility(View.GONE);
        servicesspin = (Spinner) dialogView.findViewById(R.id.services);
        servicesspin.setVisibility(View.GONE);
        catalogspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                catalogs = catalogname[position];

                if (catalogs != "--Select Catalog--") {
                    categoryspin.setVisibility(View.VISIBLE);
                    servicesspin.setVisibility(View.VISIBLE);
                    getCategoryList();
                } else {
                    categoryspin.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, catalogname);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//Setting the ArrayAdapter data on the Spinner
        catalogspin.setAdapter(aa);
        categoryspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                catalogid = category[position].split(" ")[0];
                getServiceList(catalogid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        servicesspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serviceid = service[position].split(" ")[0];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        final Button buttonAddSkill = (Button) dialogView.findViewById(R.id.buttonAddSkill);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Employee Skill");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String employeeid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();

                try {
                    result = new EmployeeOrderHelper.EmployeePOSTSkill().execute(employeeid, serviceid).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    getEmployeeSkill();
                    Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                }
                b.dismiss();
            }


        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });
    }

    private void getServiceList(String catalogid) {
        try {
            result = new AdminHelper.GETServiceList().execute(catalogid.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Service>>() {
            }.getType();
            serviceList = new Gson().fromJson(result, listType);
            service = new String[serviceList.size()];
            for (int i = 0; i < serviceList.size(); i++) {
                service[i] = String.valueOf(serviceList.get(i).getServiceID()) + " " + String.valueOf(serviceList.get(i).getServiceName());
            }

            ArrayAdapter serviceadapt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, service);
            serviceadapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            servicesspin.setAdapter(serviceadapt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategoryList() {
        try {
            result = new AdminHelper.GETCategoryList().execute(catalogs.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Catalog>>() {
            }.getType();
            categorylist = new Gson().fromJson(result, listType);
            category = new String[categorylist.size()];

            for (int i = 0; i < categorylist.size(); i++) {
                category[i] = String.valueOf(categorylist.get(i).getCatalogID()) + " " + String.valueOf(categorylist.get(i).getCategory());
            }

            ArrayAdapter catgoryadpt = new ArrayAdapter(this, android.R.layout.simple_spinner_item, category);
            catgoryadpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            categoryspin.setAdapter(catgoryadpt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
