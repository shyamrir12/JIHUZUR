package com.example.awizom.jihuzur.Adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerHomePage;
import com.example.awizom.jihuzur.Helper.CustomerGetMyOrderRunningHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomerCurrentOrderAdapter extends RecyclerView.Adapter<CustomerCurrentOrderAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId="",otpCode="",result="";

    public CustomerCurrentOrderAdapter(Context currentOrderActivity, List<Order> orderList) {
        this.mCtx = currentOrderActivity;
        this.orderitemList = orderList;

    }


    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.display_customer_current_order_layout, null);
        return new OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        try {

            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());

            holder.headerName.setText(String.valueOf(order.getOrderID()));
            holder.startTime.setText( order.getEmployeeID().toString());
            holder.endtime.setText( order.getCustomerID().toString());

            if(order.getOrderStartTime().equals("NULL")){
                holder.canclBtn.setVisibility(View.VISIBLE);
            }else {
                holder.canclBtn.setVisibility(View.GONE);
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

            switch (v.getId()){
                case R.id.acceptOtpBtn:

                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    final View dialogView = inflater.inflate(R.layout.accept_otp_for_order_layout, null);
                    dialogBuilder.setView(dialogView);

                    final EditText enterOtp = dialogView.findViewById(R.id.editTextOtp);
                    Button verify = dialogView.findViewById(R.id.buttonVerify);

                    dialogBuilder.setTitle("Accept Otp");
                    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
                    b.show();

                    verify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                result = new CustomerGetMyOrderRunningHelper.AcceptOtp().execute(orderId,enterOtp.getText().toString()).get();
                                Gson gson = new Gson();
                                Type getType = new TypeToken<ResultModel>() {
                                }.getType();
                                ResultModel resultModel = new Gson().fromJson(result, getType);
                                if(resultModel.getMessage().contains("Order Started")){
                                    canclBtn.setVisibility(View.GONE);
                                }

                                Toast.makeText(mCtx, result.toString(), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mCtx,CustomerHomePage.class);
                                mCtx.startActivity(intent);

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });



                    break;
                case R.id.trackBtn:


                    break;
                case R.id.cancelBtn:

                    try {
                        result = new CustomerGetMyOrderRunningHelper.CancelOrder().execute(orderId).get();
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
