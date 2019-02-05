package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class CustomerPricingAdapter extends RecyclerView.Adapter<CustomerPricingAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<PricingView> pricingViewList;
    private String serviceID = "", result = "",displytype="";



    public CustomerPricingAdapter(CustomerpricingActivity customerpricingActivity, List<PricingView> pricingViewsList, String displayType) {
        this.mCtx = customerpricingActivity;
        this.pricingViewList = pricingViewsList;
        this.displytype = displayType;
    }

    @NonNull
    @Override
    public CustomerPricingAdapter.OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.repair_layout, null);
        return new CustomerPricingAdapter.OrderItemViewHolder(view, mCtx, pricingViewList);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerPricingAdapter.OrderItemViewHolder holder, int position) {

        try {
            PricingView pricingView = pricingViewList.get(position);
          /*  holder.radioText.setText(String.valueOf(pricingView.getServiceName()));*/
            holder.catagoryName.setText(pricingView.getCatalogName());
            holder.desText.setText(String.valueOf(pricingView.getPrizingDesc()));
            holder.priceText.setText(String.valueOf(pricingView.getAmount()));
            holder.serviceDescText.setText(pricingView.getPricingTerms());


            if (displytype.equals("Radio")) {
                holder.checkBox.setVisibility(View.GONE);
            } else if (displytype.equals("Checkbox")) {
                holder.radioButton.setVisibility(View.GONE);
            }else if (displytype.equals(null)) {
                holder.radioButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
            }


        } catch (Exception E) {
            E.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pricingViewList.size();
    }

    class OrderItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Context mCtx;
        private List<PricingView> pricingViewList;
        private RadioButton radioButton;
        private CheckBox checkBox;
        private TextView  priceText, desText, serviceDescText, catagoryName;


        public OrderItemViewHolder(View view, Context mCtx, List<PricingView> pricingViewList) {
            super(view);
            this.mCtx = mCtx;
            this.pricingViewList = pricingViewList;
            itemView.setOnClickListener(this);


            radioButton = view.findViewById(R.id.radioSelectRepair);
            checkBox = view.findViewById(R.id.checkSelectRepairs);
            priceText = view.findViewById(R.id.price);
            desText = view.findViewById(R.id.description);
            catagoryName = view.findViewById(R.id.catagoryName);
            serviceDescText = view.findViewById(R.id.ServiceDesc);

        }

        @Override
        public void onClick(final View v) {

            switch (v.getId()) {

            }
        }

    }

}
