package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.awizom.jihuzur.Helper.VerifyMobileHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import java.util.concurrent.ExecutionException;


public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText otpEditText;
    private Button verifyOtpBtn;
    private String result,userId="",otp="",role="";
    boolean active=false;
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
        active = getIntent().getExtras().getBoolean("Active",false);

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
                result   = new VerifyMobileHelper.PostVerifyMobile().execute(otpEditText.getText().toString().trim(),userId,otp.toString().trim()).get();
                Gson gson = new Gson();
                UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
                Toast.makeText(getApplicationContext(),jsonbody.Message,Toast.LENGTH_SHORT).show();

                if(!result.equals(null)){

                   if(jsonbody.isStatus()) {


                       DataProfile dataProfile = new DataProfile();
                       dataProfile.ID = userId;
                       dataProfile.Active = Boolean.valueOf( active );
                       dataProfile.Role = role;
                       SharedPrefManager.getInstance( getApplicationContext() ).userLogin( dataProfile );

                       role = SharedPrefManager.getInstance( VerifyPhoneActivity.this ).getUser().Role;
                        if (role.equals( "Customer" )) {
                           intent = new Intent( VerifyPhoneActivity.this, CustomerHomePage.class );
                           startActivity( intent );
                       } else if (role.equals( "Admin" )) {
                           intent = new Intent( VerifyPhoneActivity.this, AdminHomePage.class );
                           startActivity( intent );
                       }

                       if (role.equals( "Employee" )&& active==true ) {
                           intent = new Intent( VerifyPhoneActivity.this, EmployeeHomePage.class );
                           startActivity( intent );
                       } else
                       {
                           Toast.makeText(getApplicationContext(),"Please Contact Your Admin",Toast.LENGTH_SHORT).show();
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