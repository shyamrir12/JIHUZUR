package com.example.awizom.jihuzur.AdminActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CategoryGridViewAdapter;
import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.List;

public class AdminCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PHOTO = 100;
    GridView gridView;
    String catalogName;
    FloatingActionButton addCategory;
    CategoryGridViewAdapter adaptercatalog;
    AutoCompleteTextView categoryNames;
    List<Catalog> categorylist;
    CategoryListAdapter adapterCategoryList;
    String result = "";
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ImageView imageView;
    private String[] categoryList;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ViewDialog viewDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
      /*  catalogName = getIntent().getStringExtra("CatalogName");*/

        catalogName="Home Cleaning & Repairs";
        toolbar.setTitle("Category's");
        viewDialog = new ViewDialog(this);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        addCategory = (FloatingActionButton) findViewById(R.id.addCategory);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {

                    getCategoryList();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
        addCategory.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridview);
        getCategoryList();

    }
    public void showCustomLoadingDialog(View view) {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }
    private void getCategoryList() {

        try {

            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GETCategoryList().execute(catalogName.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Catalog>>() {
            }.getType();
            categorylist = new Gson().fromJson(result, listType);
            adapterCategoryList = new CategoryListAdapter(AdminCategoryActivity.this, categorylist);
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setAdapter(adapterCategoryList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addCategory.getId()) {

            String categorynem = null, cetlogId = null, cetlogName = null;
            showAddCategoryDialog(categorynem, cetlogId, cetlogName);
        }
    }

    public void showAddCategoryDialog(String categorynem, final String cetlogId, String cetlogName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
        categoryNames = (AutoCompleteTextView) dialogView.findViewById(R.id.editCategory);
        if (categorynem != null) {
            categoryNames.setText(String.valueOf(categorynem));
        }

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
        dialogBuilder.setIcon(R.drawable.category);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    progressDialog = new ProgressDialog(AdminCategoryActivity.this);
                    progressDialog.setMessage("Loading..."); // Setting Message
                    progressDialog.setTitle("ProgressDialog"); // Setting Title
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                    progressDialog.show(); // Display Progress Dialog
                    progressDialog.setCancelable(false);

                    String categoryName = categoryNames.getText().toString().trim();

                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] image = stream.toByteArray();
                    System.out.println("byte array:" + image);

                    String img_str = Base64.encodeToString(image, 0);
                    String catalogID;
                    if (cetlogId != null) {
                        catalogID = cetlogId;
                    } else {
                        catalogID = "0";
                    }

                    try {
                        result = new AdminHelper.POSTCategory().execute(catalogName, catalogID, categoryName, img_str).get();
                        Gson gson = new Gson();
                        final Result jsonbodyres = gson.fromJson(result, Result.class);
                        Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                        getCategoryList();
                        progressDialog.dismiss();
                    } catch (Exception e) {

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

        if (categoryNames.getText().toString().isEmpty()) {
            categoryNames.setError("Enter a valid Category Name");
            categoryNames.requestFocus();
            return false;
        }
        return true;
    }

    public void openGallery() {
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
