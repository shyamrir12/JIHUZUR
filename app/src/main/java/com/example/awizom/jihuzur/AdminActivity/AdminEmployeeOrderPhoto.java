package com.example.awizom.jihuzur.AdminActivity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awizom.jihuzur.Adapter.AdminOrderPhotoAdapter;
import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.OrderPhoto;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AdminEmployeeOrderPhoto extends AppCompatActivity {

    RecyclerView recyclerView;

    String result="";
    List<OrderPhoto> orderPhotoList;
    AdminOrderPhotoAdapter adminOrderPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_employee_order_photo);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/

               toolbar.setTitle("Order Photo");
          toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getORderPhoto();
    }

    private void getORderPhoto() {
        try {
          //  mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GetOrderPhoto().execute().get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<OrderPhoto>>() {
            }.getType();
            orderPhotoList = new Gson().fromJson(result, listType);
            adminOrderPhotoAdapter = new AdminOrderPhotoAdapter(AdminEmployeeOrderPhoto.this, orderPhotoList);

            recyclerView.setAdapter(adminOrderPhotoAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
