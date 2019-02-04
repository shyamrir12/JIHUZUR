package com.example.awizom.jihuzur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private EditText editTextMobile;
    private TextView empsign,skiplogin;
    private Button butonContinue,createuser;
    private String mobileNumber="",mobile="";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        createuser=findViewById(R.id.signUp);
        createuser.setOnClickListener(this);


        try {


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
              redirectPage();
              break;


          case R.id.empsignin:
              redirectPage();
              break;

          case R.id.skiplogin:
              intent = new Intent(LoginActivity.this, CustomerHomePage.class);
              startActivity(intent);
              break;

          case R.id.signUp:
               intent = new Intent(LoginActivity.this, RegistrationActivity.class);
               startActivity(intent);
              break;
      }


      }

    private void redirectPage() {
        mobile= editTextMobile.getText().toString();

         if(editTextMobile.getText().toString().equals(mobileNumber)){

            intent = new Intent(LoginActivity.this, CustomerHomePage.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);

//        }else if(!editTextMobile.getText().toString().equals(mobileNumber)) {
//
//            intent = new Intent(LoginActivity.this, VerifyPhoneActivity.class);
//            intent.putExtra("mobile", mobile);
//            startActivity(intent);

        }else {

            intent = new Intent(LoginActivity.this, LoginActivity.class);
            startActivity(intent);

        }
    }


}
