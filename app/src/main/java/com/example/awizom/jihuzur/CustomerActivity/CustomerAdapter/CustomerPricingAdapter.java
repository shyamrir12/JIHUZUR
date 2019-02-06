package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.Helper.DiscountHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

public class CustomerPricingAdapter extends RecyclerView.Adapter<CustomerPricingAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<PricingView> pricingViewList;
    private String serviceID = "", result = "",displytype="",orderId="",priceId="",btn="";



    public CustomerPricingAdapter(CustomerpricingActivity customerpricingActivity, List<PricingView> pricingViewsList, String displayType, String orderID, String priceID,String btn) {
        this.mCtx = customerpricingActivity;
        this.pricingViewList = pricingViewsList;
        this.displytype = displayType;
        this.orderId = orderID;
        this.priceId = priceID;
        this.btn = btn;
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
            holder.catagoryName.setText(pricingView.getServiceName());
            holder.desText.setText(String.valueOf(pricingView.getPrizingDesc()));
            holder.priceText.setText(String.valueOf(pricingView.getAmount()));
            holder.radioButton.setText(pricingView.getPricingTerms());
            holder.checkBox.setText(pricingView.getPricingTerms());
            holder.serviceDescText.setText(String.valueOf(pricingView.getServiceName()));
            holder.priceIDText.setText(String.valueOf(pricingView.getPricingID()));

//            holder.radioButton.setText(String.valueOf(pricingView.getPricingID()));
//            holder.checkBox.setText(String.valueOf(pricingView.getPricingID()));
//            holder.serviceDescText.setText(String.valueOf(pricingView.getPricingID()));

            if (displytype.equals("Radio")) {
                holder.checkBox.setVisibility(View.GONE);
                holder.serviceDescText.setVisibility(View.GONE);
            } else if (displytype.equals("Checkbox")) {
                holder.radioButton.setVisibility(View.GONE);
                holder.serviceDescText.setVisibility(View.GONE);
            }else if (displytype.equals("")) {
                holder.radioButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
                holder.serviceDescText.setVisibility(View.VISIBLE);
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
        private PricingView pricingView;
        private RadioButton radioButton;
        private CheckBox checkBox;
        private RadioGroup radioGroup;
        private String[] pricingTerms;
//        boolean isChecked = checkBox.isChecked();


        private TextView  priceText, desText, serviceDescText, catagoryName,priceIDText;


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
         priceIDText = view.findViewById(R.id.pID);
           // radioGroup = view.findViewById(R.id.radio);

            checkBox.setOnClickListener(this);
            radioButton.setOnClickListener(this);
            pricingTerms = new String[pricingViewList.size()];
            for (int i = 0; i < pricingViewList.size(); i++) {
                pricingTerms[i] = String.valueOf(pricingViewList.get(i).getPricingTerms());
            }

        }

        @Override
        public void onClick(final View v) {

             pricingView = new PricingView();
            int checkBoxId  = v.getId();
            if(v.getId() == checkBox.getId()) {
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(mCtx, "Selected CheckBox ID" + priceIDText.getText(), Toast.LENGTH_SHORT).show();
                    PricingView pView = new PricingView();
                    pView.PricingID = Integer.valueOf((String) priceIDText.getText());
                    SharedPrefManager.getInstance( mCtx ).checked( pView );

                    String s = String.valueOf(SharedPrefManager.getInstance(mCtx).getPricingID().PricingID);


                } else {
                    Toast.makeText(mCtx, "Not selected", Toast.LENGTH_SHORT).show();
                }
            }


            if(v.getId() == radioButton.getId()) {
                if (radioButton.isChecked()) {
                    Toast.makeText(mCtx, "Selected CheckBox ID" + priceIDText.getText(), Toast.LENGTH_SHORT).show();
                    PricingView pView = new PricingView();
                    pView.PricingID = Integer.valueOf((String) priceIDText.getText());
                    SharedPrefManager.getInstance( mCtx ).checked( pView );
                    String s = String.valueOf(SharedPrefManager.getInstance(mCtx).getPricingID().PricingID);

                } else {
                    Toast.makeText(mCtx, "Not Selected" + radioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }

        }


    }

}
