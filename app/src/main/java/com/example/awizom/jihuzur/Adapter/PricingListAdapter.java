package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;

import java.util.List;


public class PricingListAdapter extends
        RecyclerView.Adapter<PricingListAdapter.MyViewHolder> implements View.OnTouchListener {

    private List<PricingView> pricingList;
    private Context mCtx;
    private int position;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    /**
     * View holder class
     * */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pricingterms;
        public TextView amount;
        public TextView description;

        public SeekBar seekBar;



        public MyViewHolder(View view) {
            super(view);
            pricingterms = (TextView) view.findViewById(R.id.pricingTerms);
            amount = (TextView) view.findViewById(R.id.amount);
            description = (TextView) view.findViewById(R.id.description);

        }
    }

    public PricingListAdapter(Context baseContext, List<PricingView> pricingList) {
        this.pricingList = pricingList;
        this.mCtx=baseContext;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PricingView c = pricingList.get(position);
        holder.pricingterms.setText((c.getPricingTerms()));
        holder.amount.setText(String.valueOf("Rs " + c.getAmount()));
        holder.description.setText(c.getPrizingDesc());




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