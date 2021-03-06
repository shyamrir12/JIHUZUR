package com.example.awizom.jihuzur.LoginRegistrationActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.MotionEvent;
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
import com.example.awizom.jihuzur.EmployeeActivity.VerifyPhoneActivityEmployeee;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EmployeeRegistration extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextMobile;
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
        editTextMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editTextMobile.setCursorVisible(true);
                editTextMobile.setHint(" ");
                return false;
            }
        });
        butonContinue = findViewById(R.id.buttonContinue);
        butonContinue.setOnClickListener(this);
        loginHelper = new LoginHelper();
        role = findViewById(R.id.roleSpiner);
    }

    private void checkAppPermission() {
        ActivityCompat.requestPermissions(EmployeeRegistration.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS, Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_SMS},
                1);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(EmployeeRegistration.this);
            alertbox.setIcon(R.drawable.exit);
            alertbox.setTitle("Do You Want To Exit ?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
                    finishAffinity();
                    System.exit(0);

                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button
                    // of Dialog
                }
            });
            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
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
            progressDialog.dismiss();
            return false;
        }
        return true;
    }

    /*For Post API call with the use of Helper class*/
    private void createuser() {

        if (validation()) {
            try {
                result = new LoginHelper.GetLogin().execute(editTextMobile.getText().toString().trim(), "Jihuzur@123", "Jihuzur@123","Employee").get();
                progressDialog.dismiss();
                Gson gson = new Gson();
                final UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                try {
                        progressDialog.dismiss();
//                        Toast.makeText(getApplicationContext(), jsonbody.Message, Toast.LENGTH_SHORT).show();
                        if (jsonbody.Otp.equals("mobile already verified")) {
                            DataProfile dataProfile = new DataProfile();
                            dataProfile.ID = jsonbody.Id;
                            dataProfile.ActiveStatus = jsonbody.ActiveStatus;
                            dataProfile.Role = jsonbody.Role;
                            dataProfile.Mobile = jsonbody.Mobile;
                            Log.d("Employee OTP", jsonbody.Otp);
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile);
                            result = String.valueOf(new AdminHelper.POSTProfileLatLong().execute(SharedPrefManager.getInstance(getApplicationContext()).getUser().getID(), String.valueOf("21.22"), String.valueOf("80.66")));
                            //20/02/2019 ravi

                            db.collection("Profile").document(jsonbody.Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().equals(null))
                                    {
                                        Map<String, Object> profile = new HashMap<>();
                                        profile.put("busystatus", false);
                                        profile.put("lat", 21.22);
                                        profile.put("long", 81.66);
                                        db.collection("Profile").document(jsonbody.Id)
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
                                    }
                                }
                            });

                        } else {
                            db.collection("Profile").document(jsonbody.Id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.getResult().equals(null))
                                    {
                                        Map<String, Object> profile = new HashMap<>();
                                        profile.put("busystatus", false);
                                        profile.put("lat", 21.22);
                                        profile.put("long", 81.66);
                                        db.collection("Profile").document(jsonbody.Id)
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
                                    }
                                }
                            });
                            if (jsonbody.Role.equals("Employee")) {

                                intent = new Intent(EmployeeRegistration.this, VerifyPhoneActivityEmployeee.class);
                                intent.putExtra("OTP", jsonbody.Otp);
                                Log.d("OtpEmployee", jsonbody.Otp);
                                intent.putExtra("Uid", jsonbody.Id);
                                intent.putExtra("Role", jsonbody.Role);
                                intent.putExtra("Active", jsonbody.ActiveStatus);
                                startActivity(intent);
                                Log.d("EmployeeOTp", jsonbody.Otp);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                            }else {
                                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                                alertDialog.setTitle("Sorry !!");
                                alertDialog.setMessage("Your mobile no is register already"+" "+jsonbody.Role);


                                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);

                                alertDialog.setIcon(R.drawable.warning);

                                alertDialog.setPositiveButton("ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                                dialog.dismiss();
                                            }
                                        });


                                alertDialog.show();
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