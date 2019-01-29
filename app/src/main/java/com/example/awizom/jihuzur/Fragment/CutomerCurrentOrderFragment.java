package com.example.awizom.jihuzur.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.awizom.jihuzur.Adapter.CustomerCurrentOrderAdapter;
import com.example.awizom.jihuzur.Helper.CustomerGetMyOrderRunningHelper;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customer_current_list, container, false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        userId= SharedPrefManager.getInstance(getContext()).getUser().getID();

        relativeLayout = view.findViewById(R.id.textRelate);
        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getMyOrderRunning();
    }
    private void getMyOrderRunning() {
        try {
            result   = new CustomerGetMyOrderRunningHelper.GetMyOrderRunning().execute(userId).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Order>>() {
            }.getType();
            orderList = new Gson().fromJson(result, listType);
            if(orderList.equals(null)){
                relativeLayout.setVisibility(View.VISIBLE);
            }
            currentOrderAdapter = new CustomerCurrentOrderAdapter(getContext(), orderList);
            recyclerView.setAdapter(currentOrderAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}