package com.example.awizom.jihuzur;

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

import com.example.awizom.jihuzur.Model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextMobile;
    private TextView empsign, skiplogin;
    private Button butonContinue;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    private String mobileNumber = "", mobile = "",ur = "User";
    Profile customerProfile;

    Intent intent;
    private Spinner role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        initView();

    }

    private void initView() {

        editTextMobile = findViewById(R.id.editTextMobile);
        empsign=findViewById(R.id.empsignin);
        butonContinue=findViewById(R.id.buttonContinue);
        skiplogin=findViewById(R.id.skiplogin);
        butonContinue.setOnClickListener(this);
        skiplogin.setOnClickListener(this);
        empsign.setOnClickListener(this);

       role = findViewById(R.id.roleSpiner);
        String userrole[] = {"Admin", "Customer", "Employee"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userrole);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        role.setAdapter(spinnerArrayAdapter);

        try {
            mAuth = FirebaseAuth.getInstance();
            mobileNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().toString();

            if (editTextMobile.getText().toString().isEmpty() || editTextMobile.getText().toString().length() < 10) {

                editTextMobile.setError("Enter a valid mobile");
                editTextMobile.requestFocus();

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonContinue:
                createuser();
                break;


            case R.id.empsignin:
                createProfile();
                break;

            case R.id.skiplogin:
                Intent skip = new Intent(RegistrationActivity.this, CustomerHomePage.class);
                startActivity(skip);
                break;
        }
    }



    private void createuser() {


        if ( editTextMobile.getText().toString() != null || role != null)
            ur = role.getSelectedItem().toString().trim();
        try {

        } catch (Exception e) {

        }
    }

    private boolean createProfile() {

        Date c = Calendar.getInstance().getTime();
        datauserprofile = FirebaseDatabase.getInstance().getReference( "profile" );
        String id =FirebaseAuth.getInstance().getCurrentUser().getUid();
        String mobileNo=mAuth.getCurrentUser().getPhoneNumber();
        customerProfile=new Profile( id, "","0.0","0.0",false,"Customer",mobileNo,"","",c.toString() );

        datauserprofile.child( id ).setValue( customerProfile );
        Toast.makeText( getApplicationContext(), "Profile Added", Toast.LENGTH_LONG ).show();
        return true;

    }

}