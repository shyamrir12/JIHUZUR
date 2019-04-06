package com.example.awizom.jihuzur.AdminActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awizom.jihuzur.Adapter.CategoryListAdapter;
import com.example.awizom.jihuzur.Adapter.DiscountListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class AdminDiscountActivity extends AppCompatActivity {

    FloatingActionButton addDiscount;
    AutoCompleteTextView editDiscountName, editDiscountAmount;
    Spinner editcategory, editDiscountType;
    ImageView addimage;
    ImageView imageView;
    ProgressDialog progressDialog;
    List<Catalog> categorylist;
    RecyclerView recyclerView;
    String result = "";
    ArrayList<String> worldlist;
    String discounttypes, category;
    Uri picUri;
    SwipeControllerDiscount swipeController;
    Uri outputFileUri;
    List<DiscountView> discountlist;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    DiscountListAdapter discountListAdapter;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_discount);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Discount Offer");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addDiscount = (FloatingActionButton) findViewById(R.id.adddiscount);
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getDiscountList();
        addDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDiscount();
            }
        });
    }

    private void getDiscountList() {

        try {
            result = new AdminHelper.GETDiscountList().execute().get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiscountView>>() {
                }.getType();
                discountlist = new Gson().fromJson(result, listType);
                discountListAdapter = new DiscountListAdapter(AdminDiscountActivity.this, discountlist);
                recyclerView.setAdapter(discountListAdapter);
                swipeController = new SwipeControllerDiscount(new SwipeControllerActions() {
                    @Override
                    public void onRightClicked(int position) {
                        String discountid = String.valueOf(discountlist.get(position).getDiscountID());
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        // Toast.makeText(getApplicationContext(), position + "position", Toast.LENGTH_LONG).show();
                        deletediscount(discountid);
                        progressDialog.dismiss();
                   /* mAdapter.players.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());*/
                    }

                    @Override
                    public void onLeftClicked(int position) {
                        super.onLeftClicked(position);
                        Intent intent = new Intent(AdminDiscountActivity.this, AdminHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    }

                });

                ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                itemTouchhelper.attachToRecyclerView(recyclerView);

                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                        swipeController.onDraw(c);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletediscount(String discountid) {

        try {
            result = new AdminHelper.DeleteDiscount().execute(discountid).get();
            Toast.makeText(getApplicationContext(), result + "", Toast.LENGTH_SHORT).show();
            getDiscountList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showAddDiscount() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_discount_alertlayout, null);
        dialogBuilder.setView(dialogView);
        editDiscountName = (AutoCompleteTextView) dialogView.findViewById(R.id.editDiscountName);
        editDiscountType = (Spinner) dialogView.findViewById(R.id.editDiscountType);
        editDiscountAmount = (AutoCompleteTextView) dialogView.findViewById(R.id.editDiscountAmount);
        editcategory = (Spinner) dialogView.findViewById(R.id.editDiscountcategory);
        imageView = (ImageView) dialogView.findViewById(R.id.imageView);
        addimage = (ImageView) dialogView.findViewById(R.id.addImage);

        getCategoryList();
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(String.valueOf("Fix"));
        spinnerArray.add("Percentage");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, spinnerArray);
        editDiscountType.setAdapter(arrayAdapter);
        editcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = "Plumber";
            }
        });
        editDiscountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discounttypes = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                discounttypes = "Fix";
            }
        });

        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });
        final Button buttonAddDiscount = (Button) dialogView.findViewById(R.id.buttonAddDiscount);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Discount");
        dialogBuilder.setIcon(R.drawable.coupons);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                    byte[] image = stream.toByteArray();
                    System.out.println("byte array:" + image);
                    String img_str = Base64.encodeToString(image, 0);
                    String discountName = editDiscountName.getText().toString();
                    String discounttype = discounttypes;
                    String discountamount = editDiscountAmount.getText().toString().trim();
                    String edtcategory = category;

                    try {
                        //String res="";
                        progressDialog.setMessage("loading...");
                        progressDialog.show();
                        new AdminHelper.POSTDiscount().execute(discountName, discounttype, discountamount, edtcategory, img_str);
                        getDiscountList();
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        // System.out.println("Error: " + e);
                    }

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
                 */
            }
        });
    }

    private void getCategoryList() {

        try {
            result = new AdminHelper.GETCategoryList().execute("Home Cleaning & Repairs".toString()).get();
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Catalog>>() {
            }.getType();
            categorylist = new Gson().fromJson(result, listType);
            worldlist = new ArrayList<String>();
            for (int i = 0; i < categorylist.size(); i++) {
                worldlist.add(categorylist.get(i).getCategory());
            }

            editcategory.setAdapter(new ArrayAdapter<String>(AdminDiscountActivity.this,
                    android.R.layout.simple_spinner_dropdown_item,
                    worldlist));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Intent getPickImageChooserIntent() {
        outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        outputFileUri = null;
        File getImage = getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    imageView.setImageBitmap(selectedImage);
                }
            }
        }
    }

    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;
        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());
    }

    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }
                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private boolean validation() {

        if (editDiscountName.getText().toString().isEmpty()) {
            editDiscountName.setError("Enter a valid Discount Name");
            editDiscountName.requestFocus();
            return false;
        }

        if (editDiscountAmount.getText().toString().isEmpty()) {
            editDiscountAmount.setError("Enter a valid Discount Amount");
            editDiscountAmount.requestFocus();
            return false;
        }
        return true;
    }

}
