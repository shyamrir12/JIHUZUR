package com.example.awizom.jihuzur.CustomerActivity;

import android.os.Bundle;
import android.app.Activity;
import android.widget.GridView;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCatagoryAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerHomePageAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NewCustomerHome extends Activity {

    GridView gridView;
    String result="";
    List<Catalog> categorylist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_home);
        gridView=(GridView)findViewById(R.id.gridview);
       //getCategoryList();

    }

//    private void getCategoryList() {
//        String catalogname="Home Cleaning & Repairs";
//        try {
//            result = new CustomerOrderHelper.GETCustomerCategoryList().execute(catalogname).get();
//            if (result != null) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Catalog>>() {
//                }.getType();
//                categorylist = new Gson().fromJson(result, listType);
//               CustomerHomePageAdapter customerCatagoryAdapter = new CustomerHomePageAdapter(NewCustomerHome.this, categorylist);
//                gridView.setAdapter(customerCatagoryAdapter);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
