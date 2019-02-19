package com.example.awizom.jihuzur.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.awizom.jihuzur.R;

public class CategoryGridViewAdapter extends BaseAdapter {

    private final String[] categoryList;
    private final int[] gridViewImageId;
    private Context mContext;

    public CategoryGridViewAdapter(Context context, String[] categoryList, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.length;
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
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.category_gridviewadapter, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.categoryName);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.categoryImage);
            textViewAndroid.setText(categoryList[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}