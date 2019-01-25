package com.example.awizom.jihuzur;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Result;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminCatalogActivity extends AppCompatActivity {

    FloatingActionButton addCatalog;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catalog);
        addCatalog=(FloatingActionButton) findViewById(R.id.addCatalog);
        progressDialog = new ProgressDialog(this);
        addCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCatalogDialog();
            }
        });
    }


    private void showAddCatalogDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_catalog_layout, null);
        dialogBuilder.setView(dialogView);

        final EditText editCatalogName = (EditText) dialogView.findViewById(R.id.editCatalogName);
        final EditText addCategory = (EditText) dialogView.findViewById(R.id.addCategory);
        final EditText serviceName = (EditText) dialogView.findViewById(R.id.serviceName);
        final EditText description = (EditText) dialogView.findViewById(R.id.description);


        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddCatalog);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Catalog");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String catalogName = editCatalogName.getText().toString().trim();
                String category = addCategory.getText().toString().trim();
                String service = serviceName.getText().toString().trim();
                String descriptions = description.getText().toString().trim();

                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                   new  AdminCatalogActivity.POSTCatalog().execute(catalogName, category, service, descriptions);
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

    private class POSTCatalog extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String catalogname = params[0];
            String category = params[1];
            String service = params[2];
            String description = params[3];


            String json = "";
            try {

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "CreateCatalog");
                builder.addHeader("Content-Type", "application/x-www-form-urlencoded");
                builder.addHeader("Accept", "application/json");
                //builder.addHeader("Authorization", "Bearer " + accesstoken);

                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogID", "0");
                parameters.add("CatalogName", catalogname);
                parameters.add("Category", category);
                parameters.add("ServiceName", service);
                parameters.add("Description", description);

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
