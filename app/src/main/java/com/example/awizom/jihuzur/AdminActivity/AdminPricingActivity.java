package com.example.awizom.jihuzur.AdminActivity;

import android.app.ProgressDialog;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.PricingListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AdminPricingActivity extends AppCompatActivity {

    FloatingActionButton addPricing;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayAdapter<String> adapter;
    List<PricingView> pricingList;
    PricingListAdapter adapterPricingList;
    String[] pricinglistString;
    String serviceID, serviceName, displayType;
    TextView servicename;
    String pricingSlots, pricingType, pricingendSlot, DisplayType;
    Integer checkValue;
    LinearLayout layout;
    LinearLayout.LayoutParams lparams;
    TextView tv;
    String result = "";
    android.support.v7.widget.Toolbar toolbar;
    SwipeRefreshLayout mSwipeRefreshLayout;
    AutoCompleteTextView editdescription, addpricingterms, editamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pricing);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        String servicenameForToolbar = getIntent().getStringExtra("serviceName");
        toolbar.setTitle(servicenameForToolbar + " Pricing");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        layout = (LinearLayout) findViewById(R.id.ll1);
        lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        servicename = (TextView) findViewById(R.id.servicePricing);
        recyclerView.setHasFixedSize(true);
        serviceID = getIntent().getStringExtra("serviceID");
        displayType = getIntent().getStringExtra("displayType");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getPricing();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        addPricing = (FloatingActionButton) findViewById(R.id.addPricing);
        progressDialog = new ProgressDialog(this);
        addPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPricingDialog();
            }
        });
        getPricing();
    }

    public void getPricing() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GETPricingList().execute(String.valueOf(serviceID)).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                //System.out.println(result);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<PricingView>>() {
                }.getType();
                pricingList = new Gson().fromJson(result, listType);
                pricinglistString = new String[pricingList.size()];
                for (int i = 0; i < pricingList.size(); i++) {
                    pricinglistString[i] = String.valueOf(pricingList.get(i).getPricingEnd());
                    tv = new TextView(AdminPricingActivity.this);
                    tv.setLayoutParams(lparams);
                    tv.setTextColor(Color.parseColor("#000000"));
                    tv.setTextSize(20);
                    tv.setText(pricinglistString[i] + "->");
                    tv.setVisibility(View.GONE);
                    layout.addView(tv);
                }

                adapterPricingList = new PricingListAdapter(AdminPricingActivity.this, pricingList, displayType);
                recyclerView.setAdapter(adapterPricingList);
                mSwipeRefreshLayout.setRefreshing(false);
                pricingList.get(0).getServiceName();
                serviceName = pricingList.get(0).getServiceName();
                servicename.setText(serviceName + " Pricing");
                checkValue = pricingList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddPricingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_pricing_layout, null);
        dialogBuilder.setView(dialogView);
        editdescription = (AutoCompleteTextView) dialogView.findViewById(R.id.editDescription);
        addpricingterms = (AutoCompleteTextView) dialogView.findViewById(R.id.addPricingTerms);
        editamount = (AutoCompleteTextView) dialogView.findViewById(R.id.editAmount);
        final AutoCompleteTextView noOfItems = (AutoCompleteTextView) dialogView.findViewById(R.id.numberItems);
        final AutoCompleteTextView pricingEndSlot = (AutoCompleteTextView) dialogView.findViewById(R.id.prizingEndSlot);
        pricingEndSlot.setVisibility(View.GONE);
        noOfItems.setVisibility(View.GONE);
        final Spinner pricingslot = (Spinner) dialogView.findViewById(R.id.pricingslot);
        pricingslot.setVisibility(View.GONE);
        pricingslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                if (item != "end") {
                    pricingSlots = item.split(" ")[0];
                    pricingEndSlot.setVisibility(View.GONE);
                    // Showing selected spinner item
                    /*  Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/

                } else {
                    pricingSlots = "0";
                    pricingEndSlot.setVisibility(View.VISIBLE);
                    // Showing selected spinner item
                    /*   Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();*/
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        if (checkValue == null) {
            List<String> PricingSlot = new ArrayList<String>();
            PricingSlot.add("0.5 hour");
            PricingSlot.add("1 hour");
            PricingSlot.add("1.5 hour");
            PricingSlot.add("2.0 hour");
            PricingSlot.add("2.5 hour");
            PricingSlot.add("3.0 hour");
            pricingendSlot = "0";
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PricingSlot);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            pricingslot.setAdapter(dataAdapter);
        }
        // Spinner Drop down elements

        else {
            List<String> PricingSlot = new ArrayList<String>();
            PricingSlot.add("0.5 hour");
            PricingSlot.add("1 hour");
            PricingSlot.add("1.5 hour");
            PricingSlot.add("2.0 hour");
            PricingSlot.add("2.5 hour");
            PricingSlot.add("3.0 hour");
            PricingSlot.add("end");
            // Creating adaptr for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PricingSlot);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            pricingslot.setAdapter(dataAdapter);
        }

        if (displayType.equals("")) {

            pricingType = "Range";
            noOfItems.setVisibility(View.GONE);
            pricingslot.setVisibility(View.VISIBLE);
        } else {
            pricingslot.setVisibility(View.GONE);
            pricingEndSlot.setVisibility(View.GONE);
            noOfItems.setVisibility(View.VISIBLE);
            pricingType = "Fix";
            pricingendSlot = "0.0";
        }

        final Button buttonAddPricing = (Button) dialogView.findViewById(R.id.buttonAddPricing);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Pricing");
        dialogBuilder.setIcon(R.drawable.dollarmoney);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    String description = editdescription.getText().toString().trim();
                    String pricing = addpricingterms.getText().toString().trim();
                    String amount = editamount.getText().toString().trim();
                    pricingendSlot = pricingEndSlot.getText().toString();
                    String pricingid = "0";
                    if (pricingType == "Fix") {
                        pricingSlots = noOfItems.getText().toString();
                    } else {
                    }
                    try {
                        //String res="";
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        result = new AdminHelper.POSTPricing().execute(description, pricing, amount, serviceID, pricingSlots, pricingType, pricingendSlot, pricingid).get();

                        if (result.isEmpty()) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                        } else {
                            //System.out.println("CONTENIDO:  " + result);
                            Gson gson = new Gson();
                            final Result jsonbodyres = gson.fromJson(result, Result.class);
                            Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                            getPricing();
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        // System.out.println("Error: " + e);
                    }
                    b.dismiss();
                }
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

    private boolean validation() {

        if (addpricingterms.getText().toString().isEmpty()) {
            addpricingterms.setError("Enter valid Pricing Terms");
            addpricingterms.requestFocus();
            return false;
        }
        if (editdescription.getText().toString().isEmpty()) {
            editdescription.setError("Enter valid description about pricing");
            editdescription.requestFocus();
            return false;
        }
        if (editamount.getText().toString().isEmpty()) {
            editamount.setError("Please Enter Amount");
            editamount.requestFocus();
            return false;
        }
        return true;
    }


}
