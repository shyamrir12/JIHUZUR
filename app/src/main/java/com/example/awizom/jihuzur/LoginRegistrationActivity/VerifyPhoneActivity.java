package com.example.awizom.jihuzur.LoginRegistrationActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.example.awizom.jihuzur.CustomerActivity.CustomerLoginRegActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerProfileActivity;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;


public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText otpEditText;
    private Button verifyOtpBtn;
    private String result, userId = "", otp = "", role = "", image = "", mobile = "", name = "";

    boolean active = false;
    private Intent intent;
    private ProgressDialog progressDialog;
    private static int TIMER = 300;

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

//        toolbar.setTitle("Verify");
//
//
//        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
//        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
//        toolbar.setTitleTextColor(Color.WHITE);
        role = getIntent().getExtras().getString("Role");
        mobile = getIntent().getExtras().getString("Mobile");
        otpEditText = findViewById(R.id.editTextOtp);
        verifyOtpBtn = findViewById(R.id.buttonVerify);
        verifyOtpBtn.setOnClickListener(this);
        progressDialog = new ProgressDialog(VerifyPhoneActivity.this);

        createuser();
//        otp = getIntent().getExtras().getString("OTP","");
//        userId = getIntent().getExtras().getString("Uid","");
//        active = getIntent().getExtras().getBoolean("Active",false);

//        name = getIntent().getExtras().getString("Name");


//        if(otp != null){
//            otpEditText.setText(otp.toString());
//        }
    }

    /*For Event Listeners */
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.buttonVerify:
                if (otp.equals(otpEditText.getText().toString())) {
                    progressDialog.setMessage("Login in progress ...");
                    progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            verifyPostOtp();

                        }
                    }, TIMER);
                } else {
                    Toast.makeText(getApplicationContext(), "Entered OTP is Wrong", Toast.LENGTH_LONG).show();

                }
                break;
        }


    }

    /*For Validation */
    private boolean validation() {

        if (otpEditText.getText().toString().isEmpty()) {
            otpEditText.setError("Required");
            return false ;
        }
        return true;
    }

    private void createuser() {


        try {

            result = new LoginHelper.GetLogin().execute(mobile, "Jihuzur@123", "Jihuzur@123", role).get();
            progressDialog.dismiss();
            Gson gson = new Gson();
            UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
            try {
                if (jsonbody.isStatus()) {
                    otp = jsonbody.getOtpCode();
                    userId = jsonbody.dataProfile.ID;
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), jsonbody.Message, Toast.LENGTH_SHORT).show();
                    if (jsonbody.OtpCode.equals("mobile already verified")) {
                        DataProfile dataProfile = new DataProfile();
                        dataProfile.ID = jsonbody.dataProfile.ID;
                        dataProfile.Active = jsonbody.dataProfile.Active;
                        dataProfile.Role = jsonbody.dataProfile.Role;
                        dataProfile.Image = jsonbody.dataProfile.Image;
                        dataProfile.Name = jsonbody.dataProfile.Name;
                        dataProfile.MobileNo = jsonbody.dataProfile.MobileNo;

                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                        result = String.valueOf(new AdminHelper.POSTProfileLatLong().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().getID(), String.valueOf("21.22"), String.valueOf("80.66")));
                        if (jsonbody.dataProfile.Role.equals("Customer")) {
                            intent = new Intent(VerifyPhoneActivity.this, CustomerHomePage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                        }
                    }
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
                Toast.makeText(getApplicationContext(), jsonbody.Message, Toast.LENGTH_SHORT).show();
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
                        role = SharedPrefManager.getInstance(VerifyPhoneActivity.this).getUser().Role;
                        if (role.equals("Customer")) {
                            intent = new Intent(VerifyPhoneActivity.this, CustomerProfileActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                        } else if (role.equals("Admin")) {
                            intent = new Intent(VerifyPhoneActivity.this, AdminHomePage.class);
                            startActivity(intent);
                        }

                        if (role.equals("Employee") && active == true) {
                            intent = new Intent(VerifyPhoneActivity.this, EmployeeHomePage.class);
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


}