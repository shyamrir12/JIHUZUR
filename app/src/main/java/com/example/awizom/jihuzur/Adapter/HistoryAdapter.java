package com.example.awizom.jihuzur.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awizom.jihuzur.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.OrderItemViewHolder> {
    private Context mCtx;
    ProgressDialog progressDialog;
    String orderList[];

    public HistoryAdapter(Context context, String[] orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;

    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.history_adapter, null);
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderItemViewHolder extends RecyclerView.ViewHolder {
        public OrderItemViewHolder(View itemView, Context mCtx, String[] orderList) {
            super(itemView);
        }
    }


}
