package com.example.awizom.jihuzur.LoginRegistrationActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerLoginRegActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerProfileActivity;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SMSTestActicity;
import com.example.awizom.jihuzur.SmsListener;
import com.example.awizom.jihuzur.SmsReceiver;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;


public class VerifyPhoneActivity extends AppCompatActivity implements SMSReceiver.OTPReceiveListener, View.OnClickListener {

    private static int TIMER = 300;
    boolean active = false;
    private EditText otpEditText;
    private Button verifyOtpBtn, resendOTP, countDown;
    private String result, userId = "", otp = "", role = "", image = "", mobile = "", name = "";
    private Intent intent;
    private SMSReceiver smsReceiver;
    private ProgressDialog progressDialog;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_verify);
        initView();

    }

    /*For Initialization */
    private void initView() {

        // android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Verify");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);

        role = getIntent().getExtras().getString("Role");
        mobile = getIntent().getExtras().getString("Mobile");
        otp = getIntent().getExtras().getString("OTP");
        userId = getIntent().getExtras().getString("Uid");

        otpEditText = findViewById(R.id.editTextOtp);
        verifyOtpBtn = findViewById(R.id.buttonVerify);
        verifyOtpBtn.setOnClickListener(this);
        startSMSListener();
        resendOTP = findViewById(R.id.resendOTP);
        countDown = findViewById(R.id.countDown);
        resendOTP.setOnClickListener(this);
        countDown.setOnClickListener(this);
        progressDialog = new ProgressDialog(VerifyPhoneActivity.this);

        otpEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                otpEditText.setHint("");
                return false;
            }
        });


//        SmsReceiver.bindListener(new SmsListener() {
//            @Override
//            public void messageReceived(String messageText) {
//                Log.d("Text",messageText);
//                String sms = messageText;
//                String[] smsSplit = messageText.split(":");
//                otpEditText.setText(smsSplit[1]);
//            }
//        });
        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("" + millisUntilFinished / 1000);
                resendOTP.setVisibility(View.GONE);
            }

            public void onFinish() {
                countDown.setText("00:00");
                resendOTP.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    private void startSMSListener() {
        try {
            smsReceiver = new SMSReceiver();
            smsReceiver.setOTPListener(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION);
            this.registerReceiver(smsReceiver, intentFilter);
            SmsRetrieverClient client = SmsRetriever.getClient(this);
            Task<Void> task = client.startSmsRetriever();
            task.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // API successfully started
                }
            });

            task.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Fail to start API
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*For Event Listeners */
    @Override
    public void onClick(View v) {

        v.startAnimation(buttonClick);
        switch (v.getId()) {
            case R.id.buttonVerify:
                if (otp.equals(otpEditText.getText().toString())) {
                    progressDialog.setMessage("Verify...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (validation()) {
                                DataProfile dataProfile = new DataProfile();
                                dataProfile.ID = userId;
                                dataProfile.Role = role;
                                dataProfile.MobileNo = mobile;
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                                if (SharedPrefManager.getInstance(getApplicationContext()).getUser().Name != null) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "SuccessFully Verified", Toast.LENGTH_LONG).show();
                                    intent = new Intent(VerifyPhoneActivity.this, CustomerHomePage.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "SuccessFully Verified", Toast.LENGTH_LONG).show();
                                    intent = new Intent(VerifyPhoneActivity.this, CustomerProfileActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                }
                            }

                        }
                    }, TIMER);
                } else {
                    Toast.makeText(getApplicationContext(), "Entered OTP is Wrong", Toast.LENGTH_LONG).show();

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
        if (validation()) {
            try {
                result = new LoginHelper.PostVerifyMobile().execute(userId, otp).get();
                progressDialog.dismiss();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                if (!result.equals(null)) {

                        DataProfile dataProfile = new DataProfile();
                        dataProfile.ID = userId;
                        dataProfile.Role = role;
                        dataProfile.MobileNo = mobile;
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);

                            if(dataProfile.Name != null){
                                intent = new Intent(VerifyPhoneActivity.this, CustomerHomePage.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                            }else {
                                intent = new Intent(VerifyPhoneActivity.this, CustomerProfileActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                            }


                }


            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onOTPReceived(String otp) {
        String onetimepwd=otp.split(":")[1];
        otpEditText.setText(onetimepwd);
        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }
}