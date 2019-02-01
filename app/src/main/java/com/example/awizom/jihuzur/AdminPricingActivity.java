package com.example.awizom.jihuzur;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CatalogListAdapter;
import com.example.awizom.jihuzur.Adapter.PricingListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminPricingActivity extends AppCompatActivity {

    FloatingActionButton addPricing;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ArrayAdapter<String> adapter;
    List<PricingView> pricingList;
    PricingListAdapter adapterPricingList;
    String serviceID,serviceName;
    TextView servicename;
    String pricingSlots;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pricing);


        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        servicename=(TextView)findViewById(R.id.servicePricing);
        recyclerView.setHasFixedSize(true);
        serviceID=getIntent().getStringExtra("serviceID");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void getPricing()  {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            new AdminPricingActivity.GETPricingList().execute(String.valueOf(serviceID));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }
    private class GETPricingList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String serviceid=params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetPricing/"+ serviceid);


                okhttp3.Response response = client.newCall(builder.build()).execute();
                if (response.isSuccessful()) {
                    json = response.body().string();
                }
            } catch (Exception e) {
                e.printStackTrace();
//                mSwipeRefreshLayout.setRefreshing(false);
                // System.out.println("Error: " + e);
//                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            try {
                if (result.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
                } else {

                    //System.out.println(result);
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<PricingView>>() {
                    }.getType();
                    pricingList = new Gson().fromJson(result, listType);


                    adapterPricingList = new PricingListAdapter(getBaseContext(),pricingList);

                    recyclerView.setAdapter(adapterPricingList);

                    pricingList.get(0).getServiceName();
                    serviceName=pricingList.get(0).getServiceName();

                    servicename.setText(serviceName + " Pricing");




                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
    }

    private void showAddPricingDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_pricing_layout, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView editdescription = (AutoCompleteTextView) dialogView.findViewById(R.id.editDescription);
        final AutoCompleteTextView addpricing = (AutoCompleteTextView) dialogView.findViewById(R.id.addPricingTerms);
        final AutoCompleteTextView editamount = (AutoCompleteTextView) dialogView.findViewById(R.id.editAmount);

        final Spinner pricingslot=(Spinner)dialogView.findViewById(R.id.pricingslot);
        pricingslot.setVisibility(View.GONE);


        pricingslot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();
                pricingSlots=item.split(" ")[0];

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Spinner Drop down elements
        List<String> PricingSlot = new ArrayList<String>();
        PricingSlot.add("0.5 hour");
        PricingSlot.add("1 hour");
        PricingSlot.add("1.5 hour");
        PricingSlot.add("2.0 hour");
        PricingSlot.add("2.5 hour");
        PricingSlot.add("3.0 hour");
        PricingSlot.add("0 hour");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, PricingSlot);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        pricingslot.setAdapter(dataAdapter);


//        final AutoCompleteTextView pricingStart=(AutoCompleteTextView)dialogView.findViewById(R.id.pricingStart) ;
//        final AutoCompleteTextView pricingEnd=(AutoCompleteTextView)dialogView.findViewById(R.id.pricingEnd);
//        final AutoCompleteTextView pricingEndSlot=(AutoCompleteTextView)dialogView.findViewById(R.id.pricingEndslot) ;
//        final AutoCompleteTextView startAmount=(AutoCompleteTextView)dialogView.findViewById(R.id.startAmount);
//
//        pricingStart.setVisibility(View.GONE);
//        pricingEnd.setVisibility(View.GONE);
//        pricingEndSlot.setVisibility(View.GONE);
//        startAmount.setVisibility(View.GONE);

        final RadioButton range=(RadioButton)dialogView.findViewById(R.id.range);
        final RadioButton fix=(RadioButton)dialogView.findViewById(R.id.fix);



        range.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                pricingStart.setVisibility(View.VISIBLE);
//                pricingEnd.setVisibility(View.VISIBLE);
//                pricingEndSlot.setVisibility(View.VISIBLE);
//                startAmount.setVisibility(View.VISIBLE);

                pricingslot.setVisibility(View.VISIBLE);

            }
        });

        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddCatalog);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Pricing");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = editdescription.getText().toString().trim();
                String pricing = addpricing.getText().toString().trim();
                String amount = editamount.getText().toString().trim();




                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new AdminPricingActivity.POSTPricing().execute(description, pricing, amount,serviceID,pricingSlots);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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


    private class POSTPricing extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String description = params[0];
            String pricing = params[1];
            String amount = params[2];
            String catalogID = params[3];
            String pricingslots = params[4];



            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "AddPricing");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("PricingID", "0");
                parameters.add("Description", description);
                parameters.add("PricingTerms", pricing);
                parameters.add("Amount", amount);
                parameters.add("CatalogID",catalogID);
                parameters.add("PricingSlot",pricingslots);
                parameters.add("PricingEndSlot","0");


//                parameters.add("CatalogID", catalogID.split("-")[0]);

                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }


        }

    }


}
