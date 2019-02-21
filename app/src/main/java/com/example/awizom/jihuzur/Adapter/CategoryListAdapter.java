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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminCategoryActivity;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.Result;
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


public class CategoryListAdapter extends
        RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    public static final int SELECT_PHOTO = 100;
    String imagestr;
    Intent intent;
    int catalogID;
    String categoryname;
    String catalogName;
    AutoCompleteTextView categoryNames;
    ImageView imageView;
    String result = "";
    int[] gridViewImageId = new int[]{
            R.drawable.home_cleaning

    };
    private List<Catalog> categorylist;
    private Context mCtx;
    private Catalog categorys;


    public CategoryListAdapter(Context baseContext, List<Catalog> categorylist) {
        this.categorylist = categorylist;
        this.mCtx = baseContext;


    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Catalog c = categorylist.get(position);
        holder.category.setText(c.getCategory());
        holder.catalogid.setText(String.valueOf(c.getCatalogID()));
        imagestr = AppConfig.BASE_URL + c.getImage();
        holder.catalogname.setText(c.getCatalogName());

        try {
            if (c.getImage() == null) {
                holder.categoryImage.setImageResource(R.drawable.jihuzurblanklogo);
            } else {

                Glide.with(mCtx)
                        .load(imagestr)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(holder.categoryImage);

               // Glide.with(mCtx).load(imagestr).into(holder.categoryImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String categorynem = holder.category.getText().toString();
        final String cetlogId = holder.catalogid.getText().toString();
        final String cetlogName = holder.catalogname.getText().toString();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(mCtx, SelectServices.class);
                intent.putExtra("CategoryName", holder.category.getText());
                intent.putExtra("CatalogID", holder.catalogid.getText());
                intent.putExtra("CatalogName", catalogName);
                mCtx.startActivity(intent);


            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                ((AdminCategoryActivity) mCtx).showAddCategoryDialog(categorynem, cetlogId, cetlogName);

//                showEditCategoryDialog(categorynem, cetlogId, cetlogName);

                return true;
            }
        });

    }

    public void showEditCategoryDialog(String categoryname, final String catalogID, final String catalogName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mCtx);
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        final View dialogView = inflater.inflate(R.layout.add_category_layout, null);
        dialogBuilder.setView(dialogView);
        categoryNames = (AutoCompleteTextView) dialogView.findViewById(R.id.editCategory);
        categoryNames.setText(String.valueOf(categoryname));
        imageView = (ImageView) dialogView.findViewById(R.id.imageView);
        Button chooseImage = (Button) dialogView.findViewById(R.id.addImage);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//               openGallery();


            }
        });

        final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Edit Category");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String categoryName = categoryNames.getText().toString().trim();

                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image = stream.toByteArray();
                System.out.println("byte array:" + image);

                String img_str = Base64.encodeToString(image, 0);


                try {
                    result = new AdminHelper.POSTCategory().execute(catalogName, catalogID.trim(), categoryName, img_str).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    Toast.makeText(mCtx, jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
////                progressDialog.dismiss();
                } catch (Exception e) {

                }

                b.dismiss();

            }


        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                /*
                 * we will code this method to delete the artist
                 * */

            }
        });
    }

    public void openGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        ((Activity) mCtx).startActivityForResult(photoPickerIntent, SELECT_PHOTO);

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_categorylist, parent, false);

        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView category, catalogid, catalogname;
        public ImageView categoryImage;
        private List<Catalog> catalogList;

        public MyViewHolder(View view) {
            super(view);

            category = (TextView) view.findViewById(R.id.categoryName);
            categoryImage = (ImageView) view.findViewById(R.id.categoryImage);
            catalogname = (TextView) view.findViewById(R.id.catalogname);
            catalogid = (TextView) view.findViewById(R.id.catalogId);

        }


    }

}