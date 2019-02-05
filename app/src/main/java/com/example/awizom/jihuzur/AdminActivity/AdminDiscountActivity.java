package com.example.awizom.jihuzur.AdminActivity;

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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import com.example.awizom.jihuzur.Adapter.DiscountListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminDiscountActivity extends AppCompatActivity {

    FloatingActionButton addDiscount;
    AutoCompleteTextView editDiscountName, editDiscountType,editDiscountAmount;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    String result="";
    List<DiscountView> discountlist;
    DiscountListAdapter discountListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_discount);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(" Discount offer");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addDiscount=(FloatingActionButton)findViewById(R.id.adddiscount);
        progressDialog = new ProgressDialog(this);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getPricingList();
        addDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAddDiscount();
            }
        });


    }


    private void getPricingList() {

        try {


            result = new AdminHelper.GETDiscountList().execute().get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiscountView>>() {
                }.getType();
                discountlist = new Gson().fromJson(result, listType);
                discountListAdapter = new DiscountListAdapter(AdminDiscountActivity.this, discountlist);
                recyclerView.setAdapter(discountListAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddDiscount() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_discount_alertlayout, null);
        dialogBuilder.setView(dialogView);


        editDiscountName = (AutoCompleteTextView) dialogView.findViewById(R.id.editDiscountName);
        editDiscountType = (AutoCompleteTextView) dialogView.findViewById(R.id.editDiscountType);
        editDiscountAmount = (AutoCompleteTextView) dialogView.findViewById(R.id.editDiscountAmount);
//
//        editCatalogName.setAdapter(adapter);
//
//        addCategory = (AutoCompleteTextView) dialogView.findViewById(R.id.addCategory);





        final Button buttonAddDiscount = (Button) dialogView.findViewById(R.id.buttonAddDiscount);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Discount");
        final AlertDialog b = dialogBuilder.create();
        b.show();





        buttonAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String discountName = editDiscountName.getText().toString();
                String discounttype = editDiscountType.getText().toString().trim();
                String discountamount = editDiscountAmount.getText().toString().trim();


                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new AdminDiscountActivity.POSTDiscount().execute(discountName, discounttype, discountamount);
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

    private class POSTDiscount extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String discountname = params[0];
            String discounttype = params[1];
            String discountamount = params[2];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "AddDiscount");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("DiscountID", "0");
                parameters.add("DiscountName", discountname);
                parameters.add("DiscountType", discounttype);
                parameters.add("Discount1", discountamount);

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
