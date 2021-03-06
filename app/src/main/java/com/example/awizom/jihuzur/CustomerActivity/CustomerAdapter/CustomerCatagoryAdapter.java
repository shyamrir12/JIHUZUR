package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;

import java.util.List;

public class CustomerCatagoryAdapter extends RecyclerView.Adapter<CustomerCatagoryAdapter.catalogListItemViewHolder> {

    private List<Catalog> categorylist;
    private Context mCtx;
    private Catalog categorys;
    private Intent intent;
    Catalog c;
    String imagelink="";
    ViewDialog viewDialog;

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
        viewDialog = new ViewDialog((Activity) mCtx);
        holder.catagory.setText(String.valueOf(c.getCategory()));
        holder.catalogid.setText(String.valueOf(c.getCatalogID()));
        holder.catalogoryName.setText(String.valueOf(c.getCatalogName()));


        if(c.getImage() != null) {
            imagelink = AppConfig.BASE_URL + c.getImage().toString();
            Glide.with(mCtx)
                    .load(AppConfig.BASE_URL + c.getImage().toString())
                    .placeholder(R.drawable.home_cleaning).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.imageViewList);
            holder.imglinkurl.setText(imagelink);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                intent = new Intent(mCtx, SelectServices.class);

                intent.putExtra("CatalogID", holder.catalogid.getText().toString());
                intent.putExtra("CategoryName", holder.catagory.getText().toString());
                intent.putExtra("Image", holder.imglinkurl.getText().toString());
                intent.putExtra("EmployeeSkill","EmployeeSkill");
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
        private TextView catagory,catalogid,catalogoryName,imglinkurl;
        private ImageView imageViewList;
        private List<Catalog> catalogList;

        public catalogListItemViewHolder(View view, Context mCtx, List<Catalog> catalogList) {
            super(view);
            this.mCtx = mCtx;
            this.catalogList = catalogList;
            String role= SharedPrefManager.getInstance(mCtx).getUser().getRole();
            catagory =  view.findViewById(R.id.categoryName);
            catalogoryName =  view.findViewById(R.id.catalogoryName);
            catalogid = view.findViewById(R.id.catalogId);
            imageViewList = view.findViewById(R.id.categoryImage);
            imglinkurl = view.findViewById(R.id.imgLink);



        }

    }

    public void showCustomLoadingDialog(View view) {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }
}
