package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RepairServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.repair_layout);
        initView();

    }

    private void initView() {
        getSupportActionBar().setTitle("Repair & Service");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nextButton = findViewById(R.id.buttonNext);
        nextButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == nextButton.getId()){
            intent = new Intent(this, LocationActivity.class);
            startActivity(intent);
        }
    }
}
