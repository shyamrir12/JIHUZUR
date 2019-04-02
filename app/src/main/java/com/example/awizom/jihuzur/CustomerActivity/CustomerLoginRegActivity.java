package com.example.awizom.jihuzur.CustomerActivity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.LoginRegistrationActivity.VerifyPhoneActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

public class CustomerLoginRegActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextMobile;
    private TextView skiplogin;
    private Button butonContinue;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    private String mobileNumber = "", mobile = "", ur = "User", result = "",userId="",userRole="";
    DataProfile customerDataProfile;
    private ProgressDialog progressDialog;
    Intent intent;
    private TextView role;
    private LoginHelper loginHelper;
    boolean connected = false;
    LinearLayout coordinatorLayout;
    Snackbar snackbar;
    FirebaseFirestore db;
    boolean check = false;
    private static int TIMER = 300;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_login_layout);
        checkAppPermission();
        initView();
    }

    /*For Initialization */
    private void initView() {
        //19/02/2019 comment for not login
        db = FirebaseFirestore.getInstance();
        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinator);

        //progress bar on
        progressDialog = new ProgressDialog(com.example.awizom.jihuzur.CustomerActivity.CustomerLoginRegActivity.this);


        snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        initView();
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        checkInternet();
        editTextMobile = findViewById(R.id.editTextMobile);
        butonContinue = findViewById(R.id.buttonContinue);
        skiplogin = findViewById(R.id.skiplogin);
        butonContinue.setOnClickListener(this);
        skiplogin.setOnClickListener(this);
        loginHelper = new LoginHelper();
        role = findViewById(R.id.roleSpiner);

        userId=  editTextMobile.getText().toString();
        userRole = role.getText().toString().trim();


    }

    private void checkAppPermission() {

        ActivityCompat.requestPermissions(com.example.awizom.jihuzur.CustomerActivity.CustomerLoginRegActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS},
                1);
    }

    private void checkInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            //    Toast.makeText(getApplicationContext(), "Internet is On", Toast.LENGTH_SHORT).show();
        } else {
            connected = false;
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(CustomerLoginRegActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }

    }

    /*For Event Listeners*/
    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClick);
        switch (v.getId()) {
            case R.id.buttonContinue:
                progressDialog.setMessage("Login in progress ...");
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                          reDirect();
                        }
                    }, TIMER);

                break;
            case R.id.skiplogin:
                intent = new Intent(CustomerLoginRegActivity.this, CustomerHomePage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                break;
        }


    }

    private void reDirect() {
        if(validation()) {

            intent = new Intent(CustomerLoginRegActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("Role", userRole);
            intent.putExtra("Mobile", editTextMobile.getText().toString());
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
        }else {
            Toast.makeText(getApplicationContext(), "Mobile No is not Valid", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }

    /*For Validation */
    private boolean validation() {

        if (editTextMobile.getText().toString().isEmpty() || editTextMobile.getText().toString().length() < 10) {
            editTextMobile.setError("Enter a valid mobile");
            editTextMobile.requestFocus();
            return false;
        }
        return true;
    }

    /*For Post API call with the use of Helper class*/
    private void createuser() {

        if (validation()) {

            if (role != null)
                ur = role.getText().toString().trim();
            try {

                result = new LoginHelper.GetLogin().execute(editTextMobile.getText().toString().trim(), "Jihuzur@123", "Jihuzur@123", ur).get();
                progressDialog.dismiss();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                try {
                    if (jsonbody.isStatus()) {
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
                                intent = new Intent(CustomerLoginRegActivity.this, CustomerHomePage.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                            }
                        } else {
                            intent = new Intent(CustomerLoginRegActivity.this, VerifyPhoneActivity.class);
                            intent.putExtra("OTP", jsonbody.OtpCode);
                            intent.putExtra("Uid", jsonbody.dataProfile.ID);
                            intent.putExtra("Role", jsonbody.dataProfile.Role);
                            intent.putExtra("Active", jsonbody.dataProfile.Active);
                            intent.putExtra("Mobile",jsonbody.dataProfile.MobileNo);
                            intent.putExtra("Name",jsonbody.dataProfile.Name);
                            Log.d("CustomerOTp",jsonbody.OtpCode);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
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
        } else {
            Toast.makeText(getApplicationContext(), "Mobile No is not Valid", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }

    }


}