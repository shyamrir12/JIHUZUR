package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.NewCustomerHome;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;

import java.util.List;

public class CustomerHomePageAdapter extends BaseAdapter {

  //  private final String[] catalogNameList;
    private List<Catalog> catalogNameList;

    private Context mContext;



    public CustomerHomePageAdapter(CustomerHomePage newCustomerHome, List<Catalog> categorylist) {


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
    public View getView(final int i, final View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = inflater.inflate(R.layout.catalogname_gridview, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.catalogName);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.catalogImage);
            final TextView imglinkurl = gridViewAndroid.findViewById(R.id.imgLink);

            try {
                String imgstr= AppConfig.BASE_URL+catalogNameList.get(i).getImage().toString();
                Glide.with(mContext).load(imgstr).placeholder(R.drawable.jihuzurblanklogo).into(imageViewAndroid);
                textViewAndroid.setText(catalogNameList.get(i).getCategory());

                if(catalogNameList.get(i).getImage().toString()!= null) {
                    imglinkurl.setText(imgstr.toString());

                }

                gridViewAndroid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext,convertView+"convert",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, SelectServices.class);
                        intent.putExtra("CatalogID",String.valueOf(catalogNameList.get(i).getCatalogID()));
                        intent.putExtra("CategoryName", catalogNameList.get(i).getCategory().toString());
                        intent.putExtra("Image",imglinkurl.getText().toString());
                        intent.putExtra("EmployeeSkill","EmployeeSkill");
                        mContext.startActivity(intent);
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }

        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
}