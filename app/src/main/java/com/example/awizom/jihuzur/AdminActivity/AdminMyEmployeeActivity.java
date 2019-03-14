package com.example.awizom.jihuzur.AdminActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.awizom.jihuzur.Adapter.MyEmployeeListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.MyEmployeeListModel;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AdminMyEmployeeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String result = "";
    List<MyEmployeeListModel> myEmployeeListModels;
    SwipeRefreshLayout mSwipeRefreshLayout;
    MyEmployeeListAdapter myEmployeeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_myemployee);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("My Employee List");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getMyEmployeeList();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getMyEmployeeList();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public void getMyEmployeeList() {

        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GetMyEmployeeList().execute().get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MyEmployeeListModel>>() {
            }.getType();

            myEmployeeListModels = new Gson().fromJson(result, listType);
            myEmployeeListAdapter = new MyEmployeeListAdapter(AdminMyEmployeeActivity.this, myEmployeeListModels);
            mSwipeRefreshLayout.setRefreshing(false);
            recyclerView.setAdapter(myEmployeeListAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
