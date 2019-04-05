package com.example.awizom.jihuzur.CustomerActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
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
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Service.GPS_Service;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.firebase.ui.auth.ui.email.RegisterEmailFragment.TAG;

public class CustomerpricingActivity extends AppCompatActivity implements View.OnClickListener {

    String mprovider;
    List<PricingView> pricingViewsList;
    CustomerPricingAdapter repairAndServiceAdapter;
    RecyclerView recyclerView;
    private LocationManager locationManager;
    private LocationListener locationListener;
    RelativeLayout relativeLayout;
    private Button nextButton;
    private Button postPricingBtn;
    private Intent intent;
    private String result = "", serviceID = "", description = "", serviceName = "",  displayType = "", btn = "", orderID = "", priceID = "0", data = "", pricingId = "";
    private String empId = "", priceIDs = "", selectedEmpId;
    private String priceIds = "";
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> empID = new ArrayList<>();
    private ArrayList<String> empMobile = new ArrayList<>();
    private ArrayList<String> empName = new ArrayList<>();
    List<EmployeeProfileModel> employeeProfileModelList;
    private EmployeeProfileModel employeeProfileModel;
    private String[] empNameList, empLat, empLong;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    FirebaseFirestore db;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    ViewDialog viewDialog;
    private ProgressDialog progressDialog;
    private static int TIMER = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_service);
        viewDialog=new ViewDialog(this);
        initView();
    }

    private void initView() {
        db = FirebaseFirestore.getInstance();
        if (!runtime_permissions())
        {enable_buttons();}
        serviceID = getIntent().getStringExtra("serviceID");
        description = getIntent().getStringExtra("description");
        serviceName = getIntent().getStringExtra("serviceName");
        displayType = getIntent().getStringExtra("DisplayType");
        btn = getIntent().getStringExtra("button");
        orderID = getIntent().getStringExtra("orderId");
        priceID = getIntent().getStringExtra("priceId");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/


        toolbar.setTitle(serviceName);
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
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog = new ProgressDialog(com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity.this);

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


    private void enable_buttons() {

        Intent i = new Intent(getApplicationContext(), SingleShotLocationProvider.class);
        startService(i);
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
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
        v.startAnimation(buttonClick);
        switch (v.getId()) {
            case R.id.buttonNext:
                showCustomLoadingDialog();
                try {
                    method();
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case R.id.postOrderPriceBtn:
                try {
                showCustomLoadingDialog();
                int j = SharedPrefManager.getInstance(CustomerpricingActivity.this).getPricingID().PricingID;
                try {
                    result = new AdminHelper.EditPricingPost().execute(orderID, String.valueOf(j)).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }catch (Exception e){
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

//        Toast.makeText(CustomerpricingActivity.this,
//                data, Toast.LENGTH_LONG)
//                .show();

        if (data != null) {
            priceID = data;
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setMessage("1. Once the order is placed, minimum onword of Rs. 100 will be Charged. " + "\n"+"2. Once the order is placed, customer could not cancel the order, only our executive can do so accordingly" );
            alertbox.setTitle("Terms & Condition");
            alertbox.setIcon(R.drawable.ic_dashboard_black_24dp);
            alertbox.setNeutralButton("Ok",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;
                        public void onClick(DialogInterface arg0,
                                            int arg1) {


                                    showTheAlertOrderDailogue();


                        }
                    });

            alertbox.show();



//             intent = new Intent(this, LocationActivity.class);
//             intent.putExtra("PricingID", data);
//             startActivity(intent);
        } else {
            Toast toast = Toast.makeText(CustomerpricingActivity.this, "Sorry Our Employee's are Busy on another Order please try after sometime", Toast.LENGTH_LONG);
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

                        progressDialog.setMessage("Order in progress ...");
                        progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                        postOderCreate();
                            }
                        }, TIMER);
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
            Gson gson = new Gson();
            Type getType = new TypeToken<ResultModel>() {
            }.getType();
            ResultModel resultModel = new Gson().fromJson(result, getType);
            if (!result.equals("")) {
                String employeeid=resultModel.getMessage().split(",")[2];
                String orrderid=resultModel.getMessage().split(",")[1].toString();
                Map<String, Object> ordernotification = new HashMap<>();

              /*  ordernotification.put("customerid", false);
                ordernotification.put("customermob", 20.22);
                ordernotification.put("employeemob", false);
                ordernotification.put("servicename", false);*/

                ordernotification.put("orderid", orrderid);
                ordernotification.put("employeeid", employeeid);

                db.collection("OrderNotification").document(employeeid)
                        .set(ordernotification)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //   Log.d(TAG, "DocumentSnapshot successfully written!");
                                Toast.makeText(getApplicationContext(), "Success!",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                intent = new Intent(this, MyBokingsActivity.class);
                generateRandomNumber(orrderid);
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

    int range = 9;  // to generate a single number with this range, by default its 0..9
    int length = 4; // by default length is 4

    public int generateRandomNumber(String orderID) {
        int randomNumber;

        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }


        randomNumber = Integer.parseInt(s);
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("JiHUzzur Otp for Order")
                .setContentText(String.valueOf(randomNumber)).setSmallIcon(R.drawable.jihuzurapplogo)
                .setContentIntent(pendingIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(randomNumber, noti);
        try {

            Map<String, Object> profile = new HashMap<>();
            profile.put("otp", randomNumber);

            db.collection("OrderOtp").document(orderID)
                    .set(profile)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //   Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(getApplicationContext(), "Success!",
                                    Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        Toast.makeText(getApplicationContext(),randomNumber+" number",Toast.LENGTH_LONG).show();

        return randomNumber;
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
         //       latlngs.add(new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())), Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong()))));
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
    public void showCustomLoadingDialog() {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }
}

