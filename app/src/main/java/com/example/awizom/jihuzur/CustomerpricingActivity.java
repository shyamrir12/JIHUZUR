package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import com.example.awizom.jihuzur.Adapter.CustomerPricingAdapter;
import com.example.awizom.jihuzur.Helper.CustomerGetMyOrderRunningHelper;
import com.example.awizom.jihuzur.Model.PricingView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerpricingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton;
    private Intent intent;
    List<PricingView> pricingViewsList;
    CustomerPricingAdapter repairAndServiceAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    private String result="",serviceID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_service);
        initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Customer Pricing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        serviceID=getIntent().getStringExtra("serviceID");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);
        getMyOrderRunning();
    }


    private void getMyOrderRunning() {
        try {
            result   = new CustomerGetMyOrderRunningHelper.GetCustomerPricing().execute(serviceID.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PricingView>>() {
            }.getType();
            pricingViewsList = new Gson().fromJson(result, listType);
            repairAndServiceAdapter = new CustomerPricingAdapter(CustomerpricingActivity.this, pricingViewsList);
            recyclerView.setAdapter(repairAndServiceAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonNext:
                intent = new Intent(this,LocationActivity.class);
                startActivity(intent);
                break;
        }
    }
}
