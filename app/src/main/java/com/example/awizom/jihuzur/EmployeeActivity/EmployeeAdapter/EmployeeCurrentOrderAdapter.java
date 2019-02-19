package com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Helper.ServicesHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeCurrentOrderAdapter extends RecyclerView.Adapter<EmployeeCurrentOrderAdapter.OrderItemViewHolder> {


    List<Service> serviceList;
    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId = "", otpCode = "", result = "", empId = "", displayType = "",priceid="";
    private Intent intent;

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
    public void onBindViewHolder(@NonNull final EmployeeCurrentOrderAdapter.OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);
        orderId = String.valueOf(order.getOrderID());
        priceid =String.valueOf(order.getPricingID());
        empId = order.getEmployeeID();
        try {

            order = orderitemList.get(position);
            holder.customerName.setText(order.getName());
            holder.startTime.setText(order.getOrderStartTime());
            holder.endtime.setText(order.getOrderEndTime());
            holder.customerContact.setText(order.getMobileNo());
            holder.catagoryName.setText(order.getCatalogName());
            holder.serviceName.setText(order.getServiceName());
            holder.totalTime.setText(order.getTotalTime());
            if(order.getDiscountName() != null) {
                holder.disctName.setText(order.getDiscountName());
            }else {
                holder.disctName.setText(null);
            }
           // holder.catlgName.setText(order.getCatalogName());

            holder.catlgId.setText(String.valueOf(order.getCatalogID()));
            holder.serviceID.setText(String.valueOf(order.getServiceID()));
           holder.pricingterms.setText(order.getPricingTerms());


            if (order.getPricingTerms() != null) {
                holder.pricingterm.setVisibility(View.VISIBLE);
                holder.pricingterm.setVisibility(View.VISIBLE);
                holder.priceUpdateBtn.setVisibility(View.VISIBLE);
            }

            if (order.getDiscountName() != null) {
              // holder.linearLayout.setVisibility(View.VISIBLE);
               holder.disctName.setVisibility(View.VISIBLE);
                holder.discountUpdateBtn.setVisibility(View.VISIBLE);
            }
         //   holder.linearLayout.setVisibility(View.VISIBLE);
            holder.disctName.setVisibility(View.VISIBLE);
            holder.discountUpdateBtn.setVisibility(View.VISIBLE);
//            holder.discountUpdateBtn.setVisibility(View.VISIBLE);
//            holder.priceUpdateBtn.setVisibility(View.VISIBLE);


            if (order.getOrderStartTime() != null) {
                holder.genrateBtn.setVisibility(View.GONE);

            } else {
                holder.stopBtn.setVisibility(View.VISIBLE);
                holder.genrateBtn.setVisibility(View.VISIBLE);
//                holder.stopBtn.setVisibility(View.GONE);
            }


            getServiceList(holder.catlgId.getText().toString());


            holder.discountUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(v.getRootView().getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getRootView().getContext());
                    final View dialogView = inflater.inflate(R.layout.discount_layout_alert, null);
                    dialogBuilder.setView(dialogView);

                    final EditText dicountText = dialogView.findViewById(R.id.distName);
                    Button submitButn = dialogView.findViewById(R.id.submitBtn);
                    dicountText.setText(holder.disctName.getText().toString());
                    dialogBuilder.setTitle("Edit Discount");
                    final android.support.v7.app.AlertDialog b = dialogBuilder.create();
                    b.show();
                    if (dicountText.getText().toString().isEmpty()) {

                        dicountText.setError("Enter a valid value");
                        dicountText.requestFocus();

                    }
                    submitButn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {

                                result = new AdminHelper.EditPostDiscount().execute(orderId,dicountText.getText().toString()).get();
                                Gson gson = new Gson();
                                final Result jsonbodyres = gson.fromJson(result, Result.class);
                                Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    });
                }
            });


            holder.priceUpdateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(mCtx, CustomerpricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", order.getServiceDesc());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.putExtra("DisplayType", displayType.toString());
                    intent.putExtra("button","empBtn");
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("priceId",priceid);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);
                }
            });



        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    private void getServiceList(String s) {


        try {

            result = new ServicesHelper.GETServiceList().execute(s).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Service>>() {
            }.getType();
            serviceList = new Gson().fromJson(result, listType);

            for (int i = 0; i < serviceList.size(); i++) {
                displayType = serviceList.get(i).getDisplayType();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orderitemList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        private TextView startTime, endtime, customerName, customerContact, catagoryName, serviceName, totalTime, pricingterm,
                disctName, catlgId, catagryName, catlgName, pricingterms, serviceID;
        private Button genrateBtn, trackinBtn, stopBtn, acceptPaymentBtn,priceUpdateBtn,discountUpdateBtn;
        private LinearLayout linearLayout;
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
            serviceName = itemView.findViewById(R.id.serviceName);
            totalTime = itemView.findViewById(R.id.timeCount);
            pricingterm = itemView.findViewById(R.id.pricingterm);
            genrateBtn = itemView.findViewById(R.id.genOtpBtn);
            trackinBtn = itemView.findViewById(R.id.trackBtn);
            stopBtn = itemView.findViewById(R.id.stopBtn);
            acceptPaymentBtn = itemView.findViewById(R.id.acceptPaymentBtn);


            catagryName = itemView.findViewById(R.id.catagoryName);
            pricingterms = itemView.findViewById(R.id.pricingterm);
            serviceID = itemView.findViewById(R.id.serviceID);
           // catlgName = itemView.findViewById(R.id.catalogName);
            catlgId = itemView.findViewById(R.id.catalogID);
            disctName = itemView.findViewById(R.id.discountName);
            linearLayout = itemView.findViewById(R.id.l5);

            genrateBtn.setOnClickListener(this);
            trackinBtn.setOnClickListener(this);
            stopBtn.setOnClickListener(this);
            acceptPaymentBtn.setOnClickListener(this);


            priceUpdateBtn = itemView.findViewById(R.id.priceupdateBtn);
            priceUpdateBtn.setOnClickListener(this);
            discountUpdateBtn = itemView.findViewById(R.id.dicupdateBtn);
            discountUpdateBtn.setOnClickListener(this);
        }


        @Override
        public void onClick(final View v) {

            switch (v.getId()) {
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
                        result = new EmployeeOrderHelper.AcceptPayment().execute(orderId, empId).get();
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
