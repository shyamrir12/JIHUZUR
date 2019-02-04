package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

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
    private String orderId = "", otpCode = "", result = "";
    int curposition;


    public CustomerHistoryAdapter(Context context, List<Order> orderList) {
        this.mCtx = context;
        this.orderitemList = orderList;
    }

    @NonNull
    @Override
    public CustomerHistoryAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_history_adapter, null);
        return new CustomerHistoryAdapter.OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHistoryAdapter.OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);

        try {
            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());

            holder.empName.setText(order.getEmpName());
            holder.empContAct.setText(order.getEmpMob());
            holder.timercount.setText(order.getTotalTime());
            holder.startTime.setText(order.getOrderStartTime());
            holder.endtime.setText(order.getOrderEndTime());
            holder.catagryName.setText(order.getCategory());
            holder.servicName.setText(order.getServiceName());
            holder.prcingtrm.setText(order.getPricingTerms());

            if(!order.getPricingTerms().equals(null)){
                holder.prcingtrm.setVisibility(View.VISIBLE);
            }


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
        private TextView startTime, endtime, empName, timercount, empContAct,catagryName,servicName,prcingtrm;
        private Button deleteBtn;
        private List<Order> orderitemList;

        public OrderItemViewHolder(View view, Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

            empName = itemView.findViewById(R.id.cusName);
            startTime = itemView.findViewById(R.id.starttime);
            endtime = itemView.findViewById(R.id.endtime);
            timercount = itemView.findViewById(R.id.timeCount);
            empContAct = itemView.findViewById(R.id.empMobile);
            catagryName = itemView.findViewById(R.id.catagoryName);
            servicName = itemView.findViewById(R.id.serviceName);
            prcingtrm = itemView.findViewById(R.id.pricingterm);

            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            curposition = getAdapterPosition();
            switch (v.getId()) {

                case R.id.deleteBtn:
                    orderitemList.remove(curposition);
                    notifyItemRemoved(curposition);
                    break;

            }
        }
    }

}
