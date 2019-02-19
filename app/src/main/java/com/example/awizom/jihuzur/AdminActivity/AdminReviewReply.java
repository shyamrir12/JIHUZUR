package com.example.awizom.jihuzur.AdminActivity;

import android.app.ActivityOptions;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.AdminComplaintReplyAdapter;
import com.example.awizom.jihuzur.Adapter.AdminReviewReplyAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Complaint;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Review;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AdminReviewReply extends AppCompatActivity {
    FloatingActionButton addReviewReply;
    AutoCompleteTextView editreviewreply, editreviewID;
    List<Review> reviewList;
    AdminReviewReplyAdapter adminReviewReplyAdapter;
    String result = "";
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_admin_review_reply);
        initView();
    }

    private void initView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Admin's ReviewReply");

        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityOptions options =
                        ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left, R.anim.fui_slide_in_right);
                onBackPressed();
            }
        });
        addReviewReply = (FloatingActionButton) findViewById(R.id.addReviewReply);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getReviewListByOrderID();
        addReviewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddComplaintReply();
            }
        });


    }

    private void getReviewListByOrderID() {

        try {
            result = new AdminHelper.GETReviewListByORderID().execute().get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Review>>() {
            }.getType();
            reviewList = new Gson().fromJson(result, listType);
            adminReviewReplyAdapter = new AdminReviewReplyAdapter(AdminReviewReply.this, reviewList);
            recyclerView.setAdapter(adminReviewReplyAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAddComplaintReply() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_reviewreply, null);
        dialogBuilder.setView(dialogView);
        editreviewreply = (AutoCompleteTextView) dialogView.findViewById(R.id.editReviewreply);
        editreviewID = (AutoCompleteTextView) dialogView.findViewById(R.id.editreviewID);

        final Button buttonAddReviewReply = (Button) dialogView.findViewById(R.id.buttonAddreviewReply);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Complaint's Reply");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddReviewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String reviewreply = editreviewreply.getText().toString().trim();
                String reviewID = editreviewID.getText().toString().trim();

                try {
                    result = new AdminHelper.POSTReviewReply().execute(reviewreply, reviewID).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
                } catch (Exception e) {

                }

                b.dismiss();

            }


        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */

            }
        });
    }


}
