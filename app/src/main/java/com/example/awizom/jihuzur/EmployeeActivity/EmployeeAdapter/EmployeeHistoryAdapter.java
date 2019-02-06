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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.ServicesHelper;
import com.example.awizom.jihuzur.Model.Order;
import com.example.awizom.jihuzur.Model.ResultModel;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EmployeeHistoryAdapter extends RecyclerView.Adapter<EmployeeHistoryAdapter.OrderItemViewHolder> {
    int curposition;
    List<Service> serviceList;
    private Context mCtx;
    private List<Order> orderitemList;
    private Order order;
    private String orderId = "", otpCode = "", result = "", displayType = "";
    private Intent intent;


    public EmployeeHistoryAdapter(Context context, List<Order> orderList) {
        this.mCtx = context;
        this.orderitemList = orderList;
    }

    @NonNull
    @Override
    public EmployeeHistoryAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.employee_history_adapter, null);
        return new EmployeeHistoryAdapter.OrderItemViewHolder(view, mCtx, orderitemList);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeHistoryAdapter.OrderItemViewHolder holder, int position) {
        order = orderitemList.get(position);

        try {
            order = orderitemList.get(position);
            orderId = String.valueOf(order.getOrderID());

            holder.empName.setText(order.getEmpName());
            holder.empContAct.setText(order.getEmpMob());
            holder.timercount.setText(order.getTotalTime());

            holder.startTime.setText(order.getOrderStartTime());
            holder.endtime.setText(order.getOrderEndTime());
            holder.disctName.setText(order.getDiscountName());

            holder.catlgName.setText(order.getCatalogName());
            holder.catagryName.setText(order.getCategory());
            holder.catlgId.setText(String.valueOf(order.getCatalogID()));

            holder.servicName.setText(order.getServiceName());
            holder.pricingterms.setText(order.getPricingTerms());
            holder.serviceID.setText(String.valueOf(order.getServiceID()));


            if (order.getPricingTerms().equals("NULL")) {
                holder.pricingterms.setVisibility(View.GONE);

            }
            if (!order.getDiscountName().equals(null)) {
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.disctName.setVisibility(View.VISIBLE);
            }
            getServiceList(holder.catlgId.getText().toString());




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
        private TextView startTime, endtime, empName, timercount, empContAct, disctName,
                catlgId, catagryName, catlgName, servicName, pricingterms, serviceID;
        private LinearLayout linearLayout;
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
            pricingterms = itemView.findViewById(R.id.pricingterm);
            serviceID = itemView.findViewById(R.id.serviceID);
            catlgName = itemView.findViewById(R.id.catalogName);
            catlgId = itemView.findViewById(R.id.catalogID);
            disctName = itemView.findViewById(R.id.discountName);
            linearLayout = itemView.findViewById(R.id.l5);

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

