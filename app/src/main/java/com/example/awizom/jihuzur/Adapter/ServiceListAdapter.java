package com.example.awizom.jihuzur.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminPricingActivity;
import com.example.awizom.jihuzur.CustomerActivity.SingleShotLocationProvider;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeSkillActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.SelectServices;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    String uri;
    Intent intent;
    int catalogID;
    String catalogName;
    AutoCompleteTextView editServicename, editDescription;
    Spinner displayType;
    String result = "", empskills = "", imageLink = "";
    private List<Service> serviceList;
    private Context mCtx;
    ViewDialog viewDialog;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    public ServiceListAdapter(Context baseContext, List<Service> serviceList, String empskill, String imageLink) {
        this.serviceList = serviceList;
        this.mCtx = baseContext;
        this.empskills = empskill;
        this.imageLink = imageLink;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        viewDialog = new ViewDialog((Activity) mCtx);
        Service c = serviceList.get(position);
        holder.serviceName.setText(c.getServiceName());
        holder.description.setText(c.getDescription());
        holder.serviceID.setText(String.valueOf(c.getServiceID()));
        holder.dType.setText(c.getDisplayType());
        holder.catalogID.setText(String.valueOf(c.getCatalogID()));

        if (imageLink != null) {
            Glide.with(mCtx)
                    .load(imageLink)
                    .placeholder(R.mipmap.temp).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imageLink);
        }

        final String servicename = holder.serviceName.getText().toString();
        final String description = holder.description.getText().toString();
        final String displaytype = String.valueOf(holder.dType.getText());
        final String serviceID = String.valueOf(holder.serviceID.getText());
        final String catalogiD = String.valueOf(holder.serviceID.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);

                try {
                    if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Admin")) {
                        showCustomLoadingDialog(v);
                        intent = new Intent(mCtx, AdminPricingActivity.class);
                        intent.putExtra("serviceName", holder.serviceName.getText());
                        intent.putExtra("description", holder.description.getText());
                        intent.putExtra("serviceID", holder.serviceID.getText());
                        intent.putExtra("displayType", holder.dType.getText());
                        Intent i = new Intent(mCtx, SingleShotLocationProvider.class);
                        mCtx.startService(i);
                        mCtx.startActivity(intent);
                        /*    Toast.makeText(mCtx, "" + position, Toast.LENGTH_SHORT).show();
                         */
                    } else if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Employee")) {
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String employeeid = SharedPrefManager.getInstance(mCtx).getUser().getID();
                                showCustomLoadingDialog(v);
                                try {
                                    result = new EmployeeOrderHelper.EmployeePOSTSkill().execute(employeeid, serviceID).get();
                                    Gson gson = new Gson();
                                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

                                    if (!result.equals(null)) {
                                        intent = new Intent(mCtx, EmployeeSkillActivity.class);
                                        mCtx.startActivity(intent);
                                    }

                                } catch (Exception e) {
                                }
                            }
                        });
                    } else if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Customer")) {
                        showCustomLoadingDialog(v);
                        intent = new Intent(mCtx, CustomerpricingActivity.class);
                        intent.putExtra("serviceName", holder.serviceName.getText());
                        intent.putExtra("description", holder.description.getText());
                        intent.putExtra("serviceID", holder.serviceID.getText());
                        intent.putExtra("DisplayType", holder.dType.getText());
                        intent.putExtra("button", "serBtn");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mCtx.startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            if (SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Admin")) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showEditServiceDialogue(servicename, description, serviceID, catalogiD, displaytype);
                        return true;
                    }
                });
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showCustomLoadingDialog(View view) {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
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
            list.add("Range");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mCtx, android.R.layout.simple_spinner_item, list);
            displayType.setAdapter(dataAdapter);
        }
        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddService);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Edit Service");
        dialogBuilder.setIcon(R.drawable.icon_services);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
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
                        ((SelectServices) mCtx).getServiceList();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mCtx, "Error: " + e, Toast.LENGTH_SHORT).show();
                        // System.out.println("Error: " + e);
                    }
                    b.dismiss();
                }
            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
    }

    private boolean validation() {

        if (editServicename.getText().toString().isEmpty()) {
            editServicename.setError("Enter valid Service Name");
            editServicename.requestFocus();
            return false;
        }
        if (editDescription.getText().toString().isEmpty()) {
            editDescription.setError("Enter valid Description");
            editDescription.requestFocus();
            return false;
        }
        return true;
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

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceName, description, serviceID, dType, catalogID;
        public ImageView imageLink;

        public MyViewHolder(View view) {
            super(view);

            serviceName = view.findViewById(R.id.serviceName);
            description = view.findViewById(R.id.description);
            serviceID = view.findViewById(R.id.serviceID);
            dType = view.findViewById(R.id.displayTypes);
            catalogID = view.findViewById(R.id.catalogID);
            imageLink = view.findViewById(R.id.image);


        }
    }
}