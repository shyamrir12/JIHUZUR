package com.example.awizom.jihuzur.CustomerActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterViewFlipper;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerCatagoryAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerHomePageAdapter;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.ExampleFliperAdapter;
import com.example.awizom.jihuzur.Fragment.CatalogFragment;
import com.example.awizom.jihuzur.Fragment.MyBookingFragment;
import com.example.awizom.jihuzur.Fragment.SearchFragment;
import com.example.awizom.jihuzur.HelpCenterActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Model.Catalog;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.DiscountModel;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Created by Ravi on 07/01/2019.
 */

public class CustomerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG;
    private Fragment fragment = null;
    private Fragment searchFragment, myBookingFragment, helpCenterFragment, catalogFragment;
    DatabaseReference datauser, datauserpro;
    String dUser, name, role;
    String Url;
    Boolean active = false;
    View header;
    TextView userName, userContact;
    ImageView imageView;
    String img_str;
    private String skipdata = "";
    boolean check = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
    Intent intent;
    private String result = "", catalogName = "Home Cleaning & Repairs";
    ViewFlipper recyclerView;
    CustomerCatagoryAdapter customerCatagoryAdapter;
    ViewDialog viewDialog;
    GridView gridView;
    FirebaseFirestore db;
    List<Catalog> categorylist;
    String imgstr;
    private AdapterViewFlipper simpleAdapterViewFlipper;
    private List<DiscountModel> discountModel;


    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        //bottom navigation Button Onclick
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class framentClass = null;

            switch (item.getItemId()) {
                case R.id.navigation_search:
                    if (!skipdata.equals("SkipLogin")) {
                        showCustomLoadingDialog();
                        intent = new Intent(CustomerHomePage.this, CustomerHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    } else {

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                        alertDialog.setTitle("Sorry !!");
                        alertDialog.setMessage("You Have To Need Login First");


                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        alertDialog.setIcon(R.drawable.warning);

                        alertDialog.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                        intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                        if (skipdata.equals("SkipLogin")) {
                                            intent.putExtra("Skip", "SkipLogin");
                                        }
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                                    }
                                });


                        alertDialog.show();
                    }
                    break;

                case R.id.navigation_booking:
                    if (!skipdata.equals("SkipLogin")) {
                        showCustomLoadingDialog();
                        intent = new Intent(CustomerHomePage.this, MyBokingsActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    } else {

                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                        alertDialog.setTitle("Sorry !!");
                        alertDialog.setMessage("You Have To Need Login First");


                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);

                        alertDialog.setIcon(R.drawable.warning);

                        alertDialog.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                        intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                        if (skipdata.equals("SkipLogin")) {
                                            intent.putExtra("Skip", "SkipLogin");
                                        }
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                                    }
                                });


                        alertDialog.show();
                    }
                    break;

                case R.id.navigation_helpCenter:
                    showCustomLoadingDialog();
                    intent = new Intent(CustomerHomePage.this, HelpCenterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//                    getSupportActionBar().setTitle("Help Center");
//                    fragment = helpCenterFragment;
//                    framentClass = HelpCenterFragment.class;
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    break;
            }
            try {
                fragment = (Fragment) framentClass.newInstance();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_container, fragment).addToBackStack(null).commit();
                setTitle("");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    };

    //layout declaration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        try {
            db.collection("ChatNotification").document(SharedPrefManager.getInstance(this).getUser().getID().toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w("failed", "Listen failed.", e);
                        return;
                    }
                    String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                            ? "Local" : "Server";
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Log.d("Snapshot data", source + " data: " + documentSnapshot.getData());
                        final Intent emptyIntent = new Intent(CustomerHomePage.this, MyBokingsActivity.class);
                        NotificationManager notificationManager = (NotificationManager) CustomerHomePage.this.getSystemService(Context.NOTIFICATION_SERVICE);
                        String channelId = "channel-01";
                        String channelName = "Channel Name";
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            NotificationChannel mChannel = new NotificationChannel(
                                    channelId, channelName, importance);
                            notificationManager.createNotificationChannel(mChannel);
                        }

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CustomerHomePage.this, channelId)
                                .setSmallIcon(R.mipmap.jihuzurapplogo)
                                .setContentTitle("Hey! You Have Message")
                                .setContentText(String.valueOf("Jihuzzur,You Have Message from Employee"));

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(CustomerHomePage.this);
                        /*   stackBuilder.addNextIntent(intent);*/
                        PendingIntent pendingIntent = PendingIntent.getActivity(CustomerHomePage.this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        mBuilder.setContentIntent(pendingIntent);

                        notificationManager.notify(199, mBuilder.build());
                        db.collection("ChatNotification").document(SharedPrefManager.getInstance(CustomerHomePage.this).getUser().getID().toString()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initView() {

        searchFragment = new SearchFragment();
        myBookingFragment = new MyBookingFragment();
        catalogFragment = new CatalogFragment();
        setContentView(R.layout.activity_customer_home_page);
        gridView = (GridView) findViewById(R.id.gridview);
        try {
            skipdata = getIntent().getStringExtra("Skip").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        db = FirebaseFirestore.getInstance();
        getCategoryList();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.jihuzzur_home_logo);
        toolbar.setTitle("");
        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem target = menu.findItem(R.id.nav_logout);
        if (skipdata.equals("SkipLogin")) {
            target.setTitle("login");


            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
            alertDialog.setTitle("Sorry !!");
            alertDialog.setMessage("You Have To Need Login First");


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            alertDialog.setIcon(R.drawable.warning);

            alertDialog.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });


            alertDialog.show();
        }

        View headerview = navigationView.getHeaderView(0);
        viewDialog = new ViewDialog((Activity) CustomerHomePage.this);
        imageView = headerview.findViewById(R.id.imageView);
        simpleAdapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.simpleAdapterViewFlipper);

//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setFlipInterval(1000);
       /* recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        /*  img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();*/
       /* {
            try {
                if (SharedPrefManager.getInstance(this).getUser().getImage() == null) {
                    imageView.setImageResource(R.drawable.jihuzurblanklogo);
                } else {
                    Glide.with(CustomerHomePage.this)
                            .load(img_str)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        userName = headerview.findViewById(R.id.profileName);
        userContact = headerview.findViewById(R.id.cusContact);
        if (skipdata.equals("SkipLogin")) {
            try {
                String uname = String.valueOf(SharedPrefManager.getInstance(CustomerHomePage.this).getUser().getName().toString());
                String ucontact = String.valueOf(SharedPrefManager.getInstance(CustomerHomePage.this).getUser().getMobileNo().toString());
                userName.setText(uname);
                userContact.setText(ucontact);
                /**/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!skipdata.equals("SkipLogin")) {
                    showCustomLoadingDialog();
                    intent = new Intent(CustomerHomePage.this, CustomerProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                } else {
                    Toast.makeText(getApplicationContext(), "You Have To Need Login First", Toast.LENGTH_LONG).show();
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                    alertDialog.setTitle("Sorry !!");
                    alertDialog.setMessage("You Have To Need Login First");


                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);

                    alertDialog.setIcon(R.drawable.warning);

                    alertDialog.setPositiveButton("ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

//                                    SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                    intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                    if (skipdata.equals("SkipLogin")) {
                                        intent.putExtra("Skip", "SkipLogin");
                                    }
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                                }
                            });


                    alertDialog.show();
                }


            }
        });
        getProfile();
        getDiscountImageList();
     /*   final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                runtime_permissions();
                handler.postDelayed(this, 1000);
            }
        };

//Start
        handler.postDelayed(runnable, 1000);
*/

        if (!runtime_permissions()) {

            openGPSSettings();

        }

    }

    private void openGPSSettings() {
        //Get GPS now state (open or closed)
        try {
            int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if (locationMode == 3) {
                enable_buttons();
                //   Toast.makeText(getApplicationContext(),"All is Fine",Toast.LENGTH_LONG).show();

            } else {
                LocationRequest mLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(1 * 1000)
                        .setFastestInterval(1 * 1000);
                LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(mLocationRequest);
                settingsBuilder.setAlwaysShow(true);
                Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                        .checkLocationSettings(settingsBuilder.build());
                result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                        try {
                            LocationSettingsResponse response =
                                    task.getResult(ApiException.class);
                        } catch (ApiException ex) {
                            switch (ex.getStatusCode()) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        ResolvableApiException resolvableApiException =
                                                (ResolvableApiException) ex;
                                        resolvableApiException
                                                .startResolutionForResult(CustomerHomePage.this,
                                                        201);
                                        enable_buttons();
                                    } catch (IntentSender.SendIntentException e) {

                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    }
                });
                //   Toast.makeText(getApplicationContext(),"Set Your Location Method:High Accurecy",Toast.LENGTH_LONG).show();
                //    runtime_permissions();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showdialogforGPS() {
//
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        AlertDialog.Builder alertbox = new AlertDialog.Builder(CustomerHomePage.this);
        alertbox.setIcon(R.drawable.map_logo);
        alertbox.setTitle("Location Method:High Accuracy");
        alertbox.setMessage("Hello! You Have To Change your Location Method as High Accuracy another wise you can't track by our employee. ");
        alertbox.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // finish used for destroyed activity
                //Get GPS now state (open or closed)
                startActivity(new Intent(action));
            }
        });
        alertbox.show();
    }

    public boolean runtime_permissions() {

        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            openGPSSettings();
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }
    }

    private void enable_buttons() {

        Intent i = new Intent(this, SingleShotLocationProvider.class);
        startService(i);
    }

    private void getCategoryList() {
        String catalogname = "Home Cleaning & Repairs";
        try {
            result = new CustomerOrderHelper.GETCustomerCategoryList().execute(catalogname).get();
            if (result != null) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Catalog>>() {
                }.getType();
                categorylist = new Gson().fromJson(result, listType);
                CustomerHomePageAdapter customerCatagoryAdapter = new CustomerHomePageAdapter(CustomerHomePage.this, categorylist, skipdata);
                gridView.setAdapter(customerCatagoryAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void getcatagoryList() {
//        try {
//            result = new CustomerOrderHelper.GETCustomerCategoryList().execute(catalogName).get();
//            if (result != null) {
//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<Catalog>>() {
//                }.getType();
//                categorylist = new Gson().fromJson(result, listType);
//                customerCatagoryAdapter = new CustomerCatagoryAdapter(CustomerHomePage.this, categorylist);
//                recyclerView.setAdapter(customerCatagoryAdapter);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void showCustomLoadingDialog() {

        //..show gif
        viewDialog.showDialog();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
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
                userName.setText(dataProfile.getName().toString());
                userContact.setText(dataProfile.MobileNo);
                imgstr = AppConfig.BASE_URL + dataProfile.getImage().toString();
                Glide.with(this).load(imgstr)
                        .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                        .into(imageView);


                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();
                    dataProfile1.ID = dataProfile.ID;
                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;
                    dataProfile1.MobileNo = dataProfile.MobileNo;
                    dataProfile1.Role = dataProfile.Role;
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* For OnBackPRess in HomePage */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(CustomerHomePage.this);
            alertbox.setIcon(R.drawable.exit);
            alertbox.setTitle("Do You Want To Exit ?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
                    finishAffinity();
                    System.exit(0);

                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button
                    // of Dialog
                }
            });
            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.customer_home_page, menu);


        final MenuItem settingsItem = menu.findItem(R.id.action_profile);


        Glide.with(this).load(imgstr).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new SimpleTarget<Bitmap>(100, 100) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                settingsItem.setIcon(new BitmapDrawable(getResources(), getCroppedBitmap(resource)));
            }
        });

        return true;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            try {

                SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                if (skipdata.equals("SkipLogin")) {
                    intent.putExtra("Skip", "SkipLogin");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;

        }
        if (id == R.id.action_customerHome) {
            intent = new Intent(CustomerHomePage.this, CustomerHomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            return true;
        }
        if (id == R.id.action_settings) {

            return true;
        }
        if (id == R.id.action_profile) {

            if (!skipdata.equals("SkipLogin")) {
                showCustomLoadingDialog();
                Intent i = new Intent(CustomerHomePage.this, CustomerProfileActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            } else {
                Intent i = new Intent(CustomerHomePage.this, CustomerLoginRegActivity.class);
                if (skipdata.equals("SkipLogin")) {
                    i.putExtra("Skip", "SkipLogin");
                }
                startActivity(i);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                //Toast.makeText(getApplicationContext(), "You Have To Need Login First", Toast.LENGTH_LONG).show();
            }


            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //side navigation drwaer started onCLick

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Class framentClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_booking) {
            if (!skipdata.equals("SkipLogin")) {
                showCustomLoadingDialog();
                intent = new Intent(CustomerHomePage.this, MyBokingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                alertDialog.setTitle("Sorry !!");
                alertDialog.setMessage("You Have To Need Login First");


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                alertDialog.setIcon(R.drawable.warning);

                alertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                if (skipdata.equals("SkipLogin")) {
                                    intent.putExtra("Skip", "SkipLogin");
                                }
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                            }
                        });


                alertDialog.show();
            }

        }
       /* else if (id == R.id.nav_review) {
                   getSupportActionBar().setTitle("My Review");
                    fragment = myBookingFragment;
                    framentClass = CustomerReviewFragment.class;

          }*/
        else if (id == R.id.nav_complaint) {
            if (!skipdata.equals("SkipLogin")) {
                showCustomLoadingDialog();
                intent = new Intent(CustomerHomePage.this, CustomerComplaintActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                alertDialog.setTitle("Sorry !!");
                alertDialog.setMessage("You Have To Need Login First");


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                alertDialog.setIcon(R.drawable.warning);

                alertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                if (skipdata.equals("SkipLogin")) {
                                    intent.putExtra("Skip", "SkipLogin");
                                }
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                            }
                        });


                alertDialog.show();
            }

//            ActivityOptions startAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left, R.anim.fui_slide_in_right);
//            startActivity(intent, startAnimation.toBundle());

        } else if (id == R.id.nav_logout) {
            try {

                SharedPrefManager.getInstance(this).logout();
                intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                if (skipdata.equals("SkipLogin")) {
                    intent.putExtra("Skip", "SkipLogin");
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (id == R.id.profile) {
            if (!skipdata.equals("SkipLogin")) {
                showCustomLoadingDialog();
                Intent i = new Intent(CustomerHomePage.this, CustomerProfileActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
            } else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CustomerHomePage.this);
                alertDialog.setTitle("Sorry !!");
                alertDialog.setMessage("You Have To Need Login First");


                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                alertDialog.setIcon(R.drawable.warning);

                alertDialog.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                // SharedPrefManager.getInstance(CustomerHomePage.this).logout();
                                intent = new Intent(getApplicationContext(), CustomerLoginRegActivity.class);
                                if (skipdata.equals("SkipLogin")) {
                                    intent.putExtra("Skip", "SkipLogin");
                                }
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                            }
                        });


                alertDialog.show();
            }

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "https://play.google.com/store/apps/details?id=com.awizom.jihuzur";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

        } else if (id == R.id.nav_send) {

            String phoneNumber = "", message = "";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

        } else if (id == R.id.profileImage) {
            showCustomLoadingDialog();
            Intent imageView = new Intent(CustomerHomePage.this, CustomerProfileActivity.class);
            startActivity(imageView);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

        }
        try {
            fragment = (Fragment) framentClass.newInstance();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClick);
        switch (v.getId()) {


        }

    }

    private void getDiscountImageList() {

        try {
            result = new CustomerOrderHelper.GETDiscountList().execute().get();

            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<DiscountModel>>() {
                }.getType();
                discountModel = new Gson().fromJson(result, listType);

                String imageNames[] = new String[discountModel.size()];
                String discountNames[] = new String[discountModel.size()];
                String dicountAmounts[] = new String[discountModel.size()];
                for (int i = 0; i <= discountModel.size(); i++) {
                    imageNames[i] = discountModel.get(i).getPhoto();
                    discountNames[i] = discountModel.get(i).getDiscountName();
                    dicountAmounts[i] = discountModel.get(i).getDiscount();
                    ExampleFliperAdapter customAdapter = new ExampleFliperAdapter(getApplicationContext(), imageNames, discountNames, dicountAmounts);
                    simpleAdapterViewFlipper.setAdapter(customAdapter);
                    simpleAdapterViewFlipper.setFlipInterval(2500);
                    simpleAdapterViewFlipper.setAutoStart(true);


                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

