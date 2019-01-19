package com.example.awizom.jihuzur;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectServices extends AppCompatActivity implements View.OnClickListener {

    TextView service1,service2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_and_repair);
       initView();
    }

    private void initView() {
        getSupportActionBar().setTitle("Ac Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        service1=(TextView)findViewById(R.id.service1);
        service2=(TextView)findViewById(R.id.service2);
        service1.setOnClickListener(this);
        service2.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == service1.getId()){
            Intent i = new Intent(this, RepairServiceActivity.class);
            startActivity(i);
        }
    }
}
