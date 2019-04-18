package com.example.awizom.jihuzur.EmployeeActivity;

import android.accounts.AccountManager;
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
import android.graphics.Color;
import android.icu.text.UnicodeSetSpanner;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EmployeeMyProfileActivity extends AppCompatActivity {


    String identityimage = "";
    Uri picUri;
    Uri outputFileUri;
    Button upload;
    ProgressDialog pd;
    de.hdodenhof.circleimageview.CircleImageView imageView;
    EditText yourname,address;
    TextView email;
    Intent intent = new Intent();
    String result = "", img_str, identimage_str;
    ImageView identityImage;
    FloatingActionButton fabidentityImage;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    String Check;
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_my_profile);
        upload = findViewById(R.id.upload);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        pd = new ProgressDialog(this);
        pd.setMessage("Uploading....");
        yourname = (EditText) findViewById(R.id.name);
        yourname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                yourname.setCursorVisible(true);
                return false;
            }
        });
        email=(TextView) findViewById(R.id.Email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
                startActivityForResult(googlePicker, 201);
            }
        });

        address=(EditText)findViewById(R.id.Adress);
        imageView = findViewById(R.id.imageView);
        identityImage = findViewById(R.id.identityImage);

        /*  yourname.setText(SharedPrefManager.getInstance(this).getUser().getName());*/

        identityImage.setVisibility(View.GONE);
        fabidentityImage = (FloatingActionButton) findViewById(R.id.fab3);
          identityImage.setVisibility(View.VISIBLE);
              fabidentityImage.setVisibility(View.VISIBLE);

        fabidentityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check = "IdentityImage";
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check = "Image";
                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
            }
        });


       /* img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();*/
     /*   try {
               *//* DataProfile dataProfile = new DataProfile();
                dataProfile.Image = img_str;*//*
            SharedPrefManager.getInstance(this).getUser().setImage(img_str);
            if (SharedPrefManager.getInstance(this).getUser().getImage() == null) {
                imageView.setImageResource(R.drawable.jihuzurblanklogo);
                *//*        Glide.with(mCtx).load("http://192.168.1.105:7096/Images/Category/1.png").into(holder.categoryImage);*//*
            } else {
                Glide.with(this)
                        .load(img_str)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                byte[] image = stream.toByteArray();
                System.out.println("byte array:" + image);
                String img_str = Base64.encodeToString(image, 0);
                String name = yourname.getText().toString();
                String emails=email.getText().toString();
                String addresss=address.getText().toString();

                try {

                    if (SharedPrefManager.getInstance(EmployeeMyProfileActivity.this).getUser().getRole().equals("Employee")) {
                        identityImage.buildDrawingCache();
                        Bitmap bitmap1 = identityImage.getDrawingCache();
                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        bitmap1.compress(Bitmap.CompressFormat.PNG, 90, stream1);
                        byte[] image1 = stream1.toByteArray();
                        identityimage = Base64.encodeToString(image1, 0);
                    } else {
                        identityimage = "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "You Have to Login First", Toast.LENGTH_LONG).show();
                }
                String id = SharedPrefManager.getInstance(EmployeeMyProfileActivity.this).getUser().getID();
                String lat = "";
                String longs = "";
                try {
                    result = new EmployeeHelper.POSTProfile().execute(id, name, img_str, identityimage, lat, longs,emails,addresss).get();
                    Gson gson = new Gson();
                    final Result jsonbodyres = gson.fromJson(result, Result.class);
                    DataProfile dataProfile = new DataProfile();
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();
                    dataProfile.Image = jsonbodyres.Image;
                    dataProfile.Name = jsonbodyres.Name;
                    dataProfile.Email = jsonbodyres.Email;
                    dataProfile.Address = jsonbodyres.Address;

                    SharedPrefManager.getInstance(EmployeeMyProfileActivity.this).getUser().setImage(String.valueOf(dataProfile));
                    /*   SharedPrefManager.getInstance(DrawingActivity.this).getUser().setName(String.valueOf(yourname.getText()));*/
                    if (SharedPrefManager.getInstance(EmployeeMyProfileActivity.this).getUser().getImage() != null) {

                    }

                 /*  {
                       if(dataProfile.isActiveStatus()) {
                           intent = new Intent(EmployeeMyProfileActivity.this, EmployeeHomePage.class);
                           startActivity(intent);
                       }else {
                           Toast.makeText(getApplicationContext(),"Please Contact your admin",Toast.LENGTH_SHORT).show();
                       }
                    }*/

                } catch (Exception e) {

                }
                intent =new Intent(EmployeeMyProfileActivity.this,EmployeeHomePage.class);
                startActivity(intent);

            }

        });
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }

        getProfile();
    }

    private void getProfile() {
        String id = SharedPrefManager.getInstance(this).getUser().getID();
        try {
            result = new AdminHelper.GetProfileForShow().execute(id).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                DataProfile dataProfile = new Gson().fromJson(result, listType);
                  String imageurl = dataProfile.getImage();
                    String img_str = AppConfig.BASE_URL + imageurl;
                     yourname.setText(dataProfile.getName().toString());
                        email.setText(dataProfile.getEmail().toString());
                        address.setText(dataProfile.getAddress().toString());
                Glide.with(EmployeeMyProfileActivity.this).load(img_str). diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(imageView);
                identimage_str = AppConfig.BASE_URL + dataProfile.getIdentityImage();
                                       Glide.with(this)
                                .load(identimage_str)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(identityImage);


                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();
                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;

                    /*  SharedPrefManager.getInstance(this).userLogin(dataProfile1);*/
                }
            }
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
        if (requestCode == 201 && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            email.setText(accountName);
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_RESULT) {
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    if (Check == "Image") {
                        imageView.setImageBitmap(selectedImage);
                    } else {
                        identityImage.setImageBitmap(selectedImage);
                    }
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
}
