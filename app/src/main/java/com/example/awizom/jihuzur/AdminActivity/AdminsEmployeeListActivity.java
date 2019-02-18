package com.example.awizom.jihuzur.AdminActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.BuildConfig;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Locationhelper.DataParser;
import com.example.awizom.jihuzur.Locationhelper.FetchURL;
import com.example.awizom.jihuzur.Locationhelper.TaskLoadedCallback;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Service.LocationMonitoringService;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminsEmployeeListActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private GoogleMap mGoogleMap;
    private DataParser dataParser;
    private String data = "", distance = "";

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;


    private boolean mAlreadyStartedService = false;
    private TextView mMsgView, distancefor;
    String result = "";
    private static final String TAG = "LocationActivity";


    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> empID = new ArrayList<>();
    private ArrayList<String> empMobile = new ArrayList<>();
    private ArrayList<String> empName = new ArrayList<>();
    List<EmployeeProfileModel> employeeProfileModelList;
    private String[] empNameList, empLat, empLong,employeeid;
    LatLng latLng;
    Intent intent;
    private String priceID = "", priceIDs = "", selectedEmpId;
    private String priceIds;
    private MarkerOptions place1, mylocation, targetlocation;
    private MarkerOptions place2;
    Button getDirection;
    String empid,name,mobno,img_str;
    private Polyline currentPolyline;

    private      String names ,  mobnos , img_strs ,empids;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admins_employee_list);
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
                            BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.greenmappin);
                            Bitmap b=bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                            mylocation = new MarkerOptions().position(new LatLng(Double.valueOf(latitude), Double.valueOf(longitude))).title("Location 1").icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                            mMsgView.setText(getString(R.string.msg_location_service_started) + "\n Latitude : " + latitude + "\n Longitude: " + longitude);
                        }
                    }
                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST)
        );

        InitView();
    }


    public void PutDistance(String distance) {

        distancefor.setText(distance);

    }

    public void InitView() {


        priceID = getIntent().getStringExtra("PricingID");
        priceIDs = String.valueOf(getIntent().getIntExtra("PricingIDS", 0));

//        getMapvalue();
        //latlngs.add(new LatLng(latitude, longitude));
        // latlngs.add(new LatLng(latitude1, longitude1));

        employeeProfileGet();

        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(AdminsEmployeeListActivity.this).execute(getUrl(mylocation.getPosition(), place2.getPosition(), "driving"), "driving");

            }


        });

        place1 = new MarkerOptions().position(new LatLng(21.2379468, 81.6336833)).title("Location 1");
        place2 = new MarkerOptions().position(new LatLng(21.2120677, 81.3732849)).title("Location 2");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);
    }


    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };


    private void employeeProfileGet() {

        try {

            result = new EmployeeOrderHelper.GetEmployeeProfileForShow().execute().get();

            Type listType = new TypeToken<List<EmployeeProfileModel>>() {
            }.getType();
            employeeProfileModelList = new Gson().fromJson(result.toString(), listType);

            empNameList = new String[employeeProfileModelList.size()];
            empLat = new String[employeeProfileModelList.size()];
            empLong = new String[employeeProfileModelList.size()];
            employeeid=new String[employeeProfileModelList.size()];
            for (int i = 0; i < employeeProfileModelList.size(); i++) {
                empNameList[i] = String.valueOf(employeeProfileModelList.get(i).getName());
                empLat[i] = String.valueOf(employeeProfileModelList.get(i).getLat());
                empLong[i] = String.valueOf(employeeProfileModelList.get(i).getLong());
                latlngs.add(new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
                        Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong()))));

                employeeid[i]=String.valueOf(employeeProfileModelList.get(i).getID());
                empMobile.add(employeeProfileModelList.get(i).getMobileNo());
                empName.add(employeeProfileModelList.get(i).getName());


            }



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;

        Log.d("mylog", "Added Markers");
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.addAll(latlngs);
//        polylineOptions
//                .width(4)
//                .color(Color.BLACK);

        Marker[] allMarkers = new Marker[employeeProfileModelList.size()];

        for (int i = 0; i < employeeProfileModelList.size(); i++) {
            latLng = new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
                    Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong())));
             String ids=employeeProfileModelList.get(i).getID();

            if (googleMap != null) {




//                allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(latLng)
//                        .title(employeeProfileModelList.get(i).getName() + " " + "+91" + employeeProfileModelList.get(i).getMobileNo())
//                        .snippet(employeeProfileModelList.get(i).getID()));
//                googleMap.addPolyline(polylineOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                //*Remove google red marker





            }

            selectedEmpId = employeeProfileModelList.get(i).getID();

        }


        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                for (int i = 0; i < employeeProfileModelList.size(); i++) {

                    latLng = new LatLng(Double.parseDouble(employeeProfileModelList.get(i).getLat()), Double.parseDouble(employeeProfileModelList.get(i).getLong()));
                    name = employeeProfileModelList.get(i).getName();
                    mobno = employeeProfileModelList.get(i).getMobileNo();
                    img_str = employeeProfileModelList.get(i).getImage();
                    empid = employeeProfileModelList.get(i).getID();

                    //                    LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);


                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(
                                    createCustomMarker(AdminsEmployeeListActivity.this, img_str, name, mobno, empid,mGoogleMap)))).setTitle(name);


                    //LatLngBound will cover all your marker on Google Maps
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(latLng);
                    LatLngBounds bounds = builder.build();


                    mGoogleMap.addMarker(place1);
                    mGoogleMap.addMarker(place1);
                    mGoogleMap.addMarker(mylocation);
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                    mGoogleMap.moveCamera(cu);
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
                }


            }
            });


        mGoogleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mGoogleMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setMinZoomPreference(11);


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

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception download url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed


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


    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(21.33, 81.50);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }


    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mGoogleMap.setMinZoomPreference(11);
                    mGoogleMap.setMaxZoomPreference(2000);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
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


    public static Bitmap createCustomMarker(final Context context, String resource, final String _name, String mobno,String id,GoogleMap googleMap) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        RelativeLayout relativeLayout=(RelativeLayout)marker.findViewById(R.id.custom_marker_view);


//        markerImage.setImageResource(resource);
        if (resource == null)

        {

        markerImage.setImageResource(R.drawable.jihuzurblanklogo);
//                 Glide.with(context).load("http://192.168.1.103:7096/Images/Category/1.png").into(markerImage);
        }
        else
            {
            Glide.with(context).load(resource).into(markerImage);
        }
        markerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ""+ _name , Toast.LENGTH_SHORT).show();
            }
        });

        final TextView txt_name = (TextView) marker.findViewById(R.id.name);
        TextView text_mob = (TextView) marker.findViewById(R.id.mobno);
        TextView txt_id = (TextView) marker.findViewById(R.id.empid);
        text_mob.setText(mobno);
        txt_name.setText(_name);
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




    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

            android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            final View dialogView = inflater.inflate(R.layout.dialog_reviewemployee, null);
            dialogBuilder.setView(dialogView);

            final Button buttonAddCategory = (Button) dialogView.findViewById(R.id.buttonAddCategory);
            final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

            dialogBuilder.setTitle(txt_name.getText().toString());
            final android.support.v7.app.AlertDialog b = dialogBuilder.create();
            b.show();


            buttonAddCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();

                    byte[] image = stream.toByteArray();
                    System.out.println("byte array:" + image);

                    String img_str = Base64.encodeToString(image, 0);


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
            return true;
        }
    });





                return bitmap;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String emp=empid;

        String latl = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[0];
        String longl = marker.getPosition().toString().split(Pattern.quote("("))[1].split(",")[1].split(Pattern.quote(")"))[0];

        place2 = new MarkerOptions().position(new LatLng(Double.valueOf(latl), Double.valueOf(longl))).title("Location 1");

        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.redpin);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);


        mGoogleMap.addMarker( new MarkerOptions().position(new LatLng(Double.valueOf(latl),
                Double.valueOf(longl))).title("Location 1").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)) );

        Log.i(TAG, "marker arg0 = " + marker);
//        if (!marker.equals(null)) {
//            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
//            alertbox.setMessage("Do you want to place this order");
//            alertbox.setTitle("Order");
//            alertbox.setIcon(R.drawable.ic_dashboard_black_24dp);
//
//            alertbox.setNeutralButton("Yes",
//                    new DialogInterface.OnClickListener() {
//                        Class fragmentClass = null;
//
//                        public void onClick(DialogInterface arg0,
//                                            int arg1) {
//
//                            postOderCreate();
//                        }
//
//
//                    });
//            alertbox.setPositiveButton("No", null);
//
//            alertbox.show();
//        }
        return false;
    }

    private void postOderCreate() {

//            Date date = new Date();
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());

        String customerid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
        String empId = selectedEmpId;
        String orderDate = String.valueOf(date);
        String catalogId = String.valueOf(2);
        if (priceID.equals(null)) {
            priceIds = priceIDs;
        } else {
            priceIds = priceID;
        }
        try {
            result = new CustomerOrderHelper.OrderPost().execute(customerid, empId, orderDate, catalogId, priceIds).get();
            if (!result.isEmpty()) {
                intent = new Intent(this, MyBokingsActivity.class);
                startActivity(intent);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
        Toast.makeText(this, jsonbody.Message, Toast.LENGTH_SHORT).show();

        if (!result.equals(null)) {


        }


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
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(AdminsEmployeeListActivity.this);
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
                            ActivityCompat.requestPermissions(AdminsEmployeeListActivity.this,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(AdminsEmployeeListActivity.this,
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


}