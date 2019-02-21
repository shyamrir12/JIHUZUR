package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCommentAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.Reply;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Review;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerCommentActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private ImageButton sendBtn,backBtn;
    private AutoCompleteTextView receiverName;
    private EditText messageCommentText,review;
    private Intent intent;
    private String orderID="",cusID="",empID="",orderStartTime="",orderEndtime="",catagoryName="",serviceName=""
            ,pricingterm="",employeeName="",employeeContact="",result="",serviceId="";
    private TextView arrowBack,cancel,empName,empMobile,serviceNAme,txtRatingValue;
    private CustomerCommentAdapter customerCommentAdapter;

    private List<Review> reviews;
    RecyclerView recyclerView;
    private Button commentButtonn,buttonAddCategory,buttonCancel;
    RatingBar ratingBar;
    private Reply reply;
    private List<Reply> replyList;
    private String s="";

    LatLng cusLatLng, empLatLng;
    private MapView mapView;
    private GoogleMap mMap;
    private ArrayList<LatLng> latlngCustomer = new ArrayList<>();
    private ArrayList<LatLng> latlngEmployee = new ArrayList<>();
    private DataProfile dataProfileCustomer;
    private DataProfile dataProfileEmployee;
    private String customerID = "", employeeID = "";
    //SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_comment_list);
        initView();
    }

    private void initView() {

        employeeID = getIntent().getStringExtra("EmployeeID");

//        sendBtn = findViewById(R.id.sendBtn);
//        receiverName = findViewById(R.id.receverName);
//        messageCommentText = findViewById(R.id.messageEditText);
//        backBtn = findViewById(R.id.backArrowBtn);
//        sendBtn.setOnClickListener(this);
//        String messages = messageCommentText.getText().toString().trim();

        arrowBack = findViewById(R.id.backArrow);
        cancel = findViewById(R.id.cancel);
        empName = findViewById(R.id.empName);
        empMobile = findViewById(R.id.contactNumber);
        serviceNAme = findViewById(R.id.serviceName);



        commentButtonn = findViewById(R.id.commentBtn);
        ratingBar=findViewById(R.id.rating);
        review=findViewById(R.id.review) ;
        txtRatingValue = findViewById(R.id.txtRatingValue);
    //    mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        buttonAddCategory = findViewById(R.id.buttonAddCategory);
        buttonCancel = findViewById(R.id.buttonCancel);

        arrowBack.setOnClickListener(this);
        cancel.setOnClickListener(this);
        commentButtonn.setOnClickListener(this);
        buttonAddCategory.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);



        // orderID = getIntent().getStringExtra("OrderID");
        orderID = String.valueOf(getIntent().getIntExtra("OrderID",0));
        cusID = getIntent().getStringExtra("CustomerID");
        empID = getIntent().getStringExtra("EmployeeID");
        orderEndtime = getIntent().getStringExtra("OrderEndTime");
        catagoryName = getIntent().getStringExtra("CategoryName");
        serviceName = getIntent().getStringExtra("ServiceName");
        pricingterm = getIntent().getStringExtra("PricingTerms");
        employeeName = getIntent().getStringExtra("EmployeeName");
        employeeContact = getIntent().getStringExtra("EmployeeContact");
        orderStartTime = getIntent().getStringExtra("OrderStartTime");
        serviceId = String.valueOf(getIntent().getIntExtra("ServiceID",0));

        empName.setText(employeeName.toString());
        empMobile.setText(employeeContact.toString());
        serviceNAme.setText(serviceName.toString());

        txtRatingValue.setText("3.0");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
            }
        });

        getreviewByOrder();

        getCustomerProfileGet();
        getEmployeeProfileGet();

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    getreviewByOrder();
//
//                    getCustomerProfileGet();
//                    getEmployeeProfileGet();
//                }catch (Exception e){
//                    e.printStackTrace();
//
//                }
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backArrow:
                intent = new Intent(CustomerCommentActivity.this, MyBokingsActivity.class);
                startActivity(intent);
                break;
            case R.id.cancel:
                try {
                    result = new CustomerOrderHelper.CancelOrder().execute(orderID).get();
                    Toast.makeText(CustomerCommentActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.commentBtn:
                getreviewByOrder();
                break;
            case R.id.buttonAddCategory:

                String rate = txtRatingValue.getText().toString().split("",3)[1];
                String revie = review.getText().toString().trim();
                try {
                    result = new CustomerOrderHelper.CustomerPostRating().execute(revie,rate,orderID).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(this, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                }

                break;
            case R.id.buttonCancel:
                review.setText("");
                break;
        }
    }

    private void getreviewByOrder() {
        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            result = new CustomerOrderHelper.GetReviewByServiceList().execute(orderID).get();
 //           mSwipeRefreshLayout.setRefreshing(false);
           // Toast.makeText(CustomerCommentActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Review>>() {
            }.getType();
            reviews = new Gson().fromJson(result, listType);
            customerCommentAdapter = new CustomerCommentAdapter(CustomerCommentActivity.this,reviews);
            recyclerView.setAdapter(customerCommentAdapter);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getCustomerProfileGet() {

        String id = SharedPrefManager.getInstance(this).getUser().getID();

        try {

            customerID = SharedPrefManager.getInstance(getApplicationContext()).getUser().ID;
//            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GetProfileForShow().execute(customerID.toString()).get();
 //           mSwipeRefreshLayout.setRefreshing(false);
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                dataProfileCustomer = new Gson().fromJson(result, listType);
                if (dataProfileCustomer != null) {
                    cusLatLng = new LatLng(Double.valueOf(String.valueOf(dataProfileCustomer.Lat)),
                            Double.valueOf(String.valueOf(dataProfileCustomer.Long)));

                    getMapvalue();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeProfileGet() {

        String id = SharedPrefManager.getInstance(this).getUser().getID();

        try {

 //           mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GetProfileForShow().execute(employeeID).get();
 //           mSwipeRefreshLayout.setRefreshing(false);
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                dataProfileEmployee = new Gson().fromJson(result, listType);
                if (dataProfileEmployee != null) {
                    empLatLng = new LatLng(Double.valueOf(String.valueOf(dataProfileEmployee.Lat)),
                            Double.valueOf(String.valueOf(dataProfileEmployee.Long)));

                    getMapvalue();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getMapvalue() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.addAll(latlngCustomer);
//        polylineOptions
//                .width(5)
//                .color(Color.BLUE);

        PolylineOptions polylinesOptions = new PolylineOptions();
        polylinesOptions.add()
                .add(new LatLng(Double.valueOf(String.valueOf(dataProfileCustomer.Lat)),
                        Double.valueOf(String.valueOf(dataProfileCustomer.Long))))
                .add(new LatLng(Double.valueOf(String.valueOf(dataProfileEmployee.Lat)),
                        Double.valueOf(String.valueOf(dataProfileEmployee.Long))));
        polylinesOptions
                .width(4)
                .color(Color.YELLOW);
        mMap.addPolyline(polylinesOptions);


        //This is Customer Location
        mMap.addMarker(new MarkerOptions().position(cusLatLng)
                .title(dataProfileCustomer.Name)
                .snippet(dataProfileCustomer.MobileNo));
        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cusLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));


        //This is Employee Location
        mMap.addMarker(new MarkerOptions().position(empLatLng)
                .title(dataProfileEmployee.Name)
                .snippet(dataProfileEmployee.MobileNo));
        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(empLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(8));

    }


}
