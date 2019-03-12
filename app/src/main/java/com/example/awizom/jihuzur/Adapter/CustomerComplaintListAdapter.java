package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Complaint;
import com.example.awizom.jihuzur.Model.ComplaintCustomer;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
public class CustomerComplaintListAdapter extends RecyclerView.Adapter<CustomerComplaintListAdapter.MyViewHolder>
{
    TextView editComplaintreply;
    String result = "", active, status;
    private List<Complaint> complaintList;
    private List<ComplaintCustomer> complaintcustomer;
    private Context mCtx;
    String loyal;

    public CustomerComplaintListAdapter(Context baseContext, List<Complaint> complaintlist) {
        this.complaintList = complaintlist;
        this.mCtx = baseContext;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Complaint c = complaintList.get(position);
        if (c.isActive()) {
            loyal = "char";
        } else {
            loyal = null;
        }
        if (loyal != null) {
            holder.constra.setVisibility(View.VISIBLE);
            holder.complaintid.setText(Integer.toString(c.getComplaintID()));
            holder.Complaint.setText(String.valueOf(c.getComplaint()));
            holder.customerID.setText(String.valueOf(c.getCustomerID()));
            holder.complaintDate.setText(String.valueOf(c.getComplaintDate()).split("T")[0]);

            final String complaintId = holder.complaintid.getText().toString();
            final String status = holder.Status.getText().toString();
            final String customerId = holder.customerID.getText().toString();
            final String complaint = holder.Complaint.getText().toString();
            final String active;
            holder.viewComplaintReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getcomplaintreply(complaintId);
                    showReplyDialog();
                }
            });
        }

    }

    private void getcomplaintreply(String complaintId) {
        try {
            result = new CustomerOrderHelper.GETCustomerComplaintReply().execute(complaintId).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<ComplaintCustomer>>() {
            }.getType();
            complaintcustomer = new Gson().fromJson(result, listType);
            //            adminComplaintReplyAdapter = new AdminComplaintReplyAdapter(AdminComplaintReply.this, complaintlist);
//            recyclerView.setAdapter(adminComplaintReplyAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showReplyDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.customer_viewreplyadapter, null);
        dialogBuilder.setView(dialogView);
        final LinearLayout rl = (LinearLayout) dialogView.findViewById(R.id.ll1);
        final TextView[] tv = new TextView[10];
        if (complaintcustomer.size() > 0) {
            for (int i = 0; i < complaintcustomer.size(); i++) {
                tv[i] = new TextView(mCtx);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                        ((int) LinearLayout.LayoutParams.WRAP_CONTENT, (int) LinearLayout.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 50;
                params.topMargin = i * 50;
                tv[i].setText(complaintcustomer.get(i).getCReply());
                tv[i].setTextSize((float) 20);
                tv[i].setPadding(20, 50, 20, 50);
                tv[i].setLayoutParams(params);
                rl.addView(tv[i]);
            }
        }

        final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);
        buttonAddCategory.setVisibility(View.GONE);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Reply By Admin for Your Complaint");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_customercomplaintlist, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView complaintid, customerID, complaintDate;
        public TextView Complaint;
        public ToggleButton activeToggle;
        public TextView Status;
        public ImageButton viewComplaintReply;
        public ConstraintLayout constra;

        public MyViewHolder(View view) {
            super(view);
            complaintid = (TextView) view.findViewById(R.id.complaintID);
            Complaint = (TextView) view.findViewById(R.id.complaint);
            activeToggle = (ToggleButton) view.findViewById(R.id.activeToggle);
            Status = (TextView) view.findViewById(R.id.status);
            customerID = (TextView) view.findViewById(R.id.customerid);
            viewComplaintReply = (ImageButton) view.findViewById(R.id.viewComplaintReply);
            complaintDate = (TextView) view.findViewById(R.id.complaintDate);
            constra = view.findViewById(R.id.constra);
            constra.setVisibility(View.GONE);

        }
    }
}