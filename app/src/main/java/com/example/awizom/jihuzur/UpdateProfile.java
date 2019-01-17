package com.example.awizom.jihuzur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.awizom.jihuzur.Model.Profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class UpdateProfile extends AppCompatActivity {

EditText userName,idNo,idtype;
Button submit;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    Profile customerProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        Bundle bundle = getIntent().getExtras();

//Extract the data…
        String uname =bundle.getString("uname");
        String idenNo =bundle.getString("idenNo");
        String idenType =bundle.getString("idenType");

        mAuth = FirebaseAuth.getInstance();
        userName=(EditText)findViewById(R.id.uname);
        idNo=(EditText)findViewById(R.id.ino);
        idtype = (EditText)findViewById(R.id.itype);
        submit=(Button)findViewById(R.id.submit);

        userName.setText(uname);
        idNo.setText(idenNo);
        idtype.setText(idenType);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserProfile();

                Intent intent= new Intent(UpdateProfile.this,CustomerHomePage.class);
                startActivity(intent);
            }
        });



    }

    private boolean addUserProfile() {
        Date c = Calendar.getInstance().getTime();
        datauserprofile = FirebaseDatabase.getInstance().getReference("profile");
        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String mobileNo = mAuth.getCurrentUser().getPhoneNumber();
        String name = userName.getText().toString();
        String idno = idNo.getText().toString();
        String idtyp = idtype.getText().toString();
        //customerProfile = new Profile(id, name, "0.0", "0.0", false, "Customer", mobileNo, idtyp, idno, c.toString());

        datauserprofile.child(id).setValue(customerProfile);
        Toast.makeText(getApplicationContext(), "Profile Added", Toast.LENGTH_LONG).show();
        return true;
    }
    }
