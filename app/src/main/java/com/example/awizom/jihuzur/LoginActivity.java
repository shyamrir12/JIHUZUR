package com.example.awizom.jihuzur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextMobile;
    private TextView empsign,skiplogin;
    private Button butonContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextMobile = findViewById(R.id.editTextMobile);
        empsign=findViewById(R.id.empsignin);
        butonContinue=findViewById(R.id.buttonContinue);
        skiplogin=findViewById(R.id.skiplogin);
        butonContinue.setOnClickListener(this);
        skiplogin.setOnClickListener(this);
        empsign.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
      switch (v.getId())
      {
          case R.id.buttonContinue:
              String mobile = editTextMobile.getText().toString().trim();

              if (mobile.isEmpty() || mobile.length() < 10) {
                  editTextMobile.setError("Enter a valid mobile");
                  editTextMobile.requestFocus();
                  return;
              }

              Intent intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
              intent.putExtra("mobile", mobile);
              startActivity(intent);
              break;


          case R.id.empsignin:
             String mobiles=editTextMobile.getText().toString().trim();

              if (mobiles.isEmpty() || mobiles.length() < 10) {
                  editTextMobile.setError("Enter a valid mobile");
                  editTextMobile.requestFocus();
                  return;
              }

              Intent i = new Intent(LoginActivity.this, VerifyPhoneActivityEmployee.class);
              i.putExtra("mobile", mobiles);
              startActivity(i);
              break;

          case R.id.skiplogin:
              Intent skip = new Intent(LoginActivity.this, CustomerHomePage.class);

              startActivity(skip);
              break;
      }


      }


    }
