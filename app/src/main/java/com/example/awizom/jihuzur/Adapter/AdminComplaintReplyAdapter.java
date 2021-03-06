package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Complaint;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class AdminComplaintReplyAdapter extends  RecyclerView.Adapter<AdminComplaintReplyAdapter.MyViewHolder> {

    AutoCompleteTextView editComplaintreply;
    String result = "", active, status;
    private List<Complaint> complaintList;
    private Context mCtx;

    public AdminComplaintReplyAdapter(Context baseContext, List<Complaint> complaintlist) {
        this.complaintList = complaintlist;
        this.mCtx = baseContext;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Complaint c = complaintList.get(position);
        holder.complaintid.setText(Integer.toString(c.getComplaintID()));
        holder.customername.setText(String.valueOf(c.getName()));
        holder.ComplaintDate.setText(String.valueOf(c.getComplaintDate().split("T")[0]));
        holder.Complaint.setText(String.valueOf(c.getComplaint()));
        holder.customerID.setText(String.valueOf(c.getCustomerID()));
        if (c.isActive() == true) {
            holder.activeToggle.setChecked(true);
        }
        else {
            holder.activeToggle.setChecked(false);
        }
//        holder.Status.setText(c.getStatus());
        holder.Status.setVisibility(View.GONE);
        holder.activeToggle.setVisibility(View.GONE);
        holder.infoimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(mCtx, "For Complaint's Reply long press in layout", Toast.LENGTH_LONG);
                View view = toast.getView();
                //To change the Background of Toast
                view.setBackgroundColor(Color.BLACK);
                TextView text = (TextView) view.findViewById(android.R.id.message);
                //Shadow of the Of the Text Color
                text.setTextSize(17);
                text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                text.setTextColor(Color.WHITE);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            }
        });
        final String complaintId = holder.complaintid.getText().toString();
        final String status = holder.Status.getText().toString();
        final String customerId = holder.customerID.getText().toString();
        final String complaint = holder.Complaint.getText().toString();
        final String active;
        if (holder.activeToggle.isChecked()) {
            active = "true";
        } else {
            active = "true";
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showaddReplyDialog(complaintId, customerId, complaint);
                return true;
            }
        });
    }

    public void showaddReplyDialog(final String complaintId, final String customerId, final String complaint) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.add_complaintreply, null);
        dialogBuilder.setView(dialogView);
        editComplaintreply = (AutoCompleteTextView) dialogView.findViewById(R.id.editComplaintreply);
        AutoCompleteTextView editcomplainId = (AutoCompleteTextView) dialogView.findViewById(R.id.editcomplaintID);
        editcomplainId.setVisibility(View.GONE);
        final CheckBox resolve = (CheckBox) dialogView.findViewById(R.id.resolved);
        CheckBox hide = (CheckBox) dialogView.findViewById(R.id.hide);
        resolve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    status = "true";
                }
                if (resolve.isChecked() == false) {
                    status = "false";
                }
            }
        });

        hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    active = "false";
                }
                if (resolve.isChecked() == false) {
                    active = "true";
                }
            }
        });

        final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Complaint's Reply");
        dialogBuilder.setIcon(R.drawable.replycompllaint);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String complaintreply = editComplaintreply.getText().toString().trim();
                try {
                    result = new AdminHelper.POSTComplaintReply().execute(complaintreply, complaintId).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
                } catch (Exception e) {
                }
                try {
                    result = new AdminHelper.POSTComplaint().execute(customerId, complaint, active, status, complaintId).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
                } catch (Exception e) {

                }
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
                .inflate(R.layout.adapter_admincomplaintreply, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView complaintid, customerID, customername;
        public TextView Complaint, ComplaintDate;
        public ToggleButton activeToggle;
        public ImageView infoimg;
        public TextView Status;

        public MyViewHolder(View view) {
            super(view);
            complaintid = (TextView) view.findViewById(R.id.complaintID);
            infoimg = (ImageView) view.findViewById(R.id.info);
            customername = (TextView) view.findViewById(R.id.cname);
            ComplaintDate = (TextView) view.findViewById(R.id.cdate);
            Complaint = (TextView) view.findViewById(R.id.complaint);
            activeToggle = (ToggleButton) view.findViewById(R.id.activeToggle);
            Status = (TextView) view.findViewById(R.id.status);
            customerID = (TextView) view.findViewById(R.id.customerid);
            }
    }
}