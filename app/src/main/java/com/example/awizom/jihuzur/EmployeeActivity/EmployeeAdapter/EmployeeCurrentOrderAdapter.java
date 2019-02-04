package com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeCurrentOrderAdapter extends RecyclerView.Adapter<EmployeeCurrentOrderAdapter.OrderItemViewHolder>{


    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId="",otpCode="",result="",empId="";

    public EmployeeCurrentOrderAdapter(Context employeeCurrentOrderFragment, List<Order> orderList) {

        this.mCtx = employeeCurrentOrderFragment;
        this.orderitemList = orderList;

    }
    @NonNull
    @Override
    public EmployeeCurrentOrderAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_employee_current_order_item, null);
        return new EmployeeCurrentOrderAdapter.OrderItemViewHolder(view, mCtx, orderitemList);
    }


    @Override
    public void onBindViewHolder(@NonNull EmployeeCurrentOrderAdapter.OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        orderId = String.valueOf(order.getOrderID());
        empId = order.getEmployeeID();
        try {

            order = orderitemList.get(position);
            holder.customerName.setText(order.getName());
            holder.startTime.setText( order.getOrderStartTime());
            holder.endtime.setText( order.getOrderEndTime());
            holder.customerContact.setText(order.getMobileNo());
            holder.catagoryName.setText(order.getCatalogName());
            holder.serviceName.setText( order.getServiceName());
            holder.totalTime.setText( order.getTotalTime());
            if(!order.getPricingTerms().equals(null)){
                holder.pricingterm.setVisibility(View.VISIBLE);
            }
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
        private TextView startTime,endtime,customerName,customerContact,catagoryName,serviceName,totalTime,pricingterm;
        private Button genrateBtn,trackinBtn,stopBtn,acceptPaymentBtn;
        private List<Order> orderitemList;


        public OrderItemViewHolder(View view, Context mCtx, List<Order> orderitemList) {
            super(view);
            this.mCtx = mCtx;
            this.orderitemList = orderitemList;
            itemView.setOnClickListener(this);

            customerName = itemView.findViewById(R.id.cusName);
            startTime = itemView.findViewById(R.id.starttime);
            endtime = itemView.findViewById(R.id.endtime);
            customerContact = itemView.findViewById(R.id.cusMobile);
            catagoryName = itemView.findViewById(R.id.catagoryName);
            serviceName= itemView.findViewById(R.id.serviceName);
            totalTime = itemView.findViewById(R.id.timeCount);
            pricingterm = itemView.findViewById(R.id.pricingterm);
            genrateBtn = itemView.findViewById(R.id.genOtpBtn);
            trackinBtn = itemView.findViewById(R.id.trackBtn);
            stopBtn = itemView.findViewById(R.id.stopBtn);
            acceptPaymentBtn = itemView.findViewById(R.id.acceptPaymentBtn);

            genrateBtn.setOnClickListener(this);
            trackinBtn.setOnClickListener(this);
            stopBtn.setOnClickListener(this);
            acceptPaymentBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {

            switch (v.getId()){
                case R.id.genOtpBtn:

                    try {
                        result = new EmployeeOrderHelper.GenerateOtp().execute(orderId).get();
                        Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.trackBtn:


                    break;
                case R.id.stopBtn:

                    try {
                        result = new EmployeeOrderHelper.StopOrder().execute(orderId).get();
                        Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.acceptPaymentBtn:

                    try {
                        result = new EmployeeOrderHelper.AcceptPayment().execute(orderId,empId).get();
                        Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }




    }


}
