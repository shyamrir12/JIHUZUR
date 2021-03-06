package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.ChatModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
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
    List<ChatModel> chatModelList;
    CustomerCHatAdapter customerCHatAdapter;
    RecyclerView recyclerView;
    String result = "";
    String orderid, ImageCustomer, ImageEmployee;
    String client;
    String empid, cusid;
    List<String> list = new ArrayList<>();
    int z;
    LinearLayout coordinatorLayout;
    Snackbar snackbar;
    Integer listsize;
    ImageView nointernet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat_boat);
        empid = getIntent().getStringExtra("EmployeeID");
        cusid = getIntent().getStringExtra("CustomerID");
        client = getIntent().getStringExtra("Client");
        orderid = getIntent().getStringExtra("OrderID");
        nointernet=findViewById(R.id.no_internet);
        coordinatorLayout = (LinearLayout) findViewById(R.id.coordinator);
        snackbar = Snackbar.make(coordinatorLayout, "No internet connection!", Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CustomerChatBoat.this,
                                CustomerHomePage.class);

                        startActivity(intent);
                    }
                });
        snackbar.setActionTextColor(Color.RED);
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);
        checkInternet();
     //   initView();
    }

    private void checkInternet() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            initView();

        } else {
            // Toast.makeText(getApplicationContext(), "Internet is off", Toast.LENGTH_SHORT).show();
            nointernet.setVisibility(View.VISIBLE);
            snackbar.show();
        /*    connected = false;
            snackbar.show();*/
        }
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
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
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        //  recyclerLayoutManager.setReverseLayout(true);


        recyclerView.setLayoutManager(recyclerLayoutManager);
        {
            sendmsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (chatcontain.getText().toString().equals("")) {
                        chatcontain.setError("Chat Contain should not be blank");
                        chatcontain.requestFocus();
                    } else {

                        final ProgressDialog progress = new ProgressDialog(CustomerChatBoat.this);
                        progress.setMessage("Sending");
                        //  progress.show();
                        int x = listsize;
                        int y = 1;
                        z = x + y;

                        if (SharedPrefManager.getInstance(CustomerChatBoat.this).getUser().getRole().equals("Customer")) {
                            final Map<String, Object> chatboat = new HashMap<>();
                            chatboat.put("Count", z);
                            chatboat.put("EmployeeID", empid.toString());
                            chatboat.put("CustomerID", cusid.toString());
                            chatboat.put("OrderID", orderid.toString());
                            chatboat.put("ChatContainCustomer", chatcontain.getText().toString());
                            chatboat.put("ChatContainEmployee", null);

                            db.collection("Chat").document(orderid.toString()).collection("Customermsg").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //  Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                    chatcontain.setText("");
                                    initView();
                                    progress.dismiss();


                                }
                            });



                        } else if (SharedPrefManager.getInstance(CustomerChatBoat.this).getUser().getRole().equals("Employee")) {
                            final Map<String, Object> chatboat = new HashMap<>();
                            chatboat.put("Count", z);
                            chatboat.put("EmployeeID", empid.toString());
                            chatboat.put("CustomerID", cusid.toString());
                            chatboat.put("OrderID", orderid.toString());
                            chatboat.put("ChatContainCustomer", null);
                            chatboat.put("ChatContainEmployee", chatcontain.getText().toString());

                            db.collection("Chat").document(orderid.toString()).collection("Customermsg").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //   Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                    chatcontain.setText("");
                                    initView();
                                    progress.dismiss();
                                    db.collection("ChatNotification").document(cusid.toString()).set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }
                            });


                        }
                    }
                }
            });
        }
        snapshotlisten();
        getchat();
    }


    private void snapshotlisten() {
        db.collection("Chat").document(orderid.toString()).collection("Customermsg").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            //  Log.d("TAG", "New Msg: " + dc.getDocument().toObject(Message.class));
                            getchat();
                            break;
                        case MODIFIED:

                            //      Log.d("TAG", "Modified Msg: " + dc.getDocument().toObject(Message.class));
                            break;
                        case REMOVED:
                            //     Log.d("TAG", "Removed Msg: " + dc.getDocument().toObject(Message.class));
                            break;
                    }
                }
            }
        });
    }


    private void getchat() {
        db.collection("Chat").document(orderid.toString()).collection("Customermsg").orderBy("Count", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<ChatModel> eventList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        ChatModel e = doc.toObject(ChatModel.class);
                        // e.setChatContainCustomer(doc.getId());
                        eventList.add(e);
                    }
                    customerCHatAdapter = new
                            CustomerCHatAdapter(CustomerChatBoat.this, eventList, db);

                    listsize = eventList.size();
                    recyclerView.setAdapter(customerCHatAdapter);
                } else {
                    Log.d("not", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
