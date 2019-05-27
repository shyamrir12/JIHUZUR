package com.example.awizom.jihuzur.AdminActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.awizom.jihuzur.CustomerActivity.CustomerNewChatBoat;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import static com.example.awizom.jihuzur.EmployeeActivity.VerifyPhoneActivityEmployeee.TAG;

public class AdminCusList extends AppCompatActivity {

    FirebaseFirestore db;
    ListView listView;
    String result = "";
    private String[] customer_name, customer_mob;
    String cus_name;
    ViewGroup.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_cus_list);
        db = FirebaseFirestore.getInstance();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/
        toolbar.setTitle("Customer Help");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               /* layoutParams = view.getLayoutParams();

                //Define your height here.
                layoutParams.height = 350;
                view.setLayoutParams(layoutParams);*/

                String texts = String.valueOf((listView.getItemAtPosition(position)));
                String cus_name = texts.split(",")[0].toString();
                String cus_mob = texts.split(",")[1].toString();

                Intent intent = new Intent(AdminCusList.this, CustomerNewChatBoat.class);
                intent.putExtra("cus_name", cus_name);
                intent.putExtra("cus_mob", cus_mob);
                intent.putExtra("cus_id",texts);
                startActivity(intent);
            }
        });
        getlistcu();
    }

    private void getlistcu() {

        db.collection("CustomerHelp").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        customer_name = new String[task.getResult().size()];
                        customer_mob = new String[task.getResult().size()];
                        for (int i = 0; i < task.getResult().size(); i++) {
                            customer_name[i] = document.getId().toString();
                            //   customer_mob[i] = document.getId().split(",")[1].toString();
                        }

                    }
                    ArrayAdapter<String> itemsAdapter =
                            new ArrayAdapter<String>(AdminCusList.this, R.layout.mylayoutxml, list);

                    listView.setAdapter(itemsAdapter);
                    Log.d(TAG, list.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


    }
}
