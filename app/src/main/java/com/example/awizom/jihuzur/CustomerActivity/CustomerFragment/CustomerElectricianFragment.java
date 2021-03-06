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
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCatagoryAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class CustomerElectricianFragment extends Fragment {

    Intent intent;
    private String result="",catalogName="Home Cleaning & Repairs";
    List<Catalog> categorylist;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    CustomerCatagoryAdapter customerCatagoryAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_apliance, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

        relativeLayout = view.findViewById(R.id.textRelate);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getcatagoryList();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        getcatagoryList();
    }

    private void getcatagoryList() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
           result = new CustomerOrderHelper.GETCustomerCategoryList().execute(catalogName).get();
            mSwipeRefreshLayout.setRefreshing(false);
           if(result != null){

               Gson gson = new Gson();
               Type listType = new TypeToken<List<Catalog>>() {
               }.getType();
               categorylist = new Gson().fromJson(result, listType);
               customerCatagoryAdapter = new CustomerCatagoryAdapter(getContext(),categorylist);
               recyclerView.setAdapter(customerCatagoryAdapter);
           }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}