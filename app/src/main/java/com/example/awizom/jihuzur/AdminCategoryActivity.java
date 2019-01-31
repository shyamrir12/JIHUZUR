package com.example.awizom.jihuzur;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CatalogGridViewAdapter;
import com.example.awizom.jihuzur.Adapter.CatalogListAdapter;
import com.example.awizom.jihuzur.Adapter.CategoryGridViewAdapter;
import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    GridView gridView;
    String catalogName;
    FloatingActionButton addCategory;
    CategoryGridViewAdapter adaptercatalog;
    AutoCompleteTextView categoryNames;
    List<Catalog> categorylist;
    CategoryListAdapter adapterCategoryList;
    private String[]categoryList;


    RecyclerView recyclerView;
    ImageView imageView;
    private final static int IMAGE_RESULT = 200;
    private static final int SELECT_PHOTO = 100;
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
        addCategory=(FloatingActionButton)findViewById(R.id.addCategory);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addCategory.setOnClickListener(this);
        catalogName=getIntent().getStringExtra("CatalogName");
        gridView=(GridView) findViewById(R.id.gridview);
        getSupportActionBar().setTitle(catalogName+"'s" + " Category");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

    @Override
    public void onClick(View v) {
        if(v.getId()==addCategory.getId())
        {
            showAddCategoryDialog();
        }
    }

    private void showAddCategoryDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
         categoryNames=(AutoCompleteTextView) dialogView.findViewById(R.id.editCategory);
         imageView=(ImageView) dialogView.findViewById(R.id.imageView);
        Button chooseImage=(Button) dialogView.findViewById(R.id.addImage);
       chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
    public void onClick(View v) {
        openGallery();

    }
});

        final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Category");
        final AlertDialog b = dialogBuilder.create();
        b.show();





        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = categoryNames.getText().toString().trim();

                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image=stream.toByteArray();
                System.out.println("byte array:"+image);

                String img_str = Base64.encodeToString(image, 0);




                try {
                    //String res="";
//                    progressDialog.setMessage("loading...");
//                    progressDialog.show();
                    new AdminCategoryActivity.POSTCategory().execute(catalogName, categoryName, img_str);
                } catch (Exception e) {
                    e.printStackTrace();
//                    progressDialog.dismiss();
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

    private void openGallery(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        imageView.setImageURI(selectedImage);
                    }
                }
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



                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Catalog>>() {
                    }.getType();
                    categorylist = new Gson().fromJson(result, listType);
                    adapterCategoryList = new CategoryListAdapter(getBaseContext(),categorylist);

                    recyclerView.setAdapter(adapterCategoryList);




                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {

        }
    }

    private class POSTCategory extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            //     InputStream inputStream
            String catalogname = params[0];
            String category = params[1];
            String image = params[2];


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
                parameters.add("Image", image);


                builder.post(parameters.build());


                okhttp3.Response response = client.newCall(builder.build()).execute();

                if (response.isSuccessful()) {
                    json = response.body().string();


                }
            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();
                // System.out.println("Error: " + e);
                Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
            }
            return json;
        }

        protected void onPostExecute(String result) {

            if (result.isEmpty()) {
//                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                //System.out.println("CONTENIDO:  " + result);
                Gson gson = new Gson();
                final Result jsonbodyres = gson.fromJson(result, Result.class);
                Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
//                progressDialog.dismiss();
            }


        }

    }
}
