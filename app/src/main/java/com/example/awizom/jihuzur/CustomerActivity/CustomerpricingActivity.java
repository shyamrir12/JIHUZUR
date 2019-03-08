package com.example.awizom.jihuzur.CustomerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPricingAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CustomerpricingActivity extends AppCompatActivity implements View.OnClickListener {

    List<PricingView> pricingViewsList;
    CustomerPricingAdapter repairAndServiceAdapter;
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    private Button nextButton;
    private Button postPricingBtn;
    private Intent intent;
    private String result = "", serviceID = "", description = "", serviceName = "",
            displayType = "", btn = "", orderID = "", priceID = "0", data = "", pricingId = "";

    private String empId = "",
            priceIDs = "", selectedEmpId;
    private String priceIds = "";
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> empID = new ArrayList<>();
    private ArrayList<String> empMobile = new ArrayList<>();
    private ArrayList<String> empName = new ArrayList<>();

    List<EmployeeProfileModel> employeeProfileModelList;
    private EmployeeProfileModel employeeProfileModel;
    private String[] empNameList, empLat, empLong;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_service);
        initView();
    }

    private void initView() {


        serviceID = getIntent().getStringExtra("serviceID");
        description = getIntent().getStringExtra("description");
        serviceName = getIntent().getStringExtra("serviceName");
        displayType = getIntent().getStringExtra("DisplayType");
        btn = getIntent().getStringExtra("button");
        orderID = getIntent().getStringExtra("orderId");
        priceID = getIntent().getStringExtra("priceId");
        getSupportActionBar().setTitle(serviceName);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);
        postPricingBtn = findViewById(R.id.postOrderPriceBtn);
        postPricingBtn.setOnClickListener(this);

        if (btn.equals("empBtn")) {
            nextButton.setVisibility(View.GONE);
        } else if (btn.equals("serBtn")) {
            nextButton.setVisibility(View.VISIBLE);
        }

        if (btn.equals("empBtn")) {
            postPricingBtn.setVisibility(View.VISIBLE);
        } else if (btn.equals("serBtn")) {
            postPricingBtn.setVisibility(View.GONE);
        }

        getMyOrderRunning();
        employeeProfileGet();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getMyOrderRunning();
                    employeeProfileGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMyOrderRunning() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new CustomerOrderHelper.GetCustomerPricing().execute(serviceID.toString()).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<PricingView>>() {
            }.getType();
            pricingViewsList = new Gson().fromJson(result, listType);
            repairAndServiceAdapter = new CustomerPricingAdapter(CustomerpricingActivity.this,
                    pricingViewsList, displayType, orderID, priceID, btn);
            recyclerView.setAdapter(repairAndServiceAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNext:
                method();
                break;
            case R.id.postOrderPriceBtn:

                int j = SharedPrefManager.getInstance(CustomerpricingActivity.this).getPricingID().PricingID;
                try {
                    result = new AdminHelper.EditPricingPost().execute(orderID, String.valueOf(j)).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void method() {
        data = "";
        List<PricingView> stList = ((CustomerPricingAdapter) repairAndServiceAdapter).getPricinglist();
        for (int p = 0; p < stList.size(); p++) {
            PricingView pricingView = stList.get(p);
            if (pricingView.isSelected() == true) {
                data = data + pricingView.getPricingID() + ",";
                /*
                 * Toast.makeText( CardViewActivity.this, " " +
                 * singleStudent.getName() + " " +
                 * singleStudent.getEmailId() + " " +
                 * singleStudent.isSelected(),
                 * Toast.LENGTH_SHORT).show();
                 */
            }
        }

        Toast.makeText(CustomerpricingActivity.this,
                data, Toast.LENGTH_LONG)
                .show();

        if (data != null) {
            priceID = data;

            showTheAlertOrderDailogue();
//             intent = new Intent(this, LocationActivity.class);
//             intent.putExtra("PricingID", data);
//             startActivity(intent);
        } else {
            int i = SharedPrefManager.getInstance(CustomerpricingActivity.this).getPricingID().PricingID;
            priceIDs = String.valueOf(i);
//             intent = new Intent(this, LocationActivity.class);
//             intent.putExtra("PricingIDS", i);
//             startActivity(intent);
        }
    }

    private void showTheAlertOrderDailogue() {
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage("Do you want to place this order");
        alertbox.setTitle("Order");
        alertbox.setIcon(R.drawable.ic_dashboard_black_24dp);
        alertbox.setNeutralButton("Yes",
                new DialogInterface.OnClickListener() {
                    Class fragmentClass = null;

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        postOderCreate();
                    }
                });
        alertbox.setPositiveButton("No", null);
        alertbox.show();
    }

    private void postOderCreate() {
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());
        String customerid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
        String empId = selectedEmpId;
        String orderDate = String.valueOf(date);
        String catalogId = String.valueOf(serviceID);
        if (priceID.equals(null)) {
            priceIds = priceIDs;
        } else {
            priceIds = priceID;
        }
        try {
            result = new CustomerOrderHelper.OrderPost().execute(customerid, empId, orderDate, catalogId, priceIds).get();
            if (!result.equals("")) {
                intent = new Intent(this, MyBokingsActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(CustomerpricingActivity.this, "Sorry Our Employee's are Busy on another Order please try after sometime", Toast.LENGTH_LONG);
                View view = toast.getView();
                //To change the Background of Toast
                view.setBackgroundColor(Color.BLACK);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                //Shadow of the Of the Text Color
                text.setTextSize(17);

                text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                text.setTextColor(Color.WHITE);
                toast.show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            Gson gson = new Gson();
            UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
            Toast.makeText(this, jsonbody.Message, Toast.LENGTH_SHORT).show();
            if (!result.equals(null)) {
                intent = new Intent(this, MyBokingsActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void employeeProfileGet() {
        try {
            result = new EmployeeOrderHelper.GetEmployeeProfileForShow().execute().get();
            Type listType = new TypeToken<List<EmployeeProfileModel>>() {
            }.getType();
            employeeProfileModelList = new Gson().fromJson(result.toString(), listType);
            empNameList = new String[employeeProfileModelList.size()];
            empLat = new String[employeeProfileModelList.size()];
            empLong = new String[employeeProfileModelList.size()];
            for (int i = 0; i < employeeProfileModelList.size(); i++) {
                empNameList[i] = String.valueOf(employeeProfileModelList.get(i).getName());
                empLat[i] = String.valueOf(employeeProfileModelList.get(i).getLat());
                empLong[i] = String.valueOf(employeeProfileModelList.get(i).getLong());
                latlngs.add(new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())), Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong()))));
                empID.add(employeeProfileModelList.get(i).getID());
                empMobile.add(employeeProfileModelList.get(i).getMobileNo());
                empName.add(employeeProfileModelList.get(i).getName());
                selectedEmpId = employeeProfileModelList.get(i).getID();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

