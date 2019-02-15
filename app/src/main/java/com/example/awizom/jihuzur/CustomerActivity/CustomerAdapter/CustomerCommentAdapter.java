package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.jihuzur.CustomerActivity.CustomerCommentActivity;
import com.example.awizom.jihuzur.CustomerActivity.CustomerReplyActivity;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Review;
import com.example.awizom.jihuzur.R;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerCommentAdapter extends  RecyclerView.Adapter<CustomerCommentAdapter.MyViewHolder> {

    private Context mCtx;
    private List<Review> reviewList;
    private Review reviews;
    private Intent intent;
    private String result="",replyID="0";
    private boolean active=true;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView sendMsz, reviewDate,reviewRateText,reviewDateText,replyBtn,reviewID,sendButton;
        private EditText edittxtViewReply;
        private LinearLayout linearLayout;
        RatingBar setRatingBar;
        public MyViewHolder(View v) {
            super(v);

            sendMsz = itemView.findViewById(R.id.senderTextMessage);
            reviewDate = itemView.findViewById(R.id.reply);
            reviewRateText = itemView.findViewById(R.id.reviewRate);
            reviewDateText = itemView.findViewById(R.id.reviewDate);
            replyBtn = itemView.findViewById(R.id.replyBtn);
            reviewID = itemView.findViewById(R.id.reviewId);

            linearLayout = itemView.findViewById(R.id.l0);
            sendButton = itemView.findViewById(R.id.sendBtn);
            edittxtViewReply = itemView.findViewById(R.id.txtReply);

            setRatingBar=itemView.findViewById(R.id.ratingSet);


        }
    }

    public CustomerCommentAdapter(CustomerCommentActivity customerCommentActivity, List<Review> reviews) {
        this.mCtx = customerCommentActivity;
        this.reviewList = reviews;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        reviews = reviewList.get(position);
        try {
            holder.sendMsz.setText(reviews.getReview().toString());
            holder.reviewDate.setText(reviews.getReviewDate());
            holder.reviewRateText.setText(String.valueOf(reviews.getRate()));
            holder.reviewDateText.setText(reviews.getReviewDate());
            holder.reviewID.setText(String.valueOf(reviews.getReviewID()));
            holder.setRatingBar.setRating(Float.parseFloat(String.valueOf(reviews.getRate())));
            holder.replyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //holder.linearLayout.setVisibility(View.VISIBLE);
                    intent = new Intent(mCtx, CustomerReplyActivity.class);
                    intent.putExtra("ReviewID",holder.reviewID.getText().toString());
                    intent.putExtra("ReView",holder.sendMsz.getText().toString());
                    intent.putExtra("Reviewdate",holder.reviewDateText.getText().toString());
                    mCtx.startActivity(intent);
                }
            });

            holder.sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        result = new CustomerOrderHelper.PostReviewReply().execute(replyID.toString(),holder.edittxtViewReply.getText().toString(),holder.reviewID.getText().toString(), String.valueOf(active)).get();
                        if (!result.isEmpty()) {
                            Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                            holder.linearLayout.setVisibility(View.GONE);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
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
