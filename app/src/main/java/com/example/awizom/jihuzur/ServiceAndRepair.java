package com.example.awizom.jihuzur;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ServiceAndRepair extends AppCompatActivity {

    TextView service1,service2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_and_repair);
        service1=(TextView)findViewById(R.id.service1);
        service2=(TextView)findViewById(R.id.service2);
        getACService();
    }

    private void getACService() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Catalog").child("Appliance Repair").child("AC Service & Repair");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot datas : dataSnapshot.getChildren()) {


                    String Service1 = dataSnapshot.child("Service1").getValue().toString();
                    String Service2 = dataSnapshot.child("Service2").getValue().toString();

                        service1.setText(Service1);
                        service2.setText(Service2);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
}


}
