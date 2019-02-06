package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPricingAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.DiscountHelper;
import com.example.awizom.jihuzur.LocationActivity;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerpricingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton; private Button postPricingBtn;
    private Intent intent;
    List<PricingView> pricingViewsList;
    CustomerPricingAdapter repairAndServiceAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    private String result="",serviceID="",description="",serviceName="",displayType="",btn="",orderID="",priceID="";

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
        description=getIntent().getStringExtra("description");
        serviceName=getIntent().getStringExtra("serviceName");
        displayType=getIntent().getStringExtra("DisplayType");
        btn = getIntent().getStringExtra("button");
        orderID=getIntent().getStringExtra("orderId");
        priceID=getIntent().getStringExtra("priceId");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);
        postPricingBtn = findViewById(R.id.postOrderPriceBtn);
        postPricingBtn.setOnClickListener(this);


        if(btn.equals("empBtn")){
            nextButton.setVisibility(View.GONE);
        }else if(btn.equals("serBtn")){
            nextButton.setVisibility(View.VISIBLE);
        }


        if(btn.equals("empBtn")){
            postPricingBtn.setVisibility(View.VISIBLE);
        }else if(btn.equals("serBtn")){
            postPricingBtn.setVisibility(View.GONE);
        }

        getMyOrderRunning();
    }


    private void getMyOrderRunning() {
        try {
            result   = new CustomerOrderHelper.GetCustomerPricing().execute(serviceID.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PricingView>>() {
            }.getType();
            pricingViewsList = new Gson().fromJson(result, listType);
            repairAndServiceAdapter = new CustomerPricingAdapter(CustomerpricingActivity.this,
                    pricingViewsList,displayType,orderID,priceID,btn);
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
            int i = SharedPrefManager.getInstance(CustomerpricingActivity.this).getPricingID().PricingID;
                intent = new Intent(this,LocationActivity.class);
                intent.putExtra("PricingID",i);
                startActivity(intent);

                break;

            case R.id.postOrderPriceBtn:
                int j = SharedPrefManager.getInstance(CustomerpricingActivity.this).getPricingID().PricingID;
                try {
                    result = new DiscountHelper.EditPricingPost().execute(orderID, String.valueOf(j)).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                break;

        }
    }
}
