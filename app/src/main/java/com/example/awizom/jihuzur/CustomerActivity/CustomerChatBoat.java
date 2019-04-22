package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPricingAdapter;
import com.example.awizom.jihuzur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CustomerChatBoat extends AppCompatActivity {


    EditText chatcontain;
    ImageView sendmsg;
    FirebaseFirestore db;
    CustomerCHatAdapter customerCHatAdapter;
    RecyclerView recyclerView;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat_boat);
        initView();
    }

    private void initView() {

        final String empid = getIntent().getStringExtra("EmployeeID");
        final String cusid = getIntent().getStringExtra("CustomerID");
        final String orderid = getIntent().getStringExtra("OrderID");
        db = FirebaseFirestore.getInstance();
        chatcontain = (EditText) findViewById(R.id.chatcontain);
        sendmsg = (ImageView) findViewById(R.id.send);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        db.collection("Chat").document(orderid.toString()).collection("Customermsg").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("get", document.getId() + " => " + document.getData());
                        list.add(document.getData().toString());

                        Toast.makeText(getApplicationContext(), document.getData().toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("not", "Error getting documents: ", task.getException());
                }
            }
        });
        customerCHatAdapter = new CustomerCHatAdapter(CustomerChatBoat.this,
                list);
        recyclerView.setAdapter(customerCHatAdapter);
        {
            sendmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> chatboat = new HashMap<>();
                    chatboat.put("EmployeeID", empid.toString());
                    chatboat.put("CustomerID", cusid.toString());
                    chatboat.put("OrderID", orderid.toString());
                    chatboat.put("ChatContainCustomer", chatcontain.getText().toString());

                    db.collection("Chat").document(orderid.toString()).collection("Customermsg").document(chatcontain.getText().toString()).set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

    }
}
