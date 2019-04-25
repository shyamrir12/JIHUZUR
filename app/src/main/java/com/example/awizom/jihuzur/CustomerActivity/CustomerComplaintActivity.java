package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CustomerComplaintListAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Complaint;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CustomerComplaintActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton addComplaint;
    AutoCompleteTextView editcomplaint, editcomplaintinDialog;
    String result = "";
    List<Complaint> complaintlist;
    List<Complaint> complaintist;
    RecyclerView recyclerView;
    CustomerComplaintListAdapter customerComplainAdapetr;
    String[] SPINNERLIST = {"Active Complaint", "Create Complaint", "Solved Complaint"};
    private ProgressDialog progressDialog;
    private static int TIMER = 300;
    String check;
    private Spinner spinner;

    //test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
        initView();
    }

    private void initView() {

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Complaint's");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left, R.anim.fui_slide_in_right);
//                Intent intent = new Intent(getApplicationContext(),CustomerComplaintActivity.class);
//                startActivity(intent);
                onBackPressed();
            }
        });

        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);

        progressDialog = new ProgressDialog(com.example.awizom.jihuzur.CustomerActivity.CustomerComplaintActivity.this);

        addComplaint = findViewById(R.id.addComplaint);
        editcomplaint = findViewById(R.id.complaint);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        spinner = findViewById(R.id.spinner2);
        addComplaint.setOnClickListener(this);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String createComplaint = parent.getItemAtPosition(position).toString();

                if (createComplaint.equals("Create Complaint")) {
                    //Toast.makeText(getApplicationContext(), "" + createComplaint, Toast.LENGTH_SHORT).show();
                    showCreateComplaintDialog();
                }
                String activeComplaint = parent.getItemAtPosition(position).toString();
                if (activeComplaint.equals("Active Complaint")) {
                   // Toast.makeText(getApplicationContext(), "" + activeComplaint, Toast.LENGTH_SHORT).show();
                    String status = "False";
                    getComplaintList(status);
                }
                String solvedComplaint = parent.getItemAtPosition(position).toString();
                if (solvedComplaint.equals("Solved Complaint")) {
                   // Toast.makeText(getApplicationContext(), "" + solvedComplaint, Toast.LENGTH_SHORT).show();
                    String status = "True";
                    getComplaintList(status);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getComplaintList(String status) {

        String customerId = SharedPrefManager.getInstance(CustomerComplaintActivity.this).getUser().getID();
        try {
            result = new CustomerOrderHelper.GETCustomerComplaint().execute(customerId).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Complaint>>() {
            }.getType();
            complaintlist = new Gson().fromJson(result, listType);
            customerComplainAdapetr = new CustomerComplaintListAdapter(CustomerComplaintActivity.this, complaintlist);
            recyclerView.setAdapter(customerComplainAdapetr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCreateComplaintDialog() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_customercomplaint, null);
        dialogBuilder.setView(dialogView);
        editcomplaintinDialog = (AutoCompleteTextView) dialogView.findViewById(R.id.editComplaint);
        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddService);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Complaint");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  if(!editcomplaintinDialog.getText().toString().trim().equals("")) {
                      String complaint = editcomplaintinDialog.getText().toString();
                      String Active = "True";
                      String Status = "False";
                      String customerId = SharedPrefManager.getInstance(CustomerComplaintActivity.this).getUser().getID();
                      try {
                          result = new CustomerOrderHelper.CustomerPOSTComplaint().execute(customerId, complaint, Active, Status).get();
                          Gson gson = new Gson();
                          final Result jsonbodyres = gson.fromJson(result, Result.class);
                          Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                         
                      } catch (Exception e) {
                          e.printStackTrace();
                          Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                          // System.out.println("Error: " + e);
                      }
                  }else {
                      Toast.makeText(getApplicationContext(), "Please enter the value first", Toast.LENGTH_SHORT).show();
                      b.dismiss();

                  }


                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerComplaintActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
                spinner.setAdapter(arrayAdapter);
                b.dismiss();


            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CustomerComplaintActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);
                spinner.setAdapter(arrayAdapter);
              b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });
        b.setCancelable(false);
        b.setCanceledOnTouchOutside(false);
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == addComplaint.getId()) {

            progressDialog.setMessage("Adding in progress ...");
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                        AddComplaint();

                }
            }, TIMER);

        }
    }

    private void AddComplaint() {

        String complaint=editcomplaint.getText().toString();
        String Active = "True";
        String Status = "False";
        String customerId = SharedPrefManager.getInstance(CustomerComplaintActivity.this).getUser().getID();

        try {

            result = new CustomerOrderHelper.CustomerPOSTComplaint().execute(customerId, complaint, Active, Status).get();
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

