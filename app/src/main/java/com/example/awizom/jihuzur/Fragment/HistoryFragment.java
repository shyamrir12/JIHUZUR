package com.example.awizom.jihuzur.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.Adapter.HistoryAdapter;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class HistoryFragment extends Fragment {


//    private TextView customername, customeraddress, customercontact, interiorname, interiorcontact;
//    private Intent intent;
//    RecyclerView recyclerView;
//    SwipeRefreshLayout mSwipeRefreshLayout;
//    private HistoryAdapter historyAdapter;
//    String orderList[];
    private  View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_adapter, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {

//        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//
//
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getHistoryList();
//            }
//        });
//
//        getHistoryList();
    }

    private void getHistoryList() {
//        String orderList[] = {"Booking Canceled", "Home Cleaning", "DELETE PROJECT"};
//        historyAdapter = new HistoryAdapter(getContext(), orderList);
    }
}
