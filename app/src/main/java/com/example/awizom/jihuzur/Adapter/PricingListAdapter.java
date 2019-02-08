package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminActivity.AdminPricingActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


public class PricingListAdapter extends
        RecyclerView.Adapter<PricingListAdapter.MyViewHolder> implements View.OnTouchListener {

    private List<PricingView> pricingList;
    private Context mCtx;
    private int position;
    String result = "", displaytype = "", pricingType;
    String pricingendSlot, pricingSlots;


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView pricingterms;
        public TextView amount;
        public TextView description;
        public TextView pricingId;
        public TextView catalogid;

        public SeekBar seekBar;


        public MyViewHolder(View view) {
            super(view);
            pricingterms = (TextView) view.findViewById(R.id.pricingTerms);
            amount = (TextView) view.findViewById(R.id.amount);
            pricingId = (TextView) view.findViewById(R.id.pricingID);
            catalogid = (TextView) view.findViewById(R.id.catalogID);
            description = (TextView) view.findViewById(R.id.description);

        }
    }

    public PricingListAdapter(Context baseContext, List<PricingView> pricingList, String displayType) {
        this.pricingList = pricingList;
        this.displaytype = displayType;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PricingView c = pricingList.get(position);
        holder.pricingterms.setText((c.getPricingTerms()));
        holder.amount.setText(String.valueOf("Rs " + c.getAmount()));
        holder.description.setText(c.getPrizingDesc());
        holder.pricingId.setText(String.valueOf(c.getPricingID()));
        holder.catalogid.setText(String.valueOf(c.getCatalogID()));

        final String pricingterms = holder.pricingterms.getText().toString();
        final String amount = holder.amount.getText().toString();
        final String description = holder.description.getText().toString();
        final String pricingId = holder.pricingId.getText().toString();
        final String catalogid = holder.catalogid.getText().toString();

        if (displaytype.equals("")) {

            pricingType = "Range";

        } else {

            pricingType = "Fix";

        }
        pricingSlots = "0";
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showEditpricing(pricingId, catalogid, pricingterms, amount, description);
                return false;
            }
        });
    }

    private void showEditpricing(final String pricingId, final String catalogid, String pricingterms, String amount, String description) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.add_pricing_layout, null);
        dialogBuilder.setView(dialogView);

        final AutoCompleteTextView editdescription = (AutoCompleteTextView) dialogView.findViewById(R.id.editDescription);
        editdescription.setText(description);
        final AutoCompleteTextView addpricingterms = (AutoCompleteTextView) dialogView.findViewById(R.id.addPricingTerms);

        addpricingterms.setText(pricingterms);
        final AutoCompleteTextView editamount = (AutoCompleteTextView) dialogView.findViewById(R.id.editAmount);
        editamount.setText(amount);
        final AutoCompleteTextView noOfItems = (AutoCompleteTextView) dialogView.findViewById(R.id.numberItems);
        noOfItems.setText("0");
        final AutoCompleteTextView pricingEndSlot = (AutoCompleteTextView) dialogView.findViewById(R.id.prizingEndSlot);
        pricingEndSlot.setText("0");


        pricingEndSlot.setVisibility(View.GONE);


        noOfItems.setVisibility(View.GONE);


        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddCatalog);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Pricing");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String description = editdescription.getText().toString().trim();
                String pricing = addpricingterms.getText().toString().trim();
                String amount = editamount.getText().toString().split(" ")[1];

                pricingendSlot = pricingEndSlot.getText().toString();


                try {
                    //String res="";

                    result = new AdminHelper.POSTPricing().execute(description, pricing, amount, catalogid, pricingSlots, pricingType, pricingendSlot, pricingId).get();

                    if (result.isEmpty()) {

                        Toast.makeText(mCtx, "Invalid request", Toast.LENGTH_SHORT).show();
                    } else {
                        //System.out.println("CONTENIDO:  " + result);
                        Gson gson = new Gson();
                        final Result jsonbodyres = gson.fromJson(result, Result.class);
                        Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();

                    }


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
        return pricingList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_pricinglist, parent, false);
        return new MyViewHolder(v);
    }
}