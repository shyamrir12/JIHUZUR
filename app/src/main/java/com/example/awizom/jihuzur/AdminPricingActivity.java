package com.example.awizom.jihuzur;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Result;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminPricingActivity extends AppCompatActivity {

    FloatingActionButton addPricing;
    ProgressDialog progressDialog;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pricing);
        getCatalog();
        addPricing = (FloatingActionButton) findViewById(R.id.addPricing);
        progressDialog = new ProgressDialog(this);
        addPricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPricingDialog();
            }
        });
    }

    private void showAddPricingDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_pricing_layout, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView editdescription = (AutoCompleteTextView) dialogView.findViewById(R.id.editDescription);
        final AutoCompleteTextView addpricing = (AutoCompleteTextView) dialogView.findViewById(R.id.addPricingTerms);
        final AutoCompleteTextView editamount = (AutoCompleteTextView) dialogView.findViewById(R.id.editAmount);
        final AutoCompleteTextView editCatalog = (AutoCompleteTextView) dialogView.findViewById(R.id.catalogID);
        editCatalog.setAdapter(adapter);


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
                String catalogID = editCatalog.getText().toString();

                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new AdminPricingActivity.POSTPricing().execute(description, pricing, amount, catalogID);
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


    private void getCatalog() {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            new AdminPricingActivity.GETCatalogList().execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }

    private class GETCatalogList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetCatalogService");


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

                    //json data fetch
                    JSONArray json = new JSONArray(result);
                    List<String> responseList = new ArrayList<String>();


                    for (int i = 0; i < json.length(); i++) {
                        final JSONObject e = json.getJSONObject(i);
                        String servicename = e.getString("ServiceName");
                        String catalogIds = e.getString("CatalogID");
                        responseList.add(catalogIds + "--" + servicename);

                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, responseList);
                    }


                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
    }


    private class POSTPricing extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String description = params[0];
            String pricing = params[1];
            String amount = params[2];
            String catalogID = params[3];

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


                parameters.add("CatalogID", catalogID.split("-")[0]);

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
