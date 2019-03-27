package com.example.awizom.jihuzur.AdminActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CatalogGridViewAdapter;
import com.example.awizom.jihuzur.Adapter.CatalogListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminCatalogActivity extends AppCompatActivity {

    FloatingActionButton addCatalog;
    AutoCompleteTextView editCatalogName, addCategory;
    ProgressDialog progressDialog;
    GridView gridview;
    List<Catalog> catalogList;
    String catalogname;
    ArrayAdapter<String> adapter;
    CatalogListAdapter adapterCatalogList;
    ArrayAdapter<String> adaptercategory;
    Intent intent;
    int[] gridViewImageId = {
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,
    };
    private String[] catalogNameList, categoryNameList;
//test
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_catalog);
        addCatalog = (FloatingActionButton) findViewById(R.id.addCatalog);
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position >= 0) {
                    Toast.makeText(getApplicationContext(), ((TextView) view.findViewById(R.id.catalogName)).getText(), Toast.LENGTH_SHORT).show();
                    intent = new Intent(AdminCatalogActivity.this, AdminCategoryActivity.class);
                    intent.putExtra("Catalogname", ((TextView) view.findViewById(R.id.catalogName)).getText());
                    startActivity(intent);
               }

            }
        });
        progressDialog = new ProgressDialog(this);
        getCatalogName();
//        getCatalogList();
        addCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCatalogDialog(catalogname);
            }
        });
    }

//    private void getCatalogList() {
//        try {
////            mSwipeRefreshLayout.setRefreshing(true);
//            new GETCatalogList().execute();
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
////            mSwipeRefreshLayout.setRefreshing(false);
//            // System.out.println("Error: " + e);
//        }
//
//
//    }

    private void showAddCatalogDialog(String catalogname) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_catalog_layout, null);
        dialogBuilder.setView(dialogView);
        editCatalogName = (AutoCompleteTextView) dialogView.findViewById(R.id.editCatalogName);
        editCatalogName.setAdapter(adapter);
        addCategory = (AutoCompleteTextView) dialogView.findViewById(R.id.addCategory);
        final AutoCompleteTextView serviceName = (AutoCompleteTextView) dialogView.findViewById(R.id.serviceName);
        final AutoCompleteTextView description = (AutoCompleteTextView) dialogView.findViewById(R.id.description);
        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddPricing);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Catalog");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        editCatalogName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addCategory.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                ///getDesignList();
                if (editCatalogName.getText().length() > 0) {
                    getCategory();
                }

            }
        });


        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String catalogName = editCatalogName.getText().toString();
                String category = addCategory.getText().toString().trim();
                String service = serviceName.getText().toString().trim();
                String descriptions = description.getText().toString().trim();

                try {
                    //String res="";
                    progressDialog.setMessage("loading...");
                    progressDialog.show();
                    new AdminCatalogActivity.POSTCatalog().execute(catalogName, category, service, descriptions);
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

    private void getCatalogName() {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            new GETCatalogNameList().execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }

    private void getCategory() {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            new GETCategoryList().execute(editCatalogName.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }

    private class GETCatalogNameList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetCatalogName/");


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
                    Type listType = new TypeToken<String[]>() {
                    }.getType();
                    catalogNameList = new Gson().fromJson(result, listType);
                    CatalogGridViewAdapter adaptercatalog = new CatalogGridViewAdapter(AdminCatalogActivity.this, catalogNameList, gridViewImageId);
                    gridview.setAdapter(adaptercatalog);
               }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
    }

    private class GETCategoryList extends AsyncTask<String, Void, String> implements View.OnClickListener {
        @Override
        protected String doInBackground(String... params) {

            String json = "";
            String catalogNameOne = params[0];


            try {
                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                builder.url(AppConfig.BASE_URL_API_Admin + "GetCategoryName");

                builder.addHeader("Content-Type", "Application/json");
                builder.addHeader("Accept", "application/json");


                FormBody.Builder parameters = new FormBody.Builder();
                parameters.add("CatalogName", catalogNameOne);
                builder.post(parameters.build());


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
                    Type listType = new TypeToken<String[]>() {
                    }.getType();
                    categoryNameList = new Gson().fromJson(result, listType);

                    adaptercategory = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, categoryNameList);
                    addCategory.setThreshold(1);
                    addCategory.setAdapter(adaptercategory);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
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


//    private class GETCatalogList extends AsyncTask<String, Void, String> implements View.OnClickListener {
//        @Override
//        protected String doInBackground(String... params) {
//
//            String json = "";
//
//
//            try {
//                OkHttpClient client = new OkHttpClient();
//                Request.Builder builder = new Request.Builder();
//                builder.url(AppConfig.BASE_URL_API_Admin + "GetCatalogList");
//
//
//                okhttp3.Response response = client.newCall(builder.build()).execute();
//                if (response.isSuccessful()) {
//                    json = response.body().string();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
////                mSwipeRefreshLayout.setRefreshing(false);
//                // System.out.println("Error: " + e);
////                Toast.makeText(getContext(),"Error: " + e,Toast.LENGTH_SHORT).show();
//            }
//            return json;
//        }
//
//        protected void onPostExecute(String result) {
//
//            try {
//                if (result.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    //System.out.println(result);
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<Catalog>>() {
//                    }.getType();
//                    catalogList = new Gson().fromJson(result, listType);
//                    adapterCatalogList = new CatalogListAdapter(getBaseContext(),catalogList);
//
//                    recyclerView.setAdapter(adapterCatalogList);
//
//
//
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        @Override
//        public void onClick(View v) {
//
//        }
//    }
}
