package com.example.awizom.jihuzur;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CatalogGridViewAdapter;
import com.example.awizom.jihuzur.Adapter.CategoryGridViewAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminCategoryActivity extends AppCompatActivity {
GridView gridView;
String catalogName;
    private String[]categoryList, categoryNameList;
    int[] gridViewImageId = {
            R.drawable.home_cleaning,   R.drawable.home_cleaning,   R.drawable.home_cleaning,
            R.drawable.home_cleaning,   R.drawable.home_cleaning,   R.drawable.home_cleaning,
            R.drawable.home_cleaning,   R.drawable.home_cleaning,   R.drawable.home_cleaning,
            R.drawable.home_cleaning,   R.drawable.home_cleaning,   R.drawable.home_cleaning,
            R.drawable.home_cleaning,   R.drawable.home_cleaning,   R.drawable.home_cleaning,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        catalogName=getIntent().getStringExtra("Catalogname");
        gridView=(GridView) findViewById(R.id.gridview);

        getCategoryList();

    }

    private void getCategoryList() {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            new AdminCategoryActivity.GETCategoryList().execute(catalogName.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
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
                    categoryList = new Gson().fromJson(result, listType);
                    CategoryGridViewAdapter adaptercatalog = new CategoryGridViewAdapter(AdminCategoryActivity.this, categoryList, gridViewImageId);

                    gridView.setAdapter(adaptercatalog);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
    }
}
