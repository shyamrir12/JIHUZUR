package com.example.awizom.jihuzur.EmployeeActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeSkillHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSkillActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addSkill;
    String result = "";
    String[] catalogname = {"--Select Catalog--","Home Cleaning & Repairs", "Appliance & Repairs"};
    String catalogs, catalogid,serviceid;
    List<Catalog> categorylist;
    List<Service> serviceList;
    List cstaha;

    private String[] category, service;
    Spinner categoryspin, servicesspin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_skill);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Employee's Skill");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        intitView();
    }

    private void intitView() {
        addSkill = (FloatingActionButton) findViewById(R.id.addSkill);
        addSkill.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addSkill.getId()) {
            addskillforEmpDialog();


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

                if(catalogs!="--Select Catalog--")
                {
                    categoryspin.setVisibility(View.VISIBLE);
                    servicesspin.setVisibility(View.VISIBLE);
                    getCategoryList();

                }
            else {
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


             String employeeid=   SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();

                try {
                    result = new EmployeeSkillHelper.POSTSkill().execute(employeeid,serviceid).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
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

                service[i] = String.valueOf(serviceList.get(i).getServiceID()) + " " +String.valueOf(serviceList.get(i).getServiceName());


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
