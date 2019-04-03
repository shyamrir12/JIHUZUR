package com.example.awizom.jihuzur.AdminActivity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.BuildConfig;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeFragment.EmployeeCurrentOrderFragment;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeFragment.EmployeeHistoryCurrentFragment;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Locationhelper.FetchURL;
import com.example.awizom.jihuzur.Locationhelper.TaskLoadedCallback;
import com.example.awizom.jihuzur.LoginRegistrationActivity.AdminRegistration;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Service.LocationMonitoringService;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.DrawingActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.SettingsActivity;

public class AdminHomePage extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    FirebaseFirestore db;
    LinearLayout mapviewLayout, getdrctlayout;
    String img_str;
    String idmark;
    String namemark;
    String img_strmark;
    String mobnomark;
    private Fragment employeeCurrentOrderFragment, employeeHistoryCurreFragment;
    private FragmentManager fragmentManager;
    String latl, long1;
    String namesForMap;
    String result = "";
    ViewDialog viewDialog;
    Intent intent;
    de.hdodenhof.circleimageview.CircleImageView profileImages;
    TextView userName, identityNo, identityType;
    List<DataProfile> listtype;
    private Fragment fragment = null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String TAG = "LocationActivity";
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };

    List<EmployeeProfileModel> employeeProfileModelList;
    LatLng latLng;
    Button getDirection;
    String empid, name, mobno;
    private GoogleMap mGoogleMap;
    de.hdodenhof.circleimageview.CircleImageView employeeImage;
    ImageView call;
    TextView customerDetails;
    private boolean mAlreadyStartedService = false;
    private TextView mMsgView, distancefor;
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> empMobile = new ArrayList<>();
    private ArrayList<String> empName = new ArrayList<>();
    private String[] empNameList, empLat, empLong, employeeid, fireid, firelat, firelong, employeemobile, employeeimage;
    private String priceID = "", priceIDs = "", selectedEmpId;
    private String priceIds;
    private MarkerOptions place1, mylocation;
    private MarkerOptions place2;
    private Polyline currentPolyline;
    private Fragment searchFragment, myBookingFragment, helpCenterFragment, catalogFragment;
    ImageView mapRefresh;
    final Marker[] marker = new Marker[1];
    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        //bottom navigation Button Onclick
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class framentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_search:
/*                  getSupportActionBar().setTitle("Catalog");
                      fragment = catalogFragment;
                      framentClass = AdminHomePage.class;*/
                    intent = new Intent(AdminHomePage.this, AdminHomePage.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    break;

                case R.id.navigation_master:
                    showCustomLoadingDialog();
                    intent = new Intent(AdminHomePage.this, AdminCategoryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    break;

                case R.id.navigation_helpCenter:
                 /*   intent = new Intent(AdminHomePage.this, HelpCenterActivity.class);
                    startActivity(intent);*/
                    generateRandomNumber();

                  //  overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//                    getSupportActionBar().setTitle("Help Center");
//                    fragment = helpCenterFragment;
//                    framentClass = HelpCenterFragment.class;
                    break;
            }
            try {
                fragment = (Fragment) framentClass.newInstance();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
                setTitle("");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    };

    int range = 9;  // to generate a single number with this range, by default its 0..9
    int length = 4; // by default length is 4

    public int generateRandomNumber() {
        int randomNumber;

        SecureRandom secureRandom = new SecureRandom();
        String s = "";
        for (int i = 0; i < length; i++) {
            int number = secureRandom.nextInt(range);
            if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                i = -1;
                continue;
            }
            s = s + number;
        }


        randomNumber = Integer.parseInt(s);
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification noti = new Notification.Builder(this)
                .setContentTitle("JiHUzzur Otp for Order")
                .setContentText(String.valueOf(randomNumber)).setSmallIcon(R.drawable.jihuzurapplogo)
                .setContentIntent(pendingIntent)
              .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(randomNumber, noti);


        Toast.makeText(getApplicationContext(),randomNumber+" number",Toast.LENGTH_LONG).show();

        return randomNumber;
    }


    /* For OnBackPRess in HomePage */
    @SuppressLint("ResourceType")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(AdminHomePage.this);
            alertbox.setIcon(R.drawable.exit);
            alertbox.setIconAttribute(90);
            alertbox.setTitle("Do You Want To Exit Programme?");
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

    //layout declaration
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mapviewLayout = (LinearLayout) findViewById(R.id.mapview);
        getdrctlayout = (LinearLayout) findViewById(R.id.getdrct);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //      Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        } else {
            showGPSDisabledAlertToUser();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        employeeImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.employee_dp);
        call = (ImageView) findViewById(R.id.call);
        viewDialog = new ViewDialog(this);
        customerDetails = (TextView) findViewById(R.id.customerdetails);
        fragmentManager = getSupportFragmentManager();//Get Fragment Manager

        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);

        setSupportActionBar(toolbar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        db = FirebaseFirestore.getInstance();
        employeeCurrentOrderFragment = new EmployeeCurrentOrderFragment();
        mapRefresh = (ImageView) findViewById(R.id.getRefresh);
        mapRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    initView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mMsgView = (TextView) findViewById(R.id.msgView);
        distancefor = (TextView) findViewById(R.id.distance);
        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
                        if (latitude != null && longitude != null) {
                            int height = 100;
                            int width = 100;
                            BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.greenmappin);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            mylocation = new MarkerOptions().position(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).title("Location 1").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                            if (marker[0] != null) {
                                marker[0].remove();
                                marker[0] = mGoogleMap.addMarker(mylocation);
                            } else {
                                //This is Employee Location
                                try {
                                    marker[0] = mGoogleMap.addMarker(mylocation);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));

        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        profileImages = headerview.findViewById(R.id.profileImage);
        userName = headerview.findViewById(R.id.profileName);

//        SharedPrefManager.getInstance(this).getUser().;
        //  userName.setText(SharedPrefManager.getInstance(this).getUser().getName());
        userName.setOnClickListener(this);
        img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();
        {
            try {
                if (SharedPrefManager.getInstance(this).getUser().getImage() == null) {
                    profileImages.setImageResource(R.drawable.jihuzurblanklogo);
                    //     Glide.with(mCtx).load("http://192.168.1.105:7096/Images/Category/1.png").into(holder.categoryImage);
                } else {
//                    Glide.with(this).load(img_str).into(profileImages);
                    Glide.with(AdminHomePage.this)
                            .load(img_str)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .placeholder(R.drawable.jihuzurblanklogo)
                            .into(profileImages);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        profileImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, DrawingActivity.class);
                startActivity(intent);
            }
        });
        getProfile();
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void initView() {
        priceID = getIntent().getStringExtra("PricingID");
        priceIDs = String.valueOf(getIntent().getIntExtra("PricingIDS", 0));
        employeeProfileGet();
        getDirection = findViewById(R.id.btnGetDirection);
      /*  getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(AdminHomePage.this).execute(getUrl(mylocation.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });
*/
        place1 = new MarkerOptions().position(new LatLng(21.2379468, 81.6336833)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(21.2120677, 81.3732849)).title("Location 2");
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
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

                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();
                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;
//                        SharedPrefManager.getInstance(this).userLogin(dataProfile1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mGoogleMap.setMinZoomPreference(11);

                    mGoogleMap.setMaxZoomPreference(2000);
                    return false;
                }
            };
    public GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    mGoogleMap.setMinZoomPreference(12);
                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));
                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.BLUE);
                    circleOptions.strokeWidth(6);
                    mGoogleMap.addCircle(circleOptions);
                }
            };

    public static Bitmap createCustomMarker(final Context context, String resource, final String _name, String mobno, String id, GoogleMap googleMap) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        final de.hdodenhof.circleimageview.CircleImageView markerImage = (de.hdodenhof.circleimageview.CircleImageView) marker.findViewById(R.id.user_dp);
        final RelativeLayout relativeLayout = (RelativeLayout) marker.findViewById(R.id.custom_marker_view);
        String img_strs = AppConfig.BASE_URL + resource;
        /*        markerImage.setImageResource(resource);*/

        final TextView txt_name = (TextView) marker.findViewById(R.id.name);
        TextView text_mob = (TextView) marker.findViewById(R.id.mobno);
        TextView txt_id = (TextView) marker.findViewById(R.id.empid);
      /*  ImageView orderHIstory=(ImageView)marker.findViewById(R.id.horder);
        final ImageView orderCurrent=(ImageView)marker.findViewById(R.id.codrer);*/
        final String ide = txt_id.getText().toString();

      /*  orderCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==orderCurrent.getId())
                {
                    Toast.makeText(context,"Click",Toast.LENGTH_LONG).show();
                }
            }
        });*/
        text_mob.setText(mobno);
        txt_name.setText(_name);
        markerImage.setImageURI(Uri.parse(img_strs));

      /*  if (resource == null) {
            markerImage.setImageResource(R.drawable.jihuzurblanklogo);
            *//*                 Glide.with(context).load("http://192.168.1.103:7096/Images/Category/1.png").into(markerImage);*//*
        } else {

            markerImage.setImageURI(Uri.parse(img_strs));
                //Glide.with(marker.getContext()).load(img_strs).into(markerImage);

        }*/
        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + _name, Toast.LENGTH_SHORT).show();
            }
        });
        txt_id.setText(id);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }


    private void employeeProfileGet() {
        try {
            result = new EmployeeOrderHelper.GetEmployeeProfileForShow().execute().get();
            Type listType = new TypeToken<List<EmployeeProfileModel>>() {
            }.getType();
            employeeProfileModelList = new Gson().fromJson(result.toString(), listType);

            for (int i = 0; i < employeeProfileModelList.size(); i++) {
                db.collection("Profile")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        try {
                                            fireid = new String[employeeProfileModelList.size()];
                                            firelat = new String[employeeProfileModelList.size()];
                                            firelong = new String[employeeProfileModelList.size()];
                                            for (int i = 0; i < task.getResult().size(); i++) {
                                                try {
                                                    fireid[i] = task.getResult().getDocuments().get(i).getId();
                                                    firelong[i] = String.valueOf(task.getResult().getDocuments().get(i).get("long"));
                                                    firelat[i] = String.valueOf(task.getResult().getDocuments().get(i).get("lat"));
                                                    latLng = new LatLng(Double.valueOf(String.valueOf(task.getResult().getDocuments().get(i).get("lat"))),
                                                            Double.valueOf(String.valueOf(task.getResult().getDocuments().get(i).get("long"))));

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d(TAG, document.getId() + " => " + document.getData());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
            empNameList = new String[employeeProfileModelList.size()];
            empLat = new String[employeeProfileModelList.size()];
            empLong = new String[employeeProfileModelList.size()];
            employeeid = new String[employeeProfileModelList.size()];
            employeemobile = new String[employeeProfileModelList.size()];
            employeeimage = new String[employeeProfileModelList.size()];
            for (int i = 0; i < employeeProfileModelList.size(); i++) {
                empNameList[i] = String.valueOf(employeeProfileModelList.get(i).getName());
                empLat[i] = String.valueOf(employeeProfileModelList.get(i).getLat());
                empLong[i] = String.valueOf(employeeProfileModelList.get(i).getLong());
                employeeid[i] = String.valueOf(employeeProfileModelList.get(i).getID());
                employeemobile[i] = String.valueOf(employeeProfileModelList.get(i).getMobileNo());
                employeeimage[i] = String.valueOf(employeeProfileModelList.get(i).getImage());
                empMobile.add(employeeProfileModelList.get(i).getMobileNo());
                empName.add(employeeProfileModelList.get(i).getName());
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Fetches data from url passed
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
      /*  MapStyleOptions mapStyleOptions= MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json);
        mGoogleMap.setMapStyle(mapStyleOptions);*/
        Log.d("mylog", "Added Markers");
        final Marker[] allMarkers = new Marker[employeeProfileModelList.size()];
        for (int i = 0; i < employeeProfileModelList.size(); i++) {

            String ids = employeeProfileModelList.get(i).getID();

            if (googleMap != null) {
            }
            selectedEmpId = employeeProfileModelList.get(i).getID();
        }
        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                for (int i = 0; i < employeeProfileModelList.size(); i++) {

                    //                    LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
                    empid = employeeProfileModelList.get(i).getID();
                    empNameList[i] = String.valueOf(employeeProfileModelList.get(i).getName());
                    empLat[i] = String.valueOf(employeeProfileModelList.get(i).getLat());
                    empLong[i] = String.valueOf(employeeProfileModelList.get(i).getLong());
                    employeeid[i] = String.valueOf(employeeProfileModelList.get(i).getID());
                    employeemobile[i] = String.valueOf(employeeProfileModelList.get(i).getMobileNo());
                    employeeimage[i] = String.valueOf(employeeProfileModelList.get(i).getImage());
                    empMobile.add(employeeProfileModelList.get(i).getMobileNo());
                    empName.add(employeeProfileModelList.get(i).getName());

                    try {

                          /*  name = dataProfileEmployee.getName();
                            mobno = dataProfileEmployee.getMobileNo();
                            img_str = dataProfileEmployee.getImage();*/

                        final int finalI = i;
                        final int finalI1 = i;
                        final int finalI2 = i;
                        db.collection("Profile").document(employeeid[i]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                latLng = new LatLng(Double.valueOf(String.valueOf(task.getResult().get("lat"))),
                                        Double.valueOf(String.valueOf(task.getResult().get("long"))));
                                int height = 100;
                                int width = 100;
                                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_logo);
                                Bitmap b = bitmapdraw.getBitmap();
                                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                                mGoogleMap.addMarker(new MarkerOptions().position(latLng).
                                        icon(BitmapDescriptorFactory.fromBitmap(smallMarker))).setTitle(empNameList[finalI] + "," + employeeid[finalI1] + "," + employeeimage[finalI2] + "," + employeemobile[finalI2]);
                            }

                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            mapviewLayout.setVisibility(View.VISIBLE);
                            mapviewLayout.setAlpha(0.0f);
                            mapviewLayout
                                    .animate()
                                    .setDuration(5)
                                    .alpha(1.0f)
                                    .setListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            mapviewLayout.animate().setListener(null);
                                        }
                                    });
                            getdrctlayout.setVisibility(View.VISIBLE);
                            latl = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[0];
                            long1 = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[1].split(Pattern.quote(")"))[0];
                            place2 = new MarkerOptions().position(new LatLng(Double.valueOf(latl), Double.valueOf(long1))).title("Location 1");
                            new FetchURL(AdminHomePage.this).execute(getUrl(mylocation.getPosition(), place2.getPosition(), "driving"), "driving");

                            try {
                                idmark = marker.getTitle().split(",")[1];
                                namemark = marker.getTitle().split(",")[0];
                                img_strmark = marker.getTitle().split(",")[2];
                                mobnomark = marker.getTitle().split(",")[3];
                                String employeeprofimage = AppConfig.BASE_URL + img_strmark;
                                customerDetails.setText(namemark);
                                if (img_strmark != null) {
                                    Glide.with(AdminHomePage.this)
                                            .load(employeeprofimage)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .placeholder(R.drawable.jihuzurblanklogo)
                                            .into(employeeImage);
                                    //   Glide.with(AdminHomePage.this).load(employeeprofimage).into(employeeImage);
                                } else {
                                    employeeImage.setImageResource(R.drawable.jihuzurblanklogo);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return true;
                        }
                    });
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobnomark));
                            if (ActivityCompat.checkSelfPermission(AdminHomePage.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(intent);
                        }
                    });
                    try {
                        getDirection.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                   /*   Double latitud=latLng.latitude;
                      Double longitud=latLng.longitude;*/
                                String label = "Route for " + namemark;
                                String uriBegin = "geo:" + latl + "," + long1;
                                String query = latl + "," + long1 + "(" + label + ")";
                                String encodedQuery = Uri.encode(query);
                                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                                Uri uri = Uri.parse(uriString);
                                try {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Update Your Google Map", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Toast.makeText(getApplicationContext(), "Please Select Employee First", Toast.LENGTH_SHORT).show();
                    }
                    mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                        @Override
                        public void onMapLongClick(LatLng latLng) {
                            if (idmark == null) {
                                Toast.makeText(getApplicationContext(), "Please Click Marker First!", Toast.LENGTH_LONG).show();
                            } else {
                                android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(AdminHomePage.this);
                                LayoutInflater inflater = LayoutInflater.from(AdminHomePage.this);
                                final View dialogView = inflater.inflate(R.layout.dialog_reviewemployee, null);
                                dialogBuilder.setView(dialogView);
                                final String ide = idmark;
                                String name = namemark;
                                String img_str = img_strmark;
                                RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.rating);
                               /* final RecyclerView orderNew = (RecyclerView) dialogView.findViewById(R.id.orderNew);
                                orderNew.setHasFixedSize(true);
                                orderNew.setLayoutManager(new LinearLayoutManager(AdminHomePage.this));
                               */
                                ImageView oderrun = (ImageView) dialogView.findViewById(R.id.orderRun);
                                ImageView orderHist = (ImageView) dialogView.findViewById(R.id.orderHist);                               /* getRatingForEmployee(ide, ratingBar);
                                getMyOrderRunning(orderNew, ide, AdminHomePage.this);*/
                                de.hdodenhof.circleimageview.CircleImageView profileimage = (de.hdodenhof.circleimageview.CircleImageView) dialogView.findViewById(R.id.profileImage);
                                if (img_str.equals("null")) {
                                    profileimage.setImageResource(R.drawable.jihuzurblanklogo);
                                    //  Glide.with(AdminHomePage.this).load("http://192.168.1.103:7096/Images/Category/1.png").into(markerImage);
                                } else {
                                    Glide.with(AdminHomePage.this).load(AppConfig.BASE_URL + img_str).into(profileimage);
                                }
                                dialogBuilder.setTitle("Mr/Ms. " + name.toString());
                                dialogBuilder.setNegativeButton("Close",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                final android.support.v7.app.AlertDialog b = dialogBuilder.create();
                                b.show();
                                oderrun.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        b.cancel();
                                        getSupportActionBar().setTitle(namemark + "'s" + " " + "Current Order");
                                        Fragment employeeCurrentOrderFragment = new EmployeeCurrentOrderFragment();//Get Fragment Instance
                                        Bundle data = new Bundle();//Use bundle to pass data
                                        data.putString("EmployeeID", ide);//put string, int, etc in bundle with a key value
                                        employeeCurrentOrderFragment.setArguments(data);//Finally set argument bundle to fragment
                                        fragmentManager.beginTransaction().replace(R.id.home_container, employeeCurrentOrderFragment).commit();//now replace the argument fragment
                                    }
                                });
                                orderHist.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        b.cancel();
                                        getSupportActionBar().setTitle(namemark + "'s" + " " + "Order History");
                                        Fragment employeeHistoryCurrentFragment = new EmployeeHistoryCurrentFragment();//Get Fragment Instance
                                        Bundle data = new Bundle();//Use bundle to pass data
                                        data.putString("EmployeeID", ide);//put string, int, etc in bundle with a key value
                                        employeeHistoryCurrentFragment.setArguments(data);//Finally set argument bundle to fragment
                                        fragmentManager.beginTransaction().replace(R.id.home_container, employeeHistoryCurrentFragment).commit();//now replace the argument fragment
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        mGoogleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mGoogleMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        LatLng latLngs = new LatLng(21.2514, 81.6296);
        builder.include(latLngs);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLngs)
                .zoom(11)
                .bearing(90)
                .tilt(30)
                .build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        /* set zoom position by this value */
        mGoogleMap.getMaxZoomLevel();
        mGoogleMap.getMinZoomLevel();
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_api_key);
        return url;
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mGoogleMap != null) {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String emp = empid;
        String latl = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[0];
        String longl = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[1].split(Pattern.quote(")"))[0];
        place2 = new MarkerOptions().position(new LatLng(Double.valueOf(latl), Double.valueOf(longl))).title("Location 1");
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.redpin);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(latl),
                Double.valueOf(longl))).title("Location 1").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        Log.i(TAG, "marker arg0 = " + marker);
        return false;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
        String distances = values[1].toString();
        String duration = values[2].toString();
        distancefor.setText(distances + "," + duration);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Intent serviceIntent = new Intent(this, LocationMonitoringNotificationService.class);
        // serviceIntent.putExtra("inputExtra", "my msg");
        // ContextCompat.startForegroundService(this, serviceIntent);
        startStep1();
    }

    /**
     * Step 1: Check Google Play services
     */
    private void startStep1() {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable()) {

            //Passing null to indicate that it is executing for the first time.
            startStep2(null);

        } else {
            Toast.makeText(getApplicationContext(), R.string.no_google_playservice_available, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Step 2: Check & Prompt Internet connection
     */
    private Boolean startStep2(DialogInterface dialog) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            promptInternetConnect();
            return false;
        }

        if (dialog != null) {
            dialog.dismiss();
        }
        //Yes there is active internet connection. Next check Location is granted by user or not.

        if (checkPermissions()) { //Yes permissions are granted by the user. Go to the next step.
            startStep3();
        } else {  //No user has not granted the permissions yet. Request now.
            requestPermissions();
        }
        return true;
    }

    /**
     * Show A Dialog with button to refresh the internet state.
     */
    private void promptInternetConnect() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AdminHomePage.this);
        builder.setTitle(R.string.title_alert_no_intenet);
        builder.setMessage(R.string.msg_alert_no_internet);
        String positiveText = getString(R.string.btn_label_refresh);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Block the Application Execution until user grants the permissions
                        if (startStep2(dialog)) {
                            //Now make sure about location permission.
                            if (checkPermissions()) {
                                //Step 2: Start the Location Monitor Service
                                //Everything is there to start the service.
                                startStep3();
                            } else if (!checkPermissions()) {
                                requestPermissions();
                            }
                        }
                    }
                });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    private void startStep3() {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService && mMsgView != null) {
            mMsgView.setText(R.string.msg_location_service_started);
            //Start location sharing service to app server.........
            Intent intent = new Intent(this, LocationMonitoringService.class);
            startService(intent);
            mAlreadyStartedService = true;
            //Ends................................................
        }
    }

    /**
     * Return the availability of GooglePlayServices
     */
    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(this, status, 2404).show();
            }
            return false;
        }
        return true;
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState1 = ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Start permissions requests.
     */
    private void requestPermissions() {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AdminHomePage.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(AdminHomePage.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "Permission granted, updates requested, starting location updates");
                startStep3();

            } else {
                // Permission denied.

                // Notify the img_user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the img_user for permission (device policy or "Never ask
                // again" prompts). Therefore, a img_user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation,
                        R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    @Override
    public void onDestroy() {


        //Stop location sharing service to app server.........

        stopService(new Intent(this, LocationMonitoringService.class));
        mAlreadyStartedService = false;
        //Ends................................................


        super.onDestroy();
    }

    //fumctionalities for side navigation drawer
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
        getMenuInflater().inflate(R.menu.admin_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
//            FirebaseAuth fAuth = FirebaseAuth.getInstance();
//            fAuth.signOut();
            return true;
        }
        if (id == R.id.action_customerHome) {
            Intent i = new Intent(this, CustomerHomePage.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(AdminHomePage.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //side navigation drwaer started onCLick
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_employee) {
            // Handle the camera action
            showCustomLoadingDialog();
            intent = new Intent(AdminHomePage.this, AdminMyEmployeeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

        } else if (id == R.id.nav_master) {
            showCustomLoadingDialog();
            intent = new Intent(AdminHomePage.this, AdminCategoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_orderPhoto) {
            showCustomLoadingDialog();
            intent = new Intent(AdminHomePage.this, AdminEmployeeOrderPhoto.class);
            startActivity(intent);
        }

        /* else if (id == R.id.nav_catalogName) {
            intent = new Intent(AdminHomePage.this, AdminCatalogActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_catalogpricing) {
            intent = new Intent(AdminHomePage.this, AdminPricingActivity.class);
            startActivity(intent);
        }*/
        else if (id == R.id.nav_complaintReply) {
            showCustomLoadingDialog();
            intent = new Intent(AdminHomePage.this, AdminComplaintReply.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_ReviewReply) {
//            intent = new Intent(AdminHomePage.this, AdminReviewReply.class);
//            startActivity(intent);
//        }
        else if (id == R.id.nav_discount) {
            showCustomLoadingDialog();
            intent = new Intent(AdminHomePage.this, AdminDiscountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), AdminRegistration.class);
            startActivity(login);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }
}