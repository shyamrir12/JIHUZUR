package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;

import java.util.List;


public class CatalogListAdapter extends
        RecyclerView.Adapter<CatalogListAdapter.MyViewHolder> {

    private List<Catalog> catalogList;
    private Context mCtx;


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView catalogId;
        public TextView catalogName;
        public TextView category;
        public TextView serviceName;
        public TextView description;

        public MyViewHolder(View view) {
            super(view);
            catalogId = (TextView) view.findViewById(R.id.catalogId);
            catalogName = (TextView) view.findViewById(R.id.catalogName);
            category = (TextView) view.findViewById(R.id.category);
            serviceName = (TextView) view.findViewById(R.id.servicename);
            description = (TextView) view.findViewById(R.id.description);

        }
    }

    public CatalogListAdapter(Context baseContext, List<Catalog> catalogList) {
        this.catalogList = catalogList;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Catalog c = catalogList.get(position);
        holder.catalogId.setText(Integer.toString(c.getCatalogID()));
        holder.catalogName.setText(String.valueOf(c.getCatalogName()));
        holder.category.setText(c.getCategory());
        holder.serviceName.setText(String.valueOf(c.getServiceName()));
        holder.description.setText(c.getDescription());

    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cataloglist, parent, false);
        return new MyViewHolder(v);
    }
}