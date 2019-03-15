package com.example.awizom.jihuzur.CustomerActivity.CustomerFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerHistoryAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerHistoryFragment extends Fragment {

    private  View view;
    private String result="",userId;
    List<Order> orderList;
    CustomerHistoryAdapter currentOrderAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Button goNow;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_history_item_fragment, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        userId= SharedPrefManager.getInstance(getContext()).getUser().getID();
        relativeLayout = view.findViewById(R.id.textRelate);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        goNow = view.findViewById(R.id.goNow);
        goNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyBokingsActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getHistoryList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        getHistoryList();
        relativeLayout.setVisibility(View.GONE);
    }

    private void getHistoryList() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result   = new CustomerOrderHelper.GetMyCompleteOrderGet().execute(userId).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
            if(!orderList.equals(null)){
                relativeLayout.setVisibility(View.VISIBLE);
            }
            currentOrderAdapter = new CustomerHistoryAdapter(getContext(), orderList);
            recyclerView.setAdapter(currentOrderAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
