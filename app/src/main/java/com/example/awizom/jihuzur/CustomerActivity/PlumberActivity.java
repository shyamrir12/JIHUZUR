package com.example.awizom.jihuzur.CustomerActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class PlumberActivity extends AppCompatActivity {
    FirebaseFirestore db;
    TextView plumberCLicks;
    String plumbercount;
    LinearLayout timelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plumber);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/
        toolbar.setTitle("JiHuzzur");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        plumberCLicks = (TextView) findViewById(R.id.plumberclicks);
        timelayout = (LinearLayout) findViewById(R.id.timerlinear);
        timelayout.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        if (SharedPrefManager.getInstance(this).getUser().getRole().equals("Admin")) {
            timelayout.setVisibility(View.VISIBLE);
        }
        //  getActionBar().setDisplayHomeAsUpEnabled(true);

        getcount();
    }

    private void getcount() {
        DocumentReference docRef = db.collection("PlumberCounting").document("1423");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        plumbercount = document.get("count").toString();
                        plumberCLicks.setText(plumbercount);
                        int x = Integer.parseInt(plumbercount.toString());
                        int y = 1;
                        int z = x + y;
                        Map<String, Object> PlumberCounting = new HashMap<>();
                        PlumberCounting.put("count", z);

                        if (SharedPrefManager.getInstance(PlumberActivity.this).getUser().getRole().equals("Customer")) {
                            db.collection("PlumberCounting").document("1423").set(PlumberCounting).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }

                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //   Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}
