package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerReplyAdapter;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Reply;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerReplyActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView sendButton, reviewMsz, reviewDate;
    private EditText edittxtViewReply;
    private Reply reply;
    private List<Reply> replyList;
    private String result = "", reviewID = "", reView, reviewdate, replyID = "0", total = "2";
    private LinearLayout linearLayout;
    CustomerReplyAdapter customerReplyAdapter;
    private boolean active = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ViewDialog viewDialog;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    private de.hdodenhof.circleimageview.CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reply_list);
        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/

        toolbar.setTitle("Reply");


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        linearLayout = findViewById(R.id.l1);
        reviewID = getIntent().getStringExtra("ReviewID");
        reView = getIntent().getStringExtra("ReView");
        reviewdate = getIntent().getStringExtra("Reviewdate");
        total = getIntent().getStringExtra("total");
        viewDialog = new ViewDialog(this);
        sendButton = findViewById(R.id.sendBtn);
        circleImageView = findViewById(R.id.circle_image);
        edittxtViewReply = findViewById(R.id.txtReply);
        reviewMsz = findViewById(R.id.reviewMsg);
        reviewDate = findViewById(R.id.reviewdate);
        reviewMsz.setText(reView.toString());
        reviewDate.setText(reviewdate.toString().split("T")[0]);
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sendButton.setOnClickListener(this);
        if (total.equals("check".toString())) {

        }
        else{

            circleImageView.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);
            edittxtViewReply.setVisibility(View.VISIBLE);
        }

        getReplyList();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getReplyList();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    private void getReplyList() {
        try {
            mSwipeRefreshLayout.setRefreshing(true);
            result = new CustomerOrderHelper.GetReviewReplyList().execute(reviewID.toString()).get();
            mSwipeRefreshLayout.setRefreshing(false);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Reply>>() {
            }.getType();
            replyList = new Gson().fromJson(result, listType);
            customerReplyAdapter = new CustomerReplyAdapter(CustomerReplyActivity.this, replyList);
            recyclerView.setAdapter(customerReplyAdapter);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClick);
        switch (v.getId()) {
            case R.id.sendBtn:
                showCustomLoadingDialog();
                postReply();
                break;
        }
    }

    private void postReply() {

        try {
            if (validate()) {
                result = new CustomerOrderHelper.PostReviewReply().execute(replyID.toString(), edittxtViewReply.getText().toString(), reviewID.toString(), String.valueOf(active)).get();
                if (!result.isEmpty()) {
                    //Toast.makeText(this, result.toString(), Toast.LENGTH_SHORT).show();
                    edittxtViewReply.setText("");
                }

                getReplyList();
            } else {
                Toast.makeText(getApplicationContext(), "Please some write", Toast.LENGTH_SHORT).show();

            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean validate() {
        if (edittxtViewReply.getText().toString().isEmpty()) {
            edittxtViewReply.setError("Enter a valid mobile");
            edittxtViewReply.requestFocus();
            return false;
        }
        return true;
    }

    public void showCustomLoadingDialog() {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }
}
