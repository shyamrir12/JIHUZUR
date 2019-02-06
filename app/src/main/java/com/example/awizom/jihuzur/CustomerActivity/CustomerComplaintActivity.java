package com.example.awizom.jihuzur.CustomerActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.CustomerComplaintHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CustomerComplaintActivity extends Activity implements View.OnClickListener {
    FloatingActionButton addComplaint;
    AutoCompleteTextView editcomplaint;
    String result = "";
    String[] SPINNERLIST = {"Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
        initView();
    }

    private void initView() {
        addComplaint = (FloatingActionButton) findViewById(R.id.addComplaint);
        editcomplaint = (AutoCompleteTextView) findViewById(R.id.complaint);
      Spinner  spinner=(Spinner)findViewById(R.id.spinner2);
        addComplaint.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        spinner.setAdapter(arrayAdapter);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == addComplaint.getId()) {
            AddComplaint();

        }
    }

    private void AddComplaint() {
      String complaint=editcomplaint.getText().toString();
      String Active="True";
      String Status="False";
      String customerId=SharedPrefManager.getInstance(CustomerComplaintActivity.this).getUser().getID();

         try {

            result = new CustomerComplaintHelper.POSTComplaint().execute(customerId,complaint, Active, Status).get();
            Gson gson = new Gson();
            final Result jsonbodyres = gson.fromJson(result, Result.class);
            Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            // System.out.println("Error: " + e);
        }


    }



    }

