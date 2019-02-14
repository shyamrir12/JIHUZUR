package com.example.awizom.jihuzur.CustomerActivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerReplyAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Reply;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerReplyActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView sendButton,reviewMsz,reviewDate;
    private EditText edittxtViewReply;
    private Reply reply;
    private List<Reply> replyList;
    private String result = "",reviewID="",reView,reviewdate,replyID="0";
    CustomerReplyAdapter customerReplyAdapter;
    private boolean active=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reply_list);
        initView();
    }

    private void initView() {

        reviewID = getIntent().getStringExtra("ReviewID");
        reView = getIntent().getStringExtra("ReView");
        reviewdate = getIntent().getStringExtra("Reviewdate");




        sendButton = findViewById(R.id.sendBtn);
        edittxtViewReply = findViewById(R.id.txtReply);
        reviewMsz = findViewById(R.id.reviewMsg);
        reviewDate = findViewById(R.id.reviewdate);

        reviewMsz.setText(reView.toString());
        reviewDate.setText(reviewdate.toString());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sendButton.setOnClickListener(this);
        getReplyList();
    }
    private void getReplyList() {
        try {
            result = new CustomerOrderHelper.GetReviewReplyList().execute(reviewID.toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Reply>>() {
            }.getType();
            replyList = new Gson().fromJson(result, listType);
            customerReplyAdapter = new CustomerReplyAdapter(CustomerReplyActivity.this,replyList );
            recyclerView.setAdapter(customerReplyAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sendBtn:
                postReply();
                break;
        }
    }

    private void postReply() {

        try {
            result = new CustomerOrderHelper.PostReviewReply().execute(replyID.toString(),edittxtViewReply.getText().toString(),reviewID.toString(), String.valueOf(active)).get();
            if (!result.isEmpty()) {
                Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
