package com.example.awizom.jihuzur;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Helper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.LoginHelper;
import com.example.awizom.jihuzur.Model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextMobile;
    private TextView  skiplogin;
    private Button butonContinue;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    private String mobileNumber = "", mobile = "",ur = "User";
    Profile customerProfile;
    private ProgressDialog progressDialog;
    Intent intent;
    private Spinner role;
    private LoginHelper loginHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();

    }

    private void initView() {

        editTextMobile = findViewById(R.id.editTextMobile);
        butonContinue=findViewById(R.id.buttonContinue);
        skiplogin=findViewById(R.id.skiplogin);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonContinue:
                if(validation()){

                    createuser();
                }
              else
                {
                    Toast.makeText(getApplicationContext(),"Mobile No is not Valid",Toast.LENGTH_SHORT).show();

                }
                break;

                case R.id.skiplogin:
                Intent skip = new Intent(RegistrationActivity.this, CustomerHomePage.class);
                startActivity(skip);
                break;
        }
    }

    private boolean validation() {


        if (editTextMobile.getText().toString().isEmpty() || editTextMobile.getText().toString().length() < 10) {

            editTextMobile.setError("Enter a valid mobile");
            editTextMobile.requestFocus();

            return false;
        }
        return true;
    }


    private void createuser() {

        String name = editTextMobile.getText().toString().trim();

        String ur = "Customer";
        if (role != null)
            ur = role.getSelectedItem().toString().trim();
        try {

            try {
                new LoginHelper.GetLogin().execute(name,ur);
            } catch (Exception e) {

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


}