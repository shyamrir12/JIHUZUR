package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminActivity.AdminPricingActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.SelectServices;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ServiceListAdapter extends
        RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    private List<Service> serviceList;
    private Context mCtx;
    String uri;
    Intent intent;
    int catalogID;
    String catalogName;
    AutoCompleteTextView editServicename, editDescription;
    Spinner displayType;
    String result = "";


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceName, description, serviceID, dType, catalogID;


        public MyViewHolder(View view) {
            super(view);

            serviceName = view.findViewById(R.id.serviceName);
            description = view.findViewById(R.id.description);
            serviceID = view.findViewById(R.id.serviceID);
            dType = view.findViewById(R.id.displayTypes);
            catalogID = view.findViewById(R.id.catalogID);


        }
    }

    public ServiceListAdapter(Context baseContext, List<Service> serviceList) {
        this.serviceList = serviceList;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Service c = serviceList.get(position);
        holder.serviceName.setText(c.getServiceName());
        holder.description.setText(c.getDescription());
        holder.serviceID.setText(String.valueOf(c.getServiceID()));
        holder.dType.setText(c.getDisplayType());
        holder.catalogID.setText(String.valueOf(c.getCatalogID()));

        final String servicename = holder.serviceName.getText().toString();
        final String description = holder.description.getText().toString();
        final String displaytype = String.valueOf(holder.dType.getText());
        final String serviceID = String.valueOf(holder.serviceID.getText());
        final String catalogiD = String.valueOf(holder.serviceID.getText());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showEditServiceDialogue(servicename, description, serviceID, catalogiD, displaytype);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Admin")) {

                    intent = new Intent(mCtx, AdminPricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.putExtra("displayType", holder.dType.getText());
                    mCtx.startActivity(intent);

                    Toast.makeText(mCtx, "" + position, Toast.LENGTH_SHORT).show();

                } else if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Employee")) {

                    intent = new Intent(mCtx, CustomerpricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.putExtra("DisplayType", holder.dType.getText());
                    intent.putExtra("button","serBtn");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);


                } else if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Customer")) {

                    intent = new Intent(mCtx, CustomerpricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.putExtra("DisplayType", holder.dType.getText());
                    intent.putExtra("button","serBtn");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);
                }


            }
        });


    }

    private void showEditServiceDialogue(final String servicename, final String description, final String serviceid, final String catalogId, String displaytype) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.add_servicelayout, null);
        dialogBuilder.setView(dialogView);


        editServicename = (AutoCompleteTextView) dialogView.findViewById(R.id.editServiceName);
        editServicename.setText(servicename);

        editDescription = (AutoCompleteTextView) dialogView.findViewById(R.id.description);
        editDescription.setText(description);
        displayType = (Spinner) dialogView.findViewById(R.id.displayType);
        if (displaytype.equals("Radio")) {
            List<String> list = new ArrayList<String>();
            list.add("Radio");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, list);

            displayType.setAdapter(dataAdapter);

        } else if (displaytype.equals("Checkbox")) {
            List<String> list = new ArrayList<String>();
            list.add("Checkbox");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, list);

            displayType.setAdapter(dataAdapter);
        } else {
            List<String> list = new ArrayList<String>();

            ;
            list.add("Range");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, list);

            displayType.setAdapter(dataAdapter);
        }
        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddService);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Edit Service");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String service = editServicename.getText().toString().trim();
                String descriptions = editDescription.getText().toString().trim();
                String displaytype = displayType.getSelectedItem().toString();
                if (displaytype == "Range") {
                    displaytype = "";

                }

                try {

                    result = new AdminHelper.POSTService().execute(serviceid, catalogId, service, descriptions, displaytype).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                    ((SelectServices)mCtx).getServiceList();

                } catch (Exception e) {
                    e.printStackTrace();

                    Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                    // System.out.println("Error: " + e);
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
        return serviceList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_servicelist, parent, false);
        return new MyViewHolder(v);
    }
}