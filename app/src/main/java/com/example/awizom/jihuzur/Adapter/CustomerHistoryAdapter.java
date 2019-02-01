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

public class CustomerHistoryAdapter extends RecyclerView.Adapter<CustomerHistoryAdapter.OrderItemViewHolder> {
    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId="",otpCode="",result="";
    int curposition;

    public CustomerHistoryAdapter(Context context, List<Order> orderList) {
        this.mCtx = context;
        this.orderitemList = orderList;
    }

    @NonNull
    @Override
    public CustomerHistoryAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.history_adapter, null);
        return new CustomerHistoryAdapter.OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHistoryAdapter.OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);

        try {
            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());

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

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        private TextView startTime,endtime,headerName;
        private Button deleteBtn;
        private List<Order> orderitemList;

        public OrderItemViewHolder(View view, Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

            headerName = itemView.findViewById(R.id.headerName);
            startTime = itemView.findViewById(R.id.starttime);
            endtime = itemView.findViewById(R.id.endtime);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            curposition=getAdapterPosition();
            switch (v.getId()){

                case R.id.deleteBtn:
                    orderitemList.remove(curposition);
                    notifyItemRemoved(curposition  );
                    break;

            }
        }
    }

}
