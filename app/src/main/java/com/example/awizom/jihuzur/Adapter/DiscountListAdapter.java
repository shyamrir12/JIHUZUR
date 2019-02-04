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
import com.example.awizom.jihuzur.Model.Discount;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;

import java.util.List;


public class DiscountListAdapter extends
        RecyclerView.Adapter<DiscountListAdapter.MyViewHolder> implements View.OnTouchListener {

    private List<DiscountView> discountlist;
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
        public TextView discountName;
        public TextView discountType;
        public TextView discountAmount;





        public MyViewHolder(View view) {
            super(view);
            discountName = (TextView) view.findViewById(R.id.discountName);
            discountType = (TextView) view.findViewById(R.id.discounttype);
            discountAmount = (TextView) view.findViewById(R.id.discountAmount);

        }
    }

    public DiscountListAdapter(Context baseContext, List<DiscountView> discountlist) {
        this.discountlist = discountlist;
        this.mCtx=baseContext;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DiscountView c = discountlist.get(position);
        holder.discountName.setText((c.getDiscountName()));
        holder.discountAmount.setText(String.valueOf("Rs " + c.getDiscount()));
        holder.discountType.setText(c.getDiscountType());
        holder.discountName.setTextAppearance(mCtx, R.style.fontForNotificationLandingPage);




    }

    @Override
    public int getItemCount() {
        return discountlist.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_discountlist,parent, false);
        return new MyViewHolder(v);
    }
}