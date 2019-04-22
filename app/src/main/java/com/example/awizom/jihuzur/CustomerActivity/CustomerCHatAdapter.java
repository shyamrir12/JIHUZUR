package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.Adapter.CatalogListAdapter;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.ChatModel;
import com.example.awizom.jihuzur.R;

import java.util.List;


public class CustomerCHatAdapter extends
        RecyclerView.Adapter<CustomerCHatAdapter.MyViewHolder> {

    private List<String> chatlist;
    private Context mCtx;


    public CustomerCHatAdapter(Context baseContext, List<String> chatlist) {
        this.chatlist = chatlist;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(CustomerCHatAdapter.MyViewHolder holder, int position) {
       // String c = chatlist.get(position);
       // holder.customermsg.setText(c.getCustomermsg());
      //  holder.employeemsg.setText(c.getCustomermsg());
    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    @Override
    public CustomerCHatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customer_chat_adapter, parent, false);
        return new CustomerCHatAdapter.MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView customermsg;
        public TextView employeemsg;


        public MyViewHolder(View view) {
            super(view);
            customermsg = (TextView) view.findViewById(R.id.customermsg);
            employeemsg = (TextView) view.findViewById(R.id.employeemsg);


        }
    }
}