package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.R;

import java.util.List;


public class PricingListAdapter extends
        RecyclerView.Adapter<PricingListAdapter.MyViewHolder> {

    private List<Pricing> pricingList;
    private Context mCtx;



    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pricingId;
        public TextView description;
        public TextView pricingTerms;
        public TextView amount;
        public TextView catalogId;

        public MyViewHolder(View view) {
            super(view);
            pricingId = (TextView) view.findViewById(R.id.pricingid);
            description = (TextView) view.findViewById(R.id.description);
            pricingTerms = (TextView) view.findViewById(R.id.pricingTerms);
            amount = (TextView) view.findViewById(R.id.amount);
            catalogId = (TextView) view.findViewById(R.id.catalogID);

        }
    }

    public PricingListAdapter(Context baseContext, List<Pricing> pricingList) {
        this.pricingList = pricingList;
        this.mCtx=baseContext;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Pricing c = pricingList.get(position);
        holder.pricingId.setText(Integer.toString(c.getPricingID()));
        holder.description.setText(String.valueOf(c.getDescription()));
        holder.pricingTerms.setText(c.getPricingTerms());
        holder.amount.setText(String.valueOf(c.getAmount()));
        holder.catalogId.setText(Integer.toString(c.getCatalogID()));

    }

    @Override
    public int getItemCount() {
        return pricingList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pricinglist,parent, false);
        return new MyViewHolder(v);
    }
}