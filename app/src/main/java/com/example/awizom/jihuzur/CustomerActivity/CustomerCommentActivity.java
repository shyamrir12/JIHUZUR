package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.R;

public class CustomerCommentActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton sendBtn,backBtn;
    private AutoCompleteTextView receiverName;
    private EditText messageCommentText;
    private Intent intent;
    private String orderID="",cusID="",empID="",orderStartTime="",orderEndtime="",catagoryName="",serviceName=""
            ,pricingterm="",employeeName="",employeeContact="";
    private TextView arrowBack,cancel,empName,empMobile,serviceNAme;
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

        arrowBack.setOnClickListener(this);
        cancel.setOnClickListener(this);

        orderID = getIntent().getStringExtra("OrderID");
        cusID = getIntent().getStringExtra("CustomerID");
        empID = getIntent().getStringExtra("EmployeeID");
        orderEndtime = getIntent().getStringExtra("OrderEndTime");
        catagoryName = getIntent().getStringExtra("CategoryName");
        serviceName = getIntent().getStringExtra("ServiceName");
        pricingterm = getIntent().getStringExtra("PricingTerms");
        employeeName = getIntent().getStringExtra("EmployeeName");
        employeeContact = getIntent().getStringExtra("EmployeeContact");

        empName.setText(employeeName.toString());
        empMobile.setText(employeeContact.toString());
        serviceNAme.setText(serviceName.toString());

//        if (messages.isEmpty()) {
//            messageCommentText.requestFocus();
//            return;
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backArrow:
                intent = new Intent(CustomerCommentActivity.this, MenuActivity.class);
                startActivity(intent);
                break;
            case R.id.cancel:
               intent = new Intent(CustomerCommentActivity.this, MenuActivity.class);
               startActivity(intent);
                break;
        }
    }
}
