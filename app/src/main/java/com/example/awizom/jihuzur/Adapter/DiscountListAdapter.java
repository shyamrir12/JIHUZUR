package com.example.awizom.jihuzur.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.AdminActivity.AdminDiscountActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Discount;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.Pricing;
import com.example.awizom.jihuzur.Model.PricingView;
import com.example.awizom.jihuzur.R;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiscountListAdapter extends RecyclerView.Adapter<DiscountListAdapter.MyViewHolder> implements View.OnTouchListener {

    private List<DiscountView> discountlist;
    private Context mCtx;
    private int position;
    String imagestr,result="";

    public DiscountListAdapter(Context baseContext, List<DiscountView> discountlist) {
        this.discountlist = discountlist;
        this.mCtx = baseContext;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DiscountView c = discountlist.get(position);
        holder.discountName.setText((c.getDiscountName()));
        holder.discountAmount.setText(String.valueOf("Rs " + c.getDiscount()));
        holder.discountType.setText(c.getDiscountType());
        holder.discountId.setText(String.valueOf(c.getDiscountID()));
        try {
            holder.imagestring.setText(c.getPhoto().toString());
            imagestr = AppConfig.BASE_URL + holder.imagestring.getText().toString();
            Glide.with(mCtx).load(imagestr).into(holder.imagview);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
                    alertDialogBuilder.setMessage("Are You Sure You Want to Delete this Discount Coupon");
                    alertDialogBuilder.setIcon(R.drawable.exit);
                            alertDialogBuilder.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            try {
                                                result = new AdminHelper.DeleteDiscount().execute(String.valueOf(holder.discountId.getText().toString())).get();
                                               // Toast.makeText(mCtx, result + "", Toast.LENGTH_SHORT).show();
                                                ((AdminDiscountActivity)mCtx).getDiscountList();
                                            } catch (ExecutionException e) {
                                                e.printStackTrace();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                return true;
            }
        });
        // holder.discountName.setTextAppearance(mCtx, R.style.fontForNotificationLandingPage);
    }

    @Override
    public int getItemCount() {
        return discountlist.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_discountlist, parent, false);
        return new MyViewHolder(v);
    }

    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView discountName, imagestring;
        public TextView discountType,discountId;
        public TextView discountAmount;
        public ImageView imagview;

        public MyViewHolder(View view) {
            super(view);
            discountId=(TextView) view.findViewById(R.id.discoountid);
            discountName = (TextView) view.findViewById(R.id.discountName);
            discountType = (TextView) view.findViewById(R.id.discounttype);
            discountAmount = (TextView) view.findViewById(R.id.discountAmount);
            imagestring = (TextView) view.findViewById(R.id.imgstring);
            imagview = (ImageView) view.findViewById(R.id.imageView);

        }
    }
}