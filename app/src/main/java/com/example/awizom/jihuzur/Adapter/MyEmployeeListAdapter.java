package com.example.awizom.jihuzur.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.AdminActivity.AdminMyEmployeeActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.MyEmployeeListModel;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.MyCustomDialog;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class MyEmployeeListAdapter extends RecyclerView.Adapter<MyEmployeeListAdapter.MyViewHolder> {


    private List<MyEmployeeListModel> myEmployeeListModels;
    private Context mCtx;
    String result="";
    String id,active;

    public MyEmployeeListAdapter(Context baseContext, List<MyEmployeeListModel> myemployeeListModels) {
        this.myEmployeeListModels = myemployeeListModels;
        this.mCtx = baseContext;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MyEmployeeListModel c = myEmployeeListModels.get(position);

        if(c.getName()!=null) {
            holder.employeeName.setText(c.getName());
        }
        else
        {
            holder.employeeName.setText("No Name");
        }
        holder.mobileNo.setText(String.valueOf(c.getMobileNo()));
        holder.employeeid.setText(c.getID());



        if (c.isActive()) {
            holder.Activeemployee.setText("Deactivate");
            holder.Activeemployee.setChecked(true);

        } else {
            holder.Activeemployee.setText("Activate");
            holder.Activeemployee.setChecked(false);
        }
      holder.Activeemployee.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(holder.Activeemployee.getText().equals("Activate"))
              {
                  active="true";
                  id=holder.employeeid.getText().toString();
                  }
                  else {
                  active="false";
                  id=holder.employeeid.getText().toString();
              }
              getEmployeeActive();
          }

      });
    }


    private void getEmployeeActive() {
        try {
            result = new AdminHelper.SubmitEmployeeActive().execute(id,active).get();
            ((AdminMyEmployeeActivity)mCtx).getMyEmployeeList();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myEmployeeListModels.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_adminmyemployee, parent, false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView employeeName, mobileNo,employeeid;
        Switch Activeemployee;
        public MyViewHolder(View view) {
            super(view);
            employeeName = view.findViewById(R.id.employeeName);
            mobileNo = view.findViewById(R.id.mobileNumber);
            Activeemployee = view.findViewById(R.id.true_false);
            employeeid=view.findViewById(R.id.empid);
        }


    }

}