package com.example.awizom.jihuzur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.awizom.jihuzur.Adapter.CurrentOrderAdapter;
import com.example.awizom.jihuzur.Helper.GetMyOrderRunningHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class CurrentOrderActivity extends AppCompatActivity {

    private String result="",userId;
    List<Order> orderList;
    CurrentOrderAdapter currentOrderAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_activity);

        InitView();
    }

    private void InitView() {
       userId= SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getMyOrderRunning();
    }

    private void getMyOrderRunning() {
        try {
            result   = new GetMyOrderRunningHelper.GetMyOrderRunning().execute(userId).get();
            String ss = result.toString();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
            currentOrderAdapter = new CurrentOrderAdapter(CurrentOrderActivity.this, orderList);
            recyclerView.setAdapter(currentOrderAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
