package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.NewCustomerHome;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class CustomerHomePageAdapter extends BaseAdapter {

  //  private final String[] catalogNameList;
    private List<Catalog> catalogNameList;

    private Context mContext;



    public CustomerHomePageAdapter(NewCustomerHome newCustomerHome, List<Catalog> categorylist) {


this.mContext=newCustomerHome;
        this.catalogNameList = categorylist;
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
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.catalogname_gridview, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.catalogName);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.catalogImage);
            String imgstr= AppConfig.BASE_URL+catalogNameList.get(i).getImage().toString();

            Glide.with(mContext).load(imgstr).placeholder(R.drawable.jihuzurblanklogo).into(imageViewAndroid);
            textViewAndroid.setText(catalogNameList.get(i).getCategory());

        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}