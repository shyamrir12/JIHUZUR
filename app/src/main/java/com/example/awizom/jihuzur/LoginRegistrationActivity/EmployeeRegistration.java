package com.example.awizom.jihuzur.LoginRegistrationActivity;

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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmployeeRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextMobile;
    private TextView skiplogin;
    private Button butonContinue;
    private String ur = "User", result = "";
    private ProgressDialog progressDialog;
    Intent intent;
    private TextView role;
    private LoginHelper loginHelper;
    boolean connected = false;
    LinearLayout coordinatorLayout;
    Snackbar snackbar;
    FirebaseFirestore db;
    private static int TIMER = 1300;

    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_employee);
        checkAppPermission();
        initView();
    }

    /*For Initialization */
    private void initView() {
        //19/02/2019 comment for not login
        db = FirebaseFirestore.getInstance();
        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinator);
        //progress bar on
        progressDialog = new ProgressDialog(EmployeeRegistration.this);
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

        butonContinue.setOnClickListener(this);

        loginHelper = new LoginHelper();
        role = findViewById(R.id.roleSpiner);
    }

    private void checkAppPermission() {
        ActivityCompat.requestPermissions(EmployeeRegistration.this,
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
                    Toast.makeText(EmployeeRegistration.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /*For Event Listeners*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonContinue:
                progressDialog.setMessage("Login in progress ...");
                progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
                progressDialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        createuser();
                    }
                }, TIMER);
                break;
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

                         /*   Map<String, Object> profile = new HashMap<>();
                            profile.put("busystatus", false);
                            profile.put("lat", 20.22);
                            profile.put("long", 81.66);
                            db.collection("Profile").document(jsonbody.dataProfile.ID)
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
                                    });*/

                            //20/02/2019 ravi





                        } else {
                            Map<String, Object> profile = new HashMap<>();
                            profile.put("busystatus", false);
                            profile.put("lat", 20.22);
                            profile.put("long", 81.66);
                            db.collection("Profile").document(jsonbody.dataProfile.ID)
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

                            intent = new Intent(EmployeeRegistration.this, VerifyPhoneActivity.class);
                            intent.putExtra("OTP", jsonbody.OtpCode);
                            Log.d("OtpEmployee", jsonbody.OtpCode);
                            intent.putExtra("Uid", jsonbody.dataProfile.ID);
                            intent.putExtra("Role", jsonbody.dataProfile.Role);
                            intent.putExtra("Active", jsonbody.dataProfile.Active);
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

        }

    }


}