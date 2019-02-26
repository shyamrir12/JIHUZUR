package com.example.awizom.jihuzur.EmployeeActivity.EmployeeFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter.EmployeeHistoryAdapter;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeHistoryCurrentFragment extends Fragment {

    List<Order> orderList;
    EmployeeHistoryAdapter employeeHistoryAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    private View view;
    private String result = "", userId;
    String empid;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_history_item_fragment, container, false);
        try {
            empid = getArguments().getString("EmployeeID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView(view);
        return view;
    }

    private void initView(View view) {
        userId = SharedPrefManager.getInstance(getContext()).getUser().getID();
        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        relativeLayout = view.findViewById(R.id.textRelate);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getHistoryList();
                } catch (Exception e) {
                    e.printStackTrace();
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            getHistoryList();
        } catch (Exception e) {
            e.printStackTrace();
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getHistoryList() {
        if (SharedPrefManager.getInstance(getContext()).getUser().getRole().equals("Admin")) {
            userId = empid.toString();
        }

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new EmployeeOrderHelper.GetMyCompleteOrderGet().execute(userId).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
            if (orderList.equals(null)) {
                relativeLayout.setVisibility(View.VISIBLE);
            }
            employeeHistoryAdapter = new EmployeeHistoryAdapter(getContext(), orderList);
            recyclerView.setAdapter(employeeHistoryAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
