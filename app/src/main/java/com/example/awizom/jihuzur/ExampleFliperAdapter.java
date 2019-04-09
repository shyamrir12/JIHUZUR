package com.example.awizom.jihuzur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;

public class ExampleFliperAdapter extends BaseAdapter {
    Context context;

    String[] imageNames;
    String[] discountNames;
    String[] dicountAmounts;
    LayoutInflater inflter;


    public ExampleFliperAdapter(Context applicationContext, String[]  imageNames,String[]  discountNames,String[]  dicountAmounts) {
        this.context = applicationContext;
        this.imageNames = imageNames;
        this.discountNames = discountNames;
        this.dicountAmounts = dicountAmounts;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return imageNames.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflter.inflate(R.layout.example_listitem, null);
        TextView discountName,imageName,discountAmount;
        imageName =  view.findViewById(R.id.fruitName);
        discountName =  view.findViewById(R.id.discountName);
        discountAmount =  view.findViewById(R.id.discountAmount);
        ImageView imageView = view.findViewById(R.id.dicountImgName);
        imageName.setText(imageNames[position]);
        discountName.setText(discountNames[position]);
        discountAmount.setText(dicountAmounts[position]);
      imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        final LinearLayout layoutDiscountDetail = view.findViewById(R.id.layout_discount_detail);



        String imgString = AppConfig.BASE_URL+imageNames[position];


        Glide.with(context)
                .load(imgString)
                .placeholder(R.drawable.jihuzurblanklogo)
                .into(imageView);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutDiscountDetail.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }
}