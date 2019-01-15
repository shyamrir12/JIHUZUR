package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstPage extends AppCompatActivity implements View.OnClickListener {

    private Button skipBtn,cusBtn,empBtn,adminBtn;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstpage);
        initView();
    }

    private void initView() {

        skipBtn = findViewById(R.id.skiploginBtn);
        cusBtn = findViewById(R.id.cusmrLoginBtn);
        empBtn = findViewById(R.id.empLoginBtn);
        adminBtn = findViewById(R.id.adminLoginBtn);

        skipBtn.setOnClickListener(this);
        cusBtn.setOnClickListener(this);
        empBtn.setOnClickListener(this);
        adminBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == skipBtn.getId()){

            startActivity( intent = new Intent(this, CustomerHomePage.class));

        }if(v.getId() == cusBtn.getId()){

            Intent intent = new Intent(this, CustomerHomePage.class);
            startActivity(intent);

        }if(v.getId() == empBtn.getId()){

            Intent intent = new Intent(this, EmployeeHomePage.class);
            startActivity(intent);

        }if(v.getId() == adminBtn.getId()){

            Intent intent = new Intent(this, AdminHomePage.class);
            startActivity(intent);

        }
    }
}
