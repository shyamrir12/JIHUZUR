package com.example.awizom.jihuzur.CustomerActivity.CustomerFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCurrentOrderAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CutomerCurrentOrderFragment extends Fragment {
    private  View view;
    private String result="",userId;
    List<Order> orderList;
    CustomerCurrentOrderAdapter currentOrderAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_current_list, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        userId= SharedPrefManager.getInstance(getContext()).getUser().getID();

        mSwipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        relativeLayout = view.findViewById(R.id.textRelate);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getMyOrderRunning();
                }catch (Exception e){
                    e.printStackTrace();
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        try {
            getMyOrderRunning();
        }catch (Exception e){
            e.printStackTrace();
            relativeLayout.setVisibility(View.VISIBLE);
        }

        relativeLayout.setVisibility(View.GONE);
    }
    private void getMyOrderRunning() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result   = new CustomerOrderHelper.GetMyOrderRunning().execute(userId).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
//            if(orderList.equals(null)){
//                relativeLayout.setVisibility(View.VISIBLE);
//            }
            currentOrderAdapter = new CustomerCurrentOrderAdapter(getContext(), orderList);
            recyclerView.setAdapter(currentOrderAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}