package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.ChatModel;
import com.example.awizom.jihuzur.Model.CustomerChatModel;
import com.example.awizom.jihuzur.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class CustomerNewChatAdapter extends
        RecyclerView.Adapter<CustomerNewChatAdapter.MyViewHolder> {

    private List<CustomerChatModel> chatlist;
    private Context mCtx;
    private FirebaseFirestore firestoreDB;


    public CustomerNewChatAdapter(Context baseContext, List<CustomerChatModel> chatlist, FirebaseFirestore db) {
        this.chatlist = chatlist;
        this.mCtx = baseContext;
        this.firestoreDB = db;
    }

    @Override
    public void onBindViewHolder(CustomerNewChatAdapter.MyViewHolder holder, int position) {
        final int itemPos = position;
        CustomerChatModel c = chatlist.get(position);
        /* introducing most important line for get data from firestore set this holder as recyclable*/
        holder.setIsRecyclable(false);
        if (c.getChatContainCustomer() != null) {
            holder.customermsg.setText(String.valueOf(c.getChatContainCustomer()));
        } else {
            holder.cus_linear.setVisibility(View.GONE);
            holder.customermsg.setVisibility(View.GONE);
        }
        if (c.getChatContainAdmin() != null) {
            holder.employeemsg.setText(String.valueOf(c.getChatContainAdmin()));
        } else {
            holder.emp_card.setVisibility(View.GONE);
            holder.employeemsg.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    @Override
    public CustomerNewChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_chat_adapter, parent, false);

        return new CustomerNewChatAdapter.MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customermsg;
        public TextView employeemsg;
        LinearLayout cus_linear;
        android.support.v7.widget.CardView emp_card;

        public MyViewHolder(View view) {
            super(view);
            customermsg = (TextView) view.findViewById(R.id.customermsg);
            employeemsg = (TextView) view.findViewById(R.id.employeemsg);
            cus_linear = (LinearLayout) view.findViewById(R.id.cus_linear);
            emp_card = (android.support.v7.widget.CardView) view.findViewById(R.id.emp_card);
        }
    }
}