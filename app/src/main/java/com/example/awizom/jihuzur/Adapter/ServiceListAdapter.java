package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminPricingActivity;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.CustomerpricingActivity;
import com.example.awizom.jihuzur.Util.SharedPrefManager;

import java.util.List;


public class ServiceListAdapter extends
        RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    private List<Service> serviceList;
    private Context mCtx;
    String uri;
    Intent intent;
    int catalogID;
    String catalogName;





    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceName,description,serviceID;


        public MyViewHolder(View view) {
            super(view);

            serviceName = (TextView) view.findViewById(R.id.serviceName);
            description=(TextView)view.findViewById(R.id.description);
            serviceID=(TextView)view.findViewById(R.id.serviceID);



        }
    }

    public ServiceListAdapter(Context baseContext, List<Service> serviceList) {
        this.serviceList = serviceList;
        this.mCtx=baseContext;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Service c = serviceList.get(position);
       holder.serviceName.setText(c.getServiceName());
       holder.description.setText(c.getDescription());
       holder.serviceID.setText(String.valueOf(c.getServiceID()));




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SharedPrefManager.getInstance(mCtx).getUser().getRole().equals("Admin")){

                    intent = new Intent(mCtx, AdminPricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    mCtx.startActivity(intent);

                    Toast.makeText(mCtx, "" + position, Toast.LENGTH_SHORT).show();

                }else if(SharedPrefManager.getInstance( mCtx).getUser().getRole().equals( "Employee" )){

                    intent = new Intent(mCtx, CustomerpricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);


                }else if(SharedPrefManager.getInstance( mCtx).getUser().getRole().equals( "Customer" )){

                    intent = new Intent(mCtx, CustomerpricingActivity.class);
                    intent.putExtra("serviceName", holder.serviceName.getText());
                    intent.putExtra("description", holder.description.getText());
                    intent.putExtra("serviceID", holder.serviceID.getText());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mCtx.startActivity(intent);
                }


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
                .inflate(R.layout.adapter_servicelist,parent, false);
        return new MyViewHolder(v);
    }
}