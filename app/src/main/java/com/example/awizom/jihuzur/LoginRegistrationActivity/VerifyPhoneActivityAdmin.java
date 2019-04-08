package com.example.awizom.jihuzur.LoginRegistrationActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerProfileActivity;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.AdminProfile;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SmsListener;
import com.example.awizom.jihuzur.SmsReceiver;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;
import java.util.concurrent.ExecutionException;


public class VerifyPhoneActivityAdmin extends AppCompatActivity implements View.OnClickListener {

    private EditText otpEditText;
    private Button verifyOtpBtn, resendOTP, countDown;
    private String result,userId="",otp="",role="",image="",mobile="",name="";

    boolean active=false;
    private Intent intent;
    private ProgressDialog progressDialog;
    private static int TIMER = 300;

    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        initView();

    }
    /*For Initialization */
    private void initView() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/

        toolbar.setTitle("Verify");


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);

        otpEditText = findViewById(R.id.editTextOtp);
        verifyOtpBtn = findViewById(R.id.buttonVerify);
        verifyOtpBtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(VerifyPhoneActivityAdmin.this);
        otp = getIntent().getExtras().getString("OTP","");
        userId = getIntent().getExtras().getString("Uid","");
        role = getIntent().getExtras().getString("Role","");
        active = getIntent().getExtras().getBoolean("Active",false);
        mobile = getIntent().getExtras().getString("Mobile");
        name = getIntent().getExtras().getString("Name");

        resendOTP = findViewById(R.id.resendOTP);
        countDown = findViewById(R.id.countDown);
        resendOTP.setOnClickListener(this);
        countDown.setOnClickListener(this);


        new CountDownTimer(240000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("" + millisUntilFinished / 1000);
                resendOTP.setVisibility(View.GONE);
            }

            public void onFinish() {
                countDown.setText("00:00");
                resendOTP.setVisibility(View.VISIBLE);
            }
        }.start();


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text",messageText);
                String sms = messageText;
                String[] smsSplit = messageText.split(":");
                otpEditText.setText(smsSplit[1]);
            }
        });
    }

    /*For Event Listeners */
    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.buttonVerify:
                if(otp.equals(otpEditText.getText().toString()))
                {
                    progressDialog.setMessage("Login in progress ...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(getApplicationContext(), "SuccessFully Verified", Toast.LENGTH_LONG).show();
                            if (validation()) {
                                DataProfile dataProfile = new DataProfile();
                                dataProfile.ID = userId;
                                dataProfile.Role = role;
                                dataProfile.MobileNo = mobile;
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                                if (dataProfile.Name != null) {
                                    intent = new Intent(VerifyPhoneActivityAdmin.this, AdminHomePage.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                } else {
                                    intent = new Intent(VerifyPhoneActivityAdmin.this,AdminHomePage.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                }
                            }


                        }}, TIMER);
                }else{
                    Toast.makeText(getApplicationContext(),"Entered OTP is Wrong",Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.resendOTP:
                progressDialog.setMessage("Resend Otp...");
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createuser();
                    }

                }, TIMER);
                break;
            case R.id.countDown:
                break;
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
    private void createuser() {
        try {
            result = new LoginHelper.GetLogin().execute(mobile, "Jihuzur@123", "Jihuzur@123", "Customer").get();
            progressDialog.dismiss();
            Gson gson = new Gson();
            UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
            try {
                if (jsonbody.isStatus()) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "OTP Send", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    /*For Post API call with the use of Helper class*/
    private void verifyPostOtp() {
        if(validation()){

            try {
                result   = new LoginHelper.PostVerifyMobile().execute(userId,otp.toString().trim()).get();
                progressDialog.dismiss();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                if(!result.equals(null)){

                    if(jsonbody.isStatus()) {
                        DataProfile dataProfile = new DataProfile();
                        dataProfile.ID = userId;
                        dataProfile.Active = Boolean.valueOf( active );
                        dataProfile.Role = role;
                        dataProfile.Image = image;
                        dataProfile.MobileNo =mobile;
                        dataProfile.Name = name;
                        SharedPrefManager.getInstance( getApplicationContext() ).userLogin( dataProfile );
                        role = SharedPrefManager.getInstance( VerifyPhoneActivityAdmin.this ).getUser().Role;
                        if (role.equals( "Admin" )) {
                            intent = new Intent( VerifyPhoneActivityAdmin.this, AdminHomePage.class );
                            startActivity( intent );
                        }


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