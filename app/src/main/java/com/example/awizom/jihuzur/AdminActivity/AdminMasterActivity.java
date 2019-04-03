package com.example.awizom.jihuzur.AdminActivity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;

public class AdminMasterActivity extends AppCompatActivity implements View.OnClickListener {
    CardView homecleaning, appliance;

    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_master);
        viewDialog = new ViewDialog(this);
        initView();

    }

    private void initView() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("MasterPage");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        homecleaning = (CardView) findViewById(R.id.homeCleancardViewOne);
        appliance = (CardView) findViewById(R.id.appliancecardview);
        appliance.setOnClickListener(this);
        homecleaning.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {

        if (v.getId() == homecleaning.getId()) {
           showCustomLoadingDialog(v);
            Intent intent = new Intent(AdminMasterActivity.this, AdminCategoryActivity.class);
            intent.putExtra("CatalogName", "Home Cleaning & Repairs");
            startActivity(intent);


        }
        if (v.getId() == appliance.getId()) {
            showCustomLoadingDialog(v);
            Intent intent = new Intent(AdminMasterActivity.this, AdminCategoryActivity.class);
            intent.putExtra("CatalogName", "Appliance & Repairs");
            startActivity(intent);


        }

    }
}
