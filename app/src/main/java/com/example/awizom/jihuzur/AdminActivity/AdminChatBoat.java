package com.example.awizom.jihuzur.AdminActivity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.awizom.jihuzur.CustomerActivity.CustomerNewChatAdapter;
import com.example.awizom.jihuzur.Model.ChatModel;
import com.example.awizom.jihuzur.Model.CustomerChatModel;
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

public class AdminChatBoat extends AppCompatActivity {

    EditText chatcontain;
    ImageView sendmsg;
    FirebaseFirestore db;
    List<ChatModel> chatModelList;
    CustomerNewChatAdapter customerCHatAdapter;
    RecyclerView recyclerView;
    String result = "";
    String orderid, ImageCustomer, ImageEmployee;
    String client;
    String empid, cusid;
    List<String> list = new ArrayList<>();
    int z;
    Integer listsize;
    String cus_name, cus_mob, cus_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_chat_boat);
        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
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
        cus_name = SharedPrefManager.getInstance(this).getUser().getName().toString();
        cus_mob = SharedPrefManager.getInstance(this).getUser().getMobileNo().toString();
        cus_id = SharedPrefManager.getInstance(this).getUser().getID().toString();
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

                        final ProgressDialog progress = new ProgressDialog(AdminChatBoat.this);
                        progress.setMessage("Sending");
                        //  progress.show();
                        int x = listsize;
                        int y = 1;
                        z = x + y;

                        final Map<String, Object> chatboat = new HashMap<>();
                        chatboat.put("Count", z);
                        chatboat.put("customer_id", cus_id.toString());
                        chatboat.put("customer_name", cus_name.toString());
                        chatboat.put("customer_mob", cus_mob.toString());
                        chatboat.put("ChatContainCustomer", chatcontain.getText().toString());
                        chatboat.put("ChatContainAdmin", null);

                        db.collection("CustomerChat").document(cus_id.toString()).collection("Customermsg").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //  Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                                chatcontain.setText("");
                                initView();
                                progress.dismiss();
                                db.collection("ChatNotification").document().set(chatboat).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });

                            }
                        });


                    }
                }
            });
        }
        snapshotlisten();
        getchat();
    }


    private void snapshotlisten() {
        db.collection("CustomerChat").document(cus_id.toString()).collection("Customermsg").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
        db.collection("CustomerChat").document(cus_id.toString()).collection("Customermsg").orderBy("Count", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<CustomerChatModel> eventList = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        CustomerChatModel e = doc.toObject(CustomerChatModel.class);
                        // e.setChatContainCustomer(doc.getId());
                        eventList.add(e);
                    }
                    customerCHatAdapter = new
                            CustomerNewChatAdapter(AdminChatBoat.this, eventList, db);

                    listsize = eventList.size();
                    recyclerView.setAdapter(customerCHatAdapter);
                } else {
                    Log.d("not", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}