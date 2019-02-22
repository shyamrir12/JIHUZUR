package com.example.awizom.jihuzur.LoginRegistrationActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.ProfileFirebase;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.settings.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMobile;
    private TextView skiplogin;
    private Button butonContinue;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    private String mobileNumber = "", mobile = "", ur = "User", result = "";
    DataProfile customerDataProfile;
    private ProgressDialog progressDialog;
    Intent intent;
    private Spinner role;
    private LoginHelper loginHelper;
    boolean connected = false;
    LinearLayout coordinatorLayout;
    Snackbar snackbar;
     FirebaseFirestore db;
    boolean check=false;
    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
    }

    /*For Initialization */
    private void initView() {

        //19/02/2019 comment for not login
        db=FirebaseFirestore.getInstance();


        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinator);
        snackbar = Snackbar
                .make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
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
        String userrole[] = {"Admin", "Customer", "Employee"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userrole);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        role.setAdapter(spinnerArrayAdapter);
        progressDialog = new ProgressDialog(this);
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
    /*For Event Listeners*/
    @Override
    public void onClick(View v) {

        if (v.getId() == butonContinue.getId()) {
            createuser();
        }
        if (v.getId() == skiplogin.getId()) {
            intent = new Intent(RegistrationActivity.this, CustomerHomePage.class);
            startActivity(intent);
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
                ur = role.getSelectedItem().toString().trim();
            try {
                result = new LoginHelper.GetLogin().execute(editTextMobile.getText().toString().trim(), "Jihuzur@123", "Jihuzur@123", ur).get();
                Gson gson = new Gson();

                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                try {
                    if (jsonbody.isStatus()) {
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

//20/02/2019 Comment for not login on employee profile


                            Map<String, Object> profile = new HashMap<>();

                            profile.put("busystatus", 0);
                            profile.put("lat", 20.22);
                            profile.put("long", 80.66);

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


                         if(jsonbody.dataProfile.Role.equals( "Employee" )&& !jsonbody.dataProfile.ID.isEmpty() )
                        { CollectionReference dbprofile=db.collection( "Profile" );
                             dbprofile.whereEqualTo( "id",jsonbody.dataProfile.ID ).get().addOnSuccessListener( new OnSuccessListener<QuerySnapshot>() {
                                 @Override
                                 public void onSuccess(QuerySnapshot documentSnapshots) {
                                     if(documentSnapshots.isEmpty()){
                                         check=true;
                                     }

                                 }
                             } );

                             if(check==true){
                                 ProfileFirebase pf=new ProfileFirebase(jsonbody.dataProfile.ID,0,0,false);
                                 dbprofile.add( pf ).addOnSuccessListener( new OnSuccessListener<DocumentReference>() {
                                     @Override
                                     public void onSuccess(DocumentReference documentReference) {

                                     }
                                 } ).addOnFailureListener( new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {

                                     }
                                 } );
                             }
                         }

                            if (jsonbody.dataProfile.Role.equals("Employee")) {
                                intent = new Intent(RegistrationActivity.this, EmployeeHomePage.class);
                                startActivity(intent);

                            } else if (jsonbody.dataProfile.Role.equals("Customer")) {
                                intent = new Intent(RegistrationActivity.this, CustomerHomePage.class);
                                startActivity(intent);

                            } else if (jsonbody.dataProfile.Role.equals("Admin")) {
                                intent = new Intent(RegistrationActivity.this, AdminHomePage.class);
                                startActivity(intent);

                            }
                        } else {
                            intent = new Intent(RegistrationActivity.this, VerifyPhoneActivity.class);
                            intent.putExtra("OTP", jsonbody.OtpCode);
                            intent.putExtra("Uid", jsonbody.dataProfile.ID);
                            intent.putExtra("Role", jsonbody.dataProfile.Role);
                            intent.putExtra("Active", jsonbody.dataProfile.Active);
                            startActivity(intent);
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