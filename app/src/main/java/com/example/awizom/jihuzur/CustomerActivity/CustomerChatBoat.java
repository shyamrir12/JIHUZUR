package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ProgressDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPricingAdapter;
import com.example.awizom.jihuzur.Model.ChatModel;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CustomerChatBoat extends AppCompatActivity {

    EditText chatcontain;
    ImageView sendmsg;
    FirebaseFirestore db;
    List<ChatModel> chatModelList;
    CustomerCHatAdapter customerCHatAdapter;
    RecyclerView recyclerView;
    String result = "";
    String orderid;
    List<String> list = new ArrayList<>();
    Integer count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat_boat);
        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        final String empid = getIntent().getStringExtra("EmployeeID");
        final String cusid = getIntent().getStringExtra("CustomerID");
        final String client = getIntent().getStringExtra("Client");
        orderid = getIntent().getStringExtra("OrderID");
        toolbar.setTitle(client);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        db = FirebaseFirestore.getInstance();
        chatcontain = (EditText) findViewById(R.id.chatcontain);
        sendmsg = (ImageView) findViewById(R.id.send);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(this.getApplicationContext());
        recyclerView.setLayoutManager(recyclerLayoutManager);
        getchat();

        {
            sendmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog progress = new ProgressDialog(CustomerChatBoat.this);
                    progress.setMessage("Sending");
                    progress.show();

                    if (SharedPrefManager.getInstance(CustomerChatBoat.this).getUser().getRole().equals("Customer")) {
                        Map<String, Object> chatboat = new HashMap<>();
                        chatboat.put("Count", count);
                        chatboat.put("EmployeeID", empid.toString());
                        chatboat.put("CustomerID", cusid.toString());
                        chatboat.put("OrderID", orderid.toString());
                        chatboat.put("ChatContainCustomer", chatcontain.getText().toString());
                        chatboat.put("ChatContainEmployee", null);

                        db.collection("Chat").document(orderid.toString()).collection("Customermsg").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                getchat();
                                progress.dismiss();
                            }
                        });
                    } else if (SharedPrefManager.getInstance(CustomerChatBoat.this).getUser().getRole().equals("Employee")) {
                        Map<String, Object> chatboat = new HashMap<>();
                        chatboat.put("Count", count);
                        chatboat.put("EmployeeID", empid.toString());
                        chatboat.put("CustomerID", cusid.toString());
                        chatboat.put("OrderID", orderid.toString());
                        chatboat.put("ChatContainCustomer", null);
                        chatboat.put("ChatContainEmployee", chatcontain.getText().toString());

                        db.collection("Chat").document(orderid.toString()).collection("Customermsg").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                getchat();
                                progress.dismiss();
                            }
                        });
                    }
                }
            });
        }
    }

    private void getchat() {
        db.collection("Chat").document(orderid.toString()).collection("Customermsg").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ChatModel> eventList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        ChatModel e = doc.toObject(ChatModel.class);
                        try {
                            if (!doc.get("count").equals(null)) {
                                count = Integer.parseInt(doc.get("count").toString());
                            } else {
                                count = 1;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        // e.setChatContainCustomer(doc.getId());
                        eventList.add(e);
                    }
                    customerCHatAdapter = new
                            CustomerCHatAdapter(CustomerChatBoat.this, eventList, db);
                    recyclerView.setAdapter(customerCHatAdapter);
                } else {
                    Log.d("not", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
