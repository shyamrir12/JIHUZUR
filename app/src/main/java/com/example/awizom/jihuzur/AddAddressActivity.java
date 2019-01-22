package com.example.awizom.jihuzur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener {

    private Button nextButton;
    private EditText nameEditText,houseEditText,localityEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_address_layout);
        initView();
    }

    private void initView() {

        getSupportActionBar().setTitle("Add Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nextButton = findViewById(R.id.NextButton);
        nameEditText = findViewById(R.id.name);
        houseEditText = findViewById(R.id.houseStreet);
        localityEditText = findViewById(R.id.locality);

        nextButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == nextButton.getId()){
           if(validation()) {
               
           }
        }
    }

    private boolean validation() {
        if (nameEditText.getText().toString().isEmpty() || houseEditText.getText().toString().isEmpty() || localityEditText.getText().toString().isEmpty() ) {

            nameEditText.setError("Required");
            houseEditText.setError("Required");
            localityEditText.setError("Required");

        }
        return false;
    }
}
