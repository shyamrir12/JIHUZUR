package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirstLoginPage extends AppCompatActivity implements View.OnClickListener {

    private Button submitLoginButton;
    private EditText userId,userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_login_page);
        initView();
    }

    private void initView() {
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.userpwd);
        submitLoginButton = findViewById(R.id.submitLoginBtn);


        userId.setOnClickListener(this);
        userPassword.setOnClickListener(this);
        submitLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

       if(v.getId() == submitLoginButton.getId()) {
           login();
       }

    }

    private void login() {

        if(userId.getText().toString().equals("Admin") || userPassword.getText().toString().equals("test")){

            Intent intent = new Intent(this, AdminHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else  if(userId.getText().toString().equals("Customer") || userPassword.getText().toString().equals("test")){

            Intent intent = new Intent(this, CustomerHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else  if(userId.getText().toString().equals("Employee") || userPassword.getText().toString().equals("test")){

            Intent intent = new Intent(this, EmployeeHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, FirstLoginPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
