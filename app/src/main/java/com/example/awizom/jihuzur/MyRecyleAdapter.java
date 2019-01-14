package com.example.awizom.jihuzur;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shiva on 26-02-2018.
 */

public class MyRecyleAdapter extends RecyclerView.Adapter<MyRecyleAdapter.MyViewHolder>
{

    Activity activity;
    ArrayList<String>list;
    LayoutInflater inflater;

    public MyRecyleAdapter(Activity activity, ArrayList<String> list)
    {
        this.activity=activity;
        this.list=list;
        inflater=LayoutInflater.from(activity);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.recycle_item,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tview.setText(list.get(position));
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        Button btn;
        TextView tview;

        public MyViewHolder(View itemView) {
            super(itemView);
            btn=(Button)itemView.findViewById(R.id.button);
            tview=(TextView)itemView.findViewById(R.id.textView);
        }
    }


}
