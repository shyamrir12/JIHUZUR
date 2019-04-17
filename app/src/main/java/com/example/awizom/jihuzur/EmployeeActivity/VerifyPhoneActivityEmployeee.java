package com.example.awizom.jihuzur.EmployeeActivity;

import android.app.Activity;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.LoginRegistrationActivity.SMSReceiver;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SmsListener;
import com.example.awizom.jihuzur.SmsReceiver;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class VerifyPhoneActivityEmployeee extends AppCompatActivity implements SMSReceiver.OTPReceiveListener, View.OnClickListener {

    private EditText otpEditText;
    private Button verifyOtpBtn, resendOTP, countDown;
    private String result, userId = "", otp = "", role = "", image = "", mobile = "", name = "";
    public static final String TAG = VerifyPhoneActivityEmployeee.class.getSimpleName();
    private SMSReceiver smsReceiver;
    boolean active = false;
    private Intent intent;
    private ProgressDialog progressDialog;
    private static int TIMER = 300;
    ViewDialog viewDialog;

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
        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        otpEditText = findViewById(R.id.editTextOtp);
        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(this);
        // This code requires one time to get Hash keys do comment and share key
        Log.d(TAG, "Apps Hash Key: " + appSignatureHashHelper.getAppSignatures().get(0));
        startSMSListener();
        verifyOtpBtn = findViewById(R.id.buttonVerify);
        verifyOtpBtn.setOnClickListener(this);
        resendOTP = findViewById(R.id.resendOTP);
        countDown = findViewById(R.id.countDown);
        resendOTP.setOnClickListener(this);
        countDown.setOnClickListener(this);
        progressDialog = new ProgressDialog(VerifyPhoneActivityEmployeee.this);
        viewDialog = new ViewDialog((Activity) VerifyPhoneActivityEmployeee.this);
        otp = getIntent().getExtras().getString("OTP", "");
        userId = getIntent().getExtras().getString("Uid", "");
        role = getIntent().getExtras().getString("Role", "");
        active = getIntent().getExtras().getBoolean("Active", false);
        mobile = getIntent().getExtras().getString("Mobile");
        name = getIntent().getExtras().getString("Name");

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.d("Text", messageText);
                // Toast.makeText(VerifyPhoneActivity.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
                String sms = messageText;
                String[] smsSplit = messageText.split(":");
                otpEditText.setText(smsSplit[1]);
            }
        });

//        new CountDownTimer(60000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//                countDown.setText("" + millisUntilFinished / 1000);
//                resendOTP.setVisibility(View.GONE);
//            }
//
//            public void onFinish() {
//                countDown.setText("00:00");
//                resendOTP.setVisibility(View.VISIBLE);
//            }
//        }.start();
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


        switch (v.getId()) {
            case R.id.buttonVerify:
                if (otp.equals(otpEditText.getText().toString())) {
                    progressDialog.setMessage("loading in progress ...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if (validation()) {
                                DataProfile dataProfile = new DataProfile();
                                dataProfile.ID = userId;
                                dataProfile.Active = Boolean.valueOf(active);
                                dataProfile.Role = role;
                                dataProfile.Image = image;
                                dataProfile.MobileNo = mobile;
                                dataProfile.Name = name;
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                                if (SharedPrefManager.getInstance(getApplicationContext()).getUser().Name != null) {
                                    showCustomLoadingDialog();
                                    intent = new Intent(VerifyPhoneActivityEmployeee.this, EmployeeHomePage.class);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                                } else {
                                    showCustomLoadingDialog();
                                    intent = new Intent(VerifyPhoneActivityEmployeee.this, EmployeeMyProfileActivity.class);
                                    startActivity(intent);

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

    /*For Post API call with the use of Helper class*/
    private void verifyPostOtp() {
        if (validation()) {

            try {
                result = new LoginHelper.PostVerifyMobile().execute(userId, otp.toString().trim()).get();
                progressDialog.dismiss();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                //     Toast.makeText(getApplicationContext(), jsonbody.Message, Toast.LENGTH_SHORT).show();

                if (!result.equals(null)) {

                    if (jsonbody.isStatus()) {
                        DataProfile dataProfile = new DataProfile();
                        dataProfile.ID = userId;
                        dataProfile.Active = Boolean.valueOf(active);
                        dataProfile.Role = role;
                        dataProfile.Image = image;
                        dataProfile.MobileNo = mobile;
                        dataProfile.Name = name;
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                        role = SharedPrefManager.getInstance(VerifyPhoneActivityEmployeee.this).getUser().Role;
                        if (role.equals("Customer")) {
                            intent = new Intent(VerifyPhoneActivityEmployeee.this, CustomerHomePage.class);
                            startActivity(intent);
                        } else if (role.equals("Admin")) {
                            intent = new Intent(VerifyPhoneActivityEmployeee.this, AdminHomePage.class);
                            startActivity(intent);
                        }

                        if (role.equals("Employee") && active == true) {
                            intent = new Intent(VerifyPhoneActivityEmployeee.this, EmployeeHomePage.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Contact Your Admin", Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    public void onOTPReceived(String otp) {
      //  Toast.makeText(getApplicationContext(), otp + "OTP", Toast.LENGTH_LONG).show();
        String onetimepwd=otp.split(":")[1];
        otpEditText.setText(onetimepwd);

        if (smsReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
        }
    }

    @Override
    public void onOTPTimeOut() {
        Toast.makeText(getApplicationContext(), "OTP Time out", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onOTPReceivedError(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
    }
}