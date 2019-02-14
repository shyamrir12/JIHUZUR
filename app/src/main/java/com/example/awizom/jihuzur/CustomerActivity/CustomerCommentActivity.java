package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCommentAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCurrentOrderAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerReplyAdapter;
import com.example.awizom.jihuzur.Fragment.MyBookingFragment;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.CustomerRatingHelper;
import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Reply;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Review;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerCommentActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fak);
        initView();
    }

    private void initView() {

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
                    result = new CustomerRatingHelper.PostRating().execute(revie,rate,orderID).get();
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
            result = new CustomerOrderHelper.GetReviewByServiceList().execute(orderID).get();
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


}
