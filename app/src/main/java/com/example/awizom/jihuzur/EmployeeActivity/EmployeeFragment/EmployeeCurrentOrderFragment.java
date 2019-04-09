package com.example.awizom.jihuzur.EmployeeActivity.EmployeeFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter.EmployeeCurrentOrderAdapter;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeCurrentOrderFragment extends Fragment implements View.OnClickListener {

    List<Order> orderList;
    EmployeeCurrentOrderAdapter employeeCurrentOrderAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    private View view;
    private String result = "", empId;
    String empid;
    private ImageView reloadBtn;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.employee_current_list, container, false);
        try {
            empid = getArguments().getString("EmployeeID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView(view);
        return view;
    }

    private void initView(View view) {

        empId = SharedPrefManager.getInstance(getContext()).getUser().getID();

        relativeLayout = view.findViewById(R.id.textRelate);
       mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.recyclerView);
        // reloadBtn = view.findViewById(R.id.reload);
        // reloadBtn.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.scrollToPosition(0);
        recyclerView.smoothScrollToPosition(0);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getMyOrderRunning();
                } catch (Exception e) {
                    e.printStackTrace();
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            getMyOrderRunning();
        } catch (Exception e) {
            e.printStackTrace();
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getMyOrderRunning() {

        if (SharedPrefManager.getInstance(getContext()).getUser().getRole().equals("Admin")) {
            empId = empid.toString();
        }

        try {
           mSwipeRefreshLayout.setRefreshing(true);
            result = new EmployeeOrderHelper.EmployeeGetMyCurrentOrder().execute(empId).get();
            mSwipeRefreshLayout.setRefreshing(false);
            if (result.isEmpty()) {
                relativeLayout.setVisibility(View.VISIBLE);
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Order>>() {
                }.getType();
                orderList = new Gson().fromJson(result, listType);
                employeeCurrentOrderAdapter = new EmployeeCurrentOrderAdapter(getContext(), orderList);


                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(employeeCurrentOrderAdapter);
                        recyclerView.smoothScrollToPosition(0);
                        recyclerView.scrollToPosition(0);
                    }
                },100);

            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
}