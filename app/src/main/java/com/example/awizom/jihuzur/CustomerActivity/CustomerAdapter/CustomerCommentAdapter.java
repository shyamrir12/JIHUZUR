package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.CustomerActivity.CustomerCommentActivity;
import com.example.awizom.jihuzur.Model.Review;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class CustomerCommentAdapter extends  RecyclerView.Adapter<CustomerCommentAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Review> reviewList;
    private Review reviews;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView sendMsz, reply,reviewRateText,reviewDateText;
        public MyViewHolder(View v) {
            super(v);

            sendMsz = itemView.findViewById(R.id.senderTextMessage);
            reply = itemView.findViewById(R.id.reply);
            reviewRateText = itemView.findViewById(R.id.reviewRate);
            reviewDateText = itemView.findViewById(R.id.reviewDate);
        }
    }

    public CustomerCommentAdapter(CustomerCommentActivity customerCommentActivity, List<Review> reviews) {
        this.mCtx = customerCommentActivity;
        this.reviewList = reviews;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        reviews = reviewList.get(position);

        try {
            holder.sendMsz.setText(reviews.getReview().toString());
            holder.reply.setText(reviews.getReviewDate());
            holder.reviewRateText.setText(String.valueOf(reviews.getRate()));
            holder.reviewDateText.setText(reviews.getReviewDate());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_comment_adapter, null);
        return new CustomerCommentAdapter.MyViewHolder(view);

    }

}
