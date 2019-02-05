package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;

import java.util.List;

public class CustomerCatagoryAdapter extends RecyclerView.Adapter<CustomerCatagoryAdapter.catalogListItemViewHolder> {

    private List<Catalog> categorylist;
    private Context mCtx;
    private Catalog categorys;
    private Intent intent;
    Catalog c;

    public CustomerCatagoryAdapter(Context applianceFragment, List<Catalog> categorylist) {

        this.categorylist = categorylist;
        this.mCtx=applianceFragment;
    }

    @NonNull
    @Override
    public CustomerCatagoryAdapter.catalogListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.customer_catagory_adapter, null);
        return new CustomerCatagoryAdapter.catalogListItemViewHolder(view, mCtx, categorylist);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomerCatagoryAdapter.catalogListItemViewHolder holder, int position) {
        c = categorylist.get(position);
        holder.catagory.setText(String.valueOf(c.getCategory()));
        holder.catalogid.setText(String.valueOf(c.getCatalogID()));
        holder.catalogoryName.setText(String.valueOf(c.getCatalogName()));
        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                intent = new Intent(mCtx, SelectServices.class);
                intent.putExtra("CategoryName",holder.catalogoryName.getText().toString());
                intent.putExtra("CatalogID", holder.catalogid.getText().toString());
                intent.putExtra("CatalogName", holder.catagory.getText().toString());
                mCtx.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    class catalogListItemViewHolder extends RecyclerView.ViewHolder {

        private Context mCtx;
        private TextView catagory,catalogid,catalogoryName;
        private List<Catalog> catalogList;

        public catalogListItemViewHolder(View view, Context mCtx, List<Catalog> catalogList) {
            super(view);
            this.mCtx = mCtx;
            this.catalogList = catalogList;

            catagory =  view.findViewById(R.id.categoryName);
            catalogoryName =  view.findViewById(R.id.catalogoryName);
            catalogid = view.findViewById(R.id.catalogId);


        }

    }
}
