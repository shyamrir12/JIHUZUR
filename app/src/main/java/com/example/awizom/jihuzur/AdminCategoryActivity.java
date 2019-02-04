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
import com.example.awizom.jihuzur.Helper.AdminHelper;
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
    private String[] categoryList;
    String result = "";


    RecyclerView recyclerView;
    ImageView imageView;

    private static final int SELECT_PHOTO = 100;
    int[] gridViewImageId = {
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,
            R.drawable.home_cleaning, R.drawable.home_cleaning, R.drawable.home_cleaning,

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        catalogName = getIntent().getStringExtra("CatalogName");
        toolbar.setTitle(catalogName + "'s" + " Category");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addCategory = (FloatingActionButton) findViewById(R.id.addCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addCategory.setOnClickListener(this);


        gridView = (GridView) findViewById(R.id.gridview);
        getCategoryList();

    }

    private void getCategoryList() {

        try {


            result = new AdminHelper.GETCategoryList().execute(catalogName.toString()).get();

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Catalog>>() {
            }.getType();
            categorylist = new Gson().fromJson(result, listType);
            adapterCategoryList = new CategoryListAdapter(AdminCategoryActivity.this, categorylist);
            recyclerView.setAdapter(adapterCategoryList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addCategory.getId()) {
            showAddCategoryDialog();
        }
    }

    public void showAddCategoryDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
        categoryNames = (AutoCompleteTextView) dialogView.findViewById(R.id.editCategory);
        imageView = (ImageView) dialogView.findViewById(R.id.imageView);
        Button chooseImage = (Button) dialogView.findViewById(R.id.addImage);
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

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image = stream.toByteArray();
                System.out.println("byte array:" + image);

                String img_str = Base64.encodeToString(image, 0);
                String catalogID="0";

                try {
                    result = new AdminHelper.POSTCategory().execute(catalogName,catalogID, categoryName, img_str).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                 Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
                } catch (Exception e) {

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

    private void openGallery() {
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


}
