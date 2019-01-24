package com.example.awizom.jihuzur.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class CurrentOrderAdapter extends RecyclerView.Adapter<CurrentOrderAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;



    public CurrentOrderAdapter(Context currentOrderActivity, List<Order> orderList) {
        this.mCtx = currentOrderActivity;
        this.orderitemList = orderList;

    }




    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_current_order_layout, null);
        return new OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        try {

            order = orderitemList.get(position);
            holder.headerName.setText(String.valueOf(order.getOrderID()));
            holder.startTime.setText( order.getEmployeeID().toString());
            holder.endtime.setText( order.getCustomerID().toString());

        } catch (Exception E) {
            E.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return orderitemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        private Context mCtx;
        private TextView startTime,endtime,headerName;
        private Button acceptBtn,trackinBtn,canclBtn;
        private List<Order> orderitemList;


        public OrderItemViewHolder(View view, Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

            headerName = itemView.findViewById(R.id.headerName);
            startTime = itemView.findViewById(R.id.starttime);
            endtime = itemView.findViewById(R.id.endtime);

            acceptBtn = itemView.findViewById(R.id.acceptOtpBtn);
            trackinBtn = itemView.findViewById(R.id.trackBtn);
            canclBtn = itemView.findViewById(R.id.cancelBtn);
            acceptBtn.setOnClickListener(this);
            trackinBtn.setOnClickListener(this);
            canclBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {


        }




    }

}
