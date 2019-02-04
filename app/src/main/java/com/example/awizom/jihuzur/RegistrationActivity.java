package com.example.awizom.jihuzur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

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

    /*For layout binding */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_sign_up );
        initView();
    }

    /*For Initialization */
    private void initView() {

        editTextMobile = findViewById( R.id.editTextMobile );
        butonContinue = findViewById( R.id.buttonContinue );
        skiplogin = findViewById( R.id.skiplogin );
        butonContinue.setOnClickListener( this );
        skiplogin.setOnClickListener( this );
        loginHelper = new LoginHelper();


        role = findViewById( R.id.roleSpiner );


        String userrole[] = {"Admin", "Customer", "Employee"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, userrole );
        spinnerArrayAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item ); // The drop down view
        role.setAdapter( spinnerArrayAdapter );

        progressDialog = new ProgressDialog( this );

    }

    /*For Event Listeners*/
    @Override
    public void onClick(View v) {

        if (v.getId() == butonContinue.getId()) {
            createuser();
        }
        if (v.getId() == skiplogin.getId()) {
            intent = new Intent( RegistrationActivity.this, CustomerHomePage.class );
            startActivity( intent );
        }

    }

    /*For Validation */
    private boolean validation() {

        if (editTextMobile.getText().toString().isEmpty() || editTextMobile.getText().toString().length() < 10) {

            editTextMobile.setError( "Enter a valid mobile" );
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
                result = new LoginHelper.GetLogin().execute( editTextMobile.getText().toString().trim(), "Jihuzur@123", "Jihuzur@123", ur ).get();
                Gson gson = new Gson();

                UserLogin.RootObject jsonbody = gson.fromJson( result, UserLogin.RootObject.class );
                try {
                    if (jsonbody.isStatus()) {
                        Toast.makeText( getApplicationContext(), jsonbody.Message, Toast.LENGTH_SHORT ).show();

                        if (jsonbody.OtpCode.equals( "mobile already verified" )) {

                            DataProfile dataProfile = new DataProfile();
                            dataProfile.ID = jsonbody.dataProfile.ID;
                            dataProfile.Active = jsonbody.dataProfile.Active;
                            dataProfile.Role = jsonbody.dataProfile.Role;
                            SharedPrefManager.getInstance( getApplicationContext() ).userLogin( dataProfile );

                            if (jsonbody.dataProfile.Role.equals( "Employee" )) {
                                intent = new Intent( RegistrationActivity.this, EmployeeHomePage.class );
                                startActivity( intent );
                            } else if (jsonbody.dataProfile.Role.equals( "Customer" )) {
                                intent = new Intent( RegistrationActivity.this, CustomerHomePage.class );
                                startActivity( intent );
                            } else if (jsonbody.dataProfile.Role.equals( "Admin" )) {
                                intent = new Intent( RegistrationActivity.this, AdminHomePage.class );
                                startActivity( intent );
                            }
                        } else {
                            intent = new Intent( RegistrationActivity.this, VerifyPhoneActivity.class );
                            intent.putExtra( "OTP", jsonbody.OtpCode );
                            intent.putExtra( "Uid", jsonbody.dataProfile.ID );
                            intent.putExtra( "Role", jsonbody.dataProfile.Role );
                            intent.putExtra( "Active", jsonbody.dataProfile.Active );
                            startActivity( intent );
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
            Toast.makeText( getApplicationContext(), "Mobile No is not Valid", Toast.LENGTH_SHORT ).show();

        }

    }


}