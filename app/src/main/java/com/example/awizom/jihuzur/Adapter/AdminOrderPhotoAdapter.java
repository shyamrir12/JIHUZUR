package com.example.awizom.jihuzur.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.OrderPhoto;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AdminOrderPhotoAdapter extends RecyclerView.Adapter<com.example.awizom.jihuzur.Adapter.AdminOrderPhotoAdapter.MyViewHolder> {


    private List<OrderPhoto> orderPhotoList;
    private Context mCtx;
    String imagestr, imageurl;

    public AdminOrderPhotoAdapter(Context baseContext, List<OrderPhoto> orderPhotolist) {
        this.orderPhotoList = orderPhotolist;
        this.mCtx = baseContext;

    }


    @Override
    public void onBindViewHolder(final com.example.awizom.jihuzur.Adapter.AdminOrderPhotoAdapter.MyViewHolder holder, final int position) {
        OrderPhoto c = orderPhotoList.get(position);
        imageurl = c.getPhoto();
        imagestr = AppConfig.BASE_URL + c.getPhoto();

        if (imageurl != null) {
            holder.orderid.setText(String.valueOf(c.getOrderID()));
            holder.employeename.setText(c.getEmpName());
            holder.cutomername.setText(c.Name);
            holder.customermobile.setText(c.getMobileNo());
            holder.employeemob.setText(c.getEmpMob());
            Glide.with(mCtx).load(imagestr).placeholder(R.drawable.jihuzurblanklogo).into(holder.orderphoto);
        }

    }


    @Override
    public int getItemCount() {
        return orderPhotoList.size();
    }

    @Override
    public com.example.awizom.jihuzur.Adapter.AdminOrderPhotoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_orderphotolist, parent, false);


        return new com.example.awizom.jihuzur.Adapter.AdminOrderPhotoAdapter.MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView orderphoto;
        TextView orderid, cutomername, employeename, customermobile, employeemob;

        public MyViewHolder(View view) {
            super(view);
            orderphoto = (ImageView) view.findViewById(R.id.orderphoto);
            orderid = (TextView) view.findViewById(R.id.orderID);
            cutomername = (TextView) view.findViewById(R.id.customername);
            employeename = (TextView) view.findViewById(R.id.empname);
            customermobile = (TextView) view.findViewById(R.id.customermob);
            employeemob = (TextView) view.findViewById(R.id.empmob);

        }


    }

}