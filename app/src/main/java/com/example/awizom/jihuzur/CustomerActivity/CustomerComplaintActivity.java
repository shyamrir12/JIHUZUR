package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ActivityOptions;
import android.os.Bundle;
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
import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.Adapter.CustomerComplaintListAdapter;
import com.example.awizom.jihuzur.Helper.CustomerComplaintHelper;
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
    AutoCompleteTextView editcomplaint,editcomplaintinDialog;
    String result = "";
    List<Complaint> complaintlist;
    RecyclerView recyclerView;
    CustomerComplaintListAdapter customerComplainAdapetr;
    String[] SPINNERLIST = { "Active Complaint","Create Complaint", "Solved Complaint"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_complaint);
        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Customer's Complaint");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left, R.anim.fui_slide_in_right);
               onBackPressed();
            }
        });
        addComplaint = (FloatingActionButton) findViewById(R.id.addComplaint);
        editcomplaint = (AutoCompleteTextView) findViewById(R.id.complaint);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Spinner  spinner=(Spinner)findViewById(R.id.spinner2);
        addComplaint.setOnClickListener(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, SPINNERLIST);

        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String createComplaint = parent.getItemAtPosition(position).toString();
                if(createComplaint.equals("Create Complaint"))
                {
                    Toast.makeText(getApplicationContext(), ""+createComplaint, Toast.LENGTH_SHORT).show();
                    showCreateComplaintDialog();
                }

                String activeComplaint = parent.getItemAtPosition(position).toString();
                if(activeComplaint.equals("Active Complaint"))
                {
                    Toast.makeText(getApplicationContext(), ""+activeComplaint, Toast.LENGTH_SHORT).show();
                    String status = "False";
                    getComplaintList(status);
                }
                String solvedComplaint = parent.getItemAtPosition(position).toString();
                if(solvedComplaint.equals("Solved Complaint"))
                {
                    Toast.makeText(getApplicationContext(), ""+solvedComplaint, Toast.LENGTH_SHORT).show();
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
        String customerId=SharedPrefManager.getInstance(CustomerComplaintActivity.this).getUser().getID();

        try {
            result = new CustomerComplaintHelper.GETCustomerComplaint().execute(customerId).get();
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
        LayoutInflater inflater = getLayoutInflater();
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

                String complaint=editcomplaintinDialog.getText().toString();
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

