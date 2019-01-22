package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.awizom.jihuzur.Helper.VerifyMobileHelper;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.google.gson.Gson;
import java.util.concurrent.ExecutionException;


public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText otpEditText;
    private Button verifyOtpBtn;
    private String result,userId="",otp="",role="";
    private Intent intent;

    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        initView();

    }
    /*For Initialization */
    private void initView() {
        otpEditText = findViewById(R.id.editTextOtp);
        verifyOtpBtn = findViewById(R.id.buttonVerify);
        verifyOtpBtn.setOnClickListener(this);


        otp = getIntent().getExtras().getString("OTP","");
        userId = getIntent().getExtras().getString("Uid","");
        role = getIntent().getExtras().getString("Role","");

        if(otp.contains("mobile already verified")){
            Toast.makeText(getApplicationContext(),"mobile already verified",Toast.LENGTH_SHORT).show();
            if(role.equals("Employee")){
                intent = new Intent(VerifyPhoneActivity.this, EmployeeHomePage.class);
                startActivity(intent);
            }else if(role.equals("Customer")){
                intent = new Intent(VerifyPhoneActivity.this, CustomerHomePage.class);
                startActivity(intent);
            }else if(role.equals("Admin")){
                intent = new Intent(VerifyPhoneActivity.this, AdminHomePage.class);
                startActivity(intent);
            }

        }else {
            validation();
        }

    }

    /*For Event Listeners */
    @Override
    public void onClick(View v) {

        if(v.getId() == verifyOtpBtn.getId()){
                verifyPostOtp();
        }
    }

    /*For Validation */
    private boolean validation() {

        if (otpEditText.getText().toString().isEmpty()) {

            otpEditText.setError("Required");
            return false;
        }
        return true;
    }

    /*For Post API call with the use of Helper class*/
    private void verifyPostOtp() {
        if(validation()){

            try {
                result   = new VerifyMobileHelper.PostVerifyMobile().execute(otpEditText.getText().toString().trim(),userId.toString().trim(),otp.toString().trim()).get();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                Toast.makeText(getApplicationContext(),jsonbody.Message,Toast.LENGTH_SHORT).show();

                if(!result.equals(null)){

                    if(role.equals("Employee")){
                        intent = new Intent(VerifyPhoneActivity.this, EmployeeHomePage.class);
                        startActivity(intent);
                    }else if(role.equals("Customer")){
                        intent = new Intent(VerifyPhoneActivity.this, CustomerHomePage.class);
                        startActivity(intent);
                    }else if(role.equals("Admin")){
                        intent = new Intent(VerifyPhoneActivity.this, AdminHomePage.class);
                        startActivity(intent);
                    }
                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(getApplicationContext(),"Invalid",Toast.LENGTH_SHORT).show();

        }


    }

}