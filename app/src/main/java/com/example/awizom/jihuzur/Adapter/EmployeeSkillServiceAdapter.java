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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeSkillActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.Model.Skill;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SelectServices;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class EmployeeSkillServiceAdapter extends
        RecyclerView.Adapter<EmployeeSkillServiceAdapter.MyViewHolder> {


    List<Skill> serviceListforshow;
    String result = "";
    private Context mCtx;
    String Skillid;


    public EmployeeSkillServiceAdapter(Context baseContext, List<Skill> serviceListforshow) {
        this.serviceListforshow = serviceListforshow;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Skill c = serviceListforshow.get(position);
        holder.serviceName.setText(c.getServiceName());
        holder.skillid.setText(String.valueOf(c.getID()));

         Skillid=holder.skillid.getText().toString();
         holder.deleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteskill(Skillid);
            }
        });


    }

    private void deleteskill(String skillid) {

        try {
            result = new EmployeeOrderHelper.EmployeePOSTDeleteSkill().execute(skillid).get();
            Gson gson = new Gson();
            final Result jsonbodyres = gson.fromJson(result, Result.class);
            Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
            ((EmployeeSkillActivity)mCtx).getEmployeeSkill();
////                progressDialog.dismiss();
        } catch (Exception e) {

        }







    }

    @Override
    public int getItemCount() {
        return serviceListforshow.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_employeeskillservice, parent, false);

        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceName,skillid;
        public ImageButton deleteSkill;


        public MyViewHolder(View view) {
            super(view);

            serviceName = (TextView) view.findViewById(R.id.serviceName);
            deleteSkill = (ImageButton) view.findViewById(R.id.deleteskill);
            skillid=(TextView)view.findViewById(R.id.skillid);



        }


    }

}