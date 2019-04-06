package com.example.awizom.jihuzur;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;

public class ExampleFliperAdapter extends BaseAdapter {
    Context context;

    String[] imageNames;
    LayoutInflater inflter;


    public ExampleFliperAdapter(Context applicationContext, String[]  imageNames) {
        this.context = applicationContext;
        this.imageNames = imageNames;

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
        TextView imageName =  view.findViewById(R.id.fruitName);
        ImageView imageView = view.findViewById(R.id.fruitImage);
        imageName.setText(imageNames[position]);



        String imgString = AppConfig.BASE_URL+imageNames[position];


        Glide.with(context)
                .load(imgString)
                .placeholder(R.drawable.jihuzurblanklogo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
        return view;
    }
}