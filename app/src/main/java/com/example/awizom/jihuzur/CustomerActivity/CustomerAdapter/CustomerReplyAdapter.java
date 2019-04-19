package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.awizom.jihuzur.CustomerActivity.CustomerReplyActivity;
import com.example.awizom.jihuzur.Model.Reply;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class CustomerReplyAdapter extends  RecyclerView.Adapter<CustomerReplyAdapter.MyViewHolder>  {

    private Context mCtx;
    private List<Reply> replyList;
    private Reply reply;
    private Intent intent;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView replyIDs, replyTxt, replyDate;
        public MyViewHolder(View v) {
            super(v);

            replyIDs = itemView.findViewById(R.id.replyID);
            replyTxt = itemView.findViewById(R.id.replyText);
            replyDate = itemView.findViewById(R.id.replydate);

        }
    }


    public CustomerReplyAdapter(CustomerReplyActivity customerReplyActivity, List<Reply> replyList) {
        this.mCtx = customerReplyActivity;
        this.replyList = replyList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        reply = replyList.get(position);

        try {
            holder.replyIDs.setText(String.valueOf(reply.getReplyID()));
            holder.replyTxt.setText(reply.getReply());
            holder.replyDate.setText(reply.getReplyDate().split("T")[0]);

        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public int getItemCount() {
        return replyList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_customer_reply_adapter, null);
        return new CustomerReplyAdapter.MyViewHolder(view);

    }


}
