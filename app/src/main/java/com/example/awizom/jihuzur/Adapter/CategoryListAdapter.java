package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;


public class CategoryListAdapter extends
        RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>  {

    private List<Catalog> categorylist;
    private Context mCtx;
    private Catalog categorys;
    String imagestr;
    Intent intent;
    int catalogID;
    String categoryname;
    String catalogName;
    int[] gridViewImageId = new int[]{
            R.drawable.home_cleaning

    };



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView category,catalogid;
        public ImageView categoryImage;
        private List<Catalog> catalogList;

        public MyViewHolder(View view) {
            super(view);

            category = (TextView) view.findViewById(R.id.categoryName);
            categoryImage=(ImageView)view.findViewById(R.id.imageView);
            catalogid=(TextView)view.findViewById(R.id.catalogId);

        }


    }

    public CategoryListAdapter(Context baseContext, List<Catalog> categorylist) {
        this.categorylist = categorylist;
        this.mCtx=baseContext;


    }

    @Override
    public void onBindViewHolder  (final MyViewHolder holder, final int position) {
        Catalog c = categorylist.get(position);
         holder.category.setText(c.getCategory());
         holder.catalogid.setText(String.valueOf(c.getCatalogID()));
        imagestr= AppConfig.BASE_URL+c.getImage();

try {
    if (c.getImage() == null)

    {
        Glide.with(mCtx).load("http://192.168.1.202:7096//Images/Category/1.png").into(holder.categoryImage);
    } else {
        Glide.with(mCtx).load(imagestr).into(holder.categoryImage);
    }
}
catch (Exception e)
{
e.printStackTrace();

}

// Glide.with(mCtx).load("http://192.168.1.202:7096//Images/Category/1.png").into(holder.categoryImage);
        //http://192.168.1.202:7096//Images/Category/1.png

  holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        intent = new Intent(mCtx, SelectServices.class);
        intent.putExtra("CategoryName", holder.category.getText());
        intent.putExtra("CatalogID", holder.catalogid.getText());
        intent.putExtra("CatalogName", catalogName);
        mCtx.startActivity(intent);


    }
});

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_categorylist,parent, false);

        return new MyViewHolder(v);
    }
}