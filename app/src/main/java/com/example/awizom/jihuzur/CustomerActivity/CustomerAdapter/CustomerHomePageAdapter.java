package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.NewCustomerHome;
import com.example.awizom.jihuzur.CustomerActivity.PlumberActivity;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.example.awizom.jihuzur.ViewDialog;

import java.util.List;

public class CustomerHomePageAdapter extends BaseAdapter {

    //  private final String[] catalogNameList;
    private List<Catalog> catalogNameList;
    ViewDialog viewDialog;
    private Context mContext;
    private String skipdata="";

    public CustomerHomePageAdapter(CustomerHomePage newCustomerHome, List<Catalog> categorylist, String skipdata) {

        this.mContext = newCustomerHome;
        this.catalogNameList = categorylist;
        this.skipdata = skipdata;
    }

    @Override
    public int getCount() {

            return catalogNameList.size();

    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, final View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.catalogname_gridview, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.catalogName);
            de.hdodenhof.circleimageview.CircleImageView imageViewAndroid = (de.hdodenhof.circleimageview.CircleImageView) gridViewAndroid.findViewById(R.id.catalogImage);
            final TextView imglinkurl = gridViewAndroid.findViewById(R.id.imgLink);
            //  final ProgressBar progressBar = gridViewAndroid.findViewById(R.id.homeprogress);

            viewDialog = new ViewDialog((Activity) mContext);
            try {
                String imgstr = AppConfig.BASE_URL + catalogNameList.get(i).getImage().toString();

                if (catalogNameList.get(i).getImage().toString() != null) {
                    Glide.with(mContext).load(imgstr).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageViewAndroid);
                }
                textViewAndroid.setText(catalogNameList.get(i).getCategory());
                if (catalogNameList.get(i).getImage().toString() != null) {
                    imglinkurl.setText(imgstr.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


//                Glide.wit h(mContext).load(imgstr).placeholder(R.drawable.jihuzurblanklogo)
//                        .listener(new RequestListener<String, GlideDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                progressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                progressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//                        })
//                .into(imageViewAndroid);
            try {
                gridViewAndroid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showcustomloadingdialog();
                        if (catalogNameList.get(i).getCategory().toString().equals("Plumber")) {
                            Intent intent = new Intent(mContext, PlumberActivity.class);
                            mContext.startActivity(intent);
                        } else {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Check Permissions Now
                                ActivityCompat.requestPermissions(((CustomerHomePage)mContext),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        200);
                            } else {
                                // permission has been granted, continue as usual
                                Intent intent = new Intent(mContext, SelectServices.class);
                                intent.putExtra("CatalogID", String.valueOf(catalogNameList.get(i).getCatalogID()));
                                intent.putExtra("CategoryName", catalogNameList.get(i).getCategory().toString());
                                intent.putExtra("Image", imglinkurl.getText().toString());
                                intent.putExtra("EmployeeSkill", "EmployeeSkill");
                                intent.putExtra("Skip",skipdata);
                                mContext.startActivity(intent);
                            }
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }


    public void showcustomloadingdialog() {

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