package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.awizom.jihuzur.CustomerActivity.CustomerpricingActivity;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import java.util.List;

public class CustomerPricingAdapter extends RecyclerView.Adapter<CustomerPricingAdapter.OrderItemViewHolder> {

    private Context mCtx;
    private List<PricingView> pricingViewList;
    private String serviceID = "", result = "", displytype = "", orderId = "", priceId = "", btn = "";
    private RadioButton selectedRadioButton;

    public CustomerPricingAdapter(CustomerpricingActivity customerpricingActivity, List<PricingView> pricingViewsList, String displayType, String orderID, String priceID, String btn) {
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
    public void onBindViewHolder(@NonNull final CustomerPricingAdapter.OrderItemViewHolder holder, final int position) {

        try {
            final int pos = position;
            PricingView pricingView = pricingViewList.get(position);
            /*  holder.radioText.setText(String.valueOf(pricingView.getServiceName()));*/
            holder.catagoryName.setText(pricingView.getServiceName());
            holder.desText.setText(pricingView.getTime());
            holder.priceText.setText(String.valueOf(pricingView.getAmount()));
            holder.radioButton.setText(pricingView.getPricingTerms());
            holder.checkBox.setText(pricingView.getPricingTerms());
            holder.serviceDescText.setText(pricingView.getServiceName());
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
            } else if (displytype.equals("")) {
                holder.radioButton.setVisibility(View.GONE);
                holder.checkBox.setVisibility(View.GONE);
                holder.serviceDescText.setVisibility(View.VISIBLE);
            }

            holder.checkBox.setChecked(pricingViewList.get(position).isSelected());

            holder.checkBox.setTag(pricingViewList.get(position));


            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    PricingView pricingView1 = (PricingView) cb.getTag();

                    pricingView1.setSelected(cb.isChecked());
                    pricingViewList.get(pos).setSelected(cb.isChecked());

                    Toast.makeText(
                            v.getContext(),
                            "Clicked on Checkbox: " + cb.getText() + " is "
                                    + cb.isChecked(), Toast.LENGTH_LONG).show();
                }
            });

            holder.radioButton.setChecked(pricingViewList.get(position).isSelected());
            holder.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(PricingView model: pricingViewList)
                        model.setSelected(false);

                    pricingViewList.get(position).setSelected(true);

                    // If current view (RadioButton) differs from previous selected radio button, then uncheck selectedRadioButton
                    if(null != selectedRadioButton && !v.equals(selectedRadioButton))
                        selectedRadioButton.setChecked(false);

                    // Replace the previous selected radio button with the current (clicked) one, and "check" it
                    selectedRadioButton = (RadioButton) v;
                    selectedRadioButton.setChecked(true);

                    Toast.makeText(
                            v.getContext(),
                            "Clicked on Checkbox: " + holder.radioButton.getText() + " is "
                                    + selectedRadioButton.isChecked(), Toast.LENGTH_LONG).show();

                }
            });





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


        private TextView priceText, desText, serviceDescText, catagoryName, priceIDText;


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



            boolean checkbox = checkBox.isChecked();

        }

        @Override
        public void onClick(final View v) {
            boolean checked = checkBox.isChecked();
            pricingView = new PricingView();

            if (v.getId() == radioButton.getId()) {
//                if (radioButton.isChecked()) {
//                    Toast.makeText(mCtx, "Selected CheckBox ID" + priceIDText.getText(), Toast.LENGTH_SHORT).show();
//                    PricingView pView = new PricingView();
//                    pView.PricingID = Integer.valueOf((String) priceIDText.getText());
//                    SharedPrefManager.getInstance(mCtx).checked(pView);
//                    String s = String.valueOf(SharedPrefManager.getInstance(mCtx).getPricingID().PricingID);
//
//                } else {
//                    Toast.makeText(mCtx, "Not Selected" + radioButton.getText(), Toast.LENGTH_SHORT).show();
//                }
            }

        }


    }
    // method to access in activity after updating selection
    public List<PricingView> getPricinglist() {
        return pricingViewList;
    }

}
