package com.example.awizom.jihuzur;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Adapter.DiscountListAdapter;
import com.example.awizom.jihuzur.AdminActivity.AdminDiscountActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCommentAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.DiscountImageAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerCommentActivity;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Discount;
import com.example.awizom.jihuzur.Model.DiscountModel;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.Review;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AutoFlipper extends Activity {

    String result="";
    DiscountImageAdapter discountImageAdapter;


    RecyclerView recyclerView;
    private List<DiscountModel> discountModel;
    private  ViewFlipper viewFlipper;
    private Button recalls;
    String imgstr="";
    private String[] imageList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_flipper);

        viewFlipper = findViewById(R.id.viewflipper);
        recalls = findViewById(R.id.recall);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDiscountImageList();
        recalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDiscountImageList();
            }
        });
    }
    private void getDiscountImageList() {

        try {
            result = new CustomerOrderHelper.GETDiscountList().execute().get();

            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiscountView>>() {
                }.getType();
                discountModel = new Gson().fromJson(result, listType);
                discountImageAdapter = new DiscountImageAdapter(getApplicationContext(), discountModel);
                recyclerView.setAdapter(discountImageAdapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    }
