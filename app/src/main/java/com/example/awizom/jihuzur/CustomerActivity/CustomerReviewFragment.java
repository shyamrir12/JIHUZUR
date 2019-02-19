package com.example.awizom.jihuzur.CustomerActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;

public class CustomerReviewFragment  extends Fragment implements View.OnClickListener {

    FloatingActionButton addReview;
    TextView txtRatingValue;
    EditText review;
    RatingBar ratingBar;
    String result;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_customerreview,container,false);
        initView(view);
        return  view;
    }

    private void initView(View view) {

     addReview =(FloatingActionButton)view.findViewById(R.id.addReview);
        addReview.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==addReview.getId())
        {
            addReviewforOrder();

        }
    }

    public void addReviewforOrder() {

        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_reviewbycustomer, null);

        dialogBuilder.setView(dialogView);
         ratingBar=(RatingBar)dialogView.findViewById(R.id.rating);
         review=(EditText) dialogView.findViewById(R.id.review) ;


        txtRatingValue = (TextView) dialogView.findViewById(R.id.txtRatingValue);
        txtRatingValue.setText("3.0");
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
            }
        });

        final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);




        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Review");
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String rate = txtRatingValue.getText().toString().split("",3)[1];
                String revie = review.getText().toString().trim();


                try {
                    result = new CustomerOrderHelper.CustomerPostRating().execute(revie,rate).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(getContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
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