package com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AutoFlipper;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Model.DiscountModel;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.R;

import java.util.List;

public class DiscountImageAdapter extends RecyclerView.Adapter<DiscountImageAdapter.MyViewHolder> {

    private List<DiscountModel> discountModel;
    private Context mCtx;
    private String imagestr;

    public DiscountImageAdapter(Context autoFlipper, List<DiscountModel> discountModel) {
        this.discountModel = discountModel;
        this.mCtx = autoFlipper;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        position = 0;
        try {
            DiscountModel c = discountModel.get(position);
                holder.imagestring.setText(c.getPhoto().toString());
                imagestr = AppConfig.BASE_URL + holder.imagestring.getText().toString();
                Glide.with(mCtx)
                        .load(imagestr)
                        .placeholder(R.mipmap.final_top_home_image)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(holder.imagview);;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return discountModel.size();
    }

    @Override
    public DiscountImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ddiscountadapter_layout, parent, false);
        return new DiscountImageAdapter.MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

      public ViewFlipper viewFlipper;
        public ImageView imagview;
        private TextView imagestring;

        public MyViewHolder(View view) {
            super(view);
            imagview = view.findViewById(R.id.imageView);
            viewFlipper =view.findViewById(R.id.viewflipper);
            imagestring = view.findViewById(R.id.imgstring);
        }
    }


}
