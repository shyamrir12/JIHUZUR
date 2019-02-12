package com.example.awizom.jihuzur.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.ClusterMarker;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.MyClusterManagerRendrer;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap mGoogleMap;
    Polyline line;
    PolylineOptions polylineOptions;
    ArrayList markerPoints = new ArrayList();

    String lat = "", longitud = "", result = "", empNames = "";
    private static final String TAG = "LocationActivity";
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();

    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private ArrayList<String> empID = new ArrayList<>();
    private ArrayList<String> empMobile = new ArrayList<>();
    private ArrayList<String> empName = new ArrayList<>();
    private ClusterManager<ClusterMarker> mClusterManager;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private MyClusterManagerRendrer mClusterManagerRenderer;
    List<EmployeeProfileModel> employeeProfileModelList;


    private EmployeeProfileModel employeeProfileModel;
    private String[] empNameList, empLat, empLong;
    LatLng latLng;

    Double latitude, latitude1;
    Double longitude, longitude1;
    Intent intent;
    private String priceID="",empId="",priceIDs="",selectedEmpId;
    private String priceIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        InitView();
    }

    public void InitView() {
        priceID = getIntent().getStringExtra("PricingID");
        priceIDs = String.valueOf(getIntent().getIntExtra("PricingIDS",0));
        getMapvalue();
        //latlngs.add(new LatLng(latitude, longitude));
        // latlngs.add(new LatLng(latitude1, longitude1));

        employeeProfileGet();


    }


// add marker to Map

    private void employeeProfileGet() {

        try {

            result = new EmployeeOrderHelper.GetEmployeeProfileForShow().execute().get();

            Type listType = new TypeToken<List<EmployeeProfileModel>>() {
            }.getType();
            employeeProfileModelList = new Gson().fromJson(result.toString(), listType);

            empNameList = new String[employeeProfileModelList.size()];
            empLat = new String[employeeProfileModelList.size()];
            empLong = new String[employeeProfileModelList.size()];
            for (int i = 0; i < employeeProfileModelList.size(); i++) {
                empNameList[i] = String.valueOf(employeeProfileModelList.get(i).getName());
                empLat[i] = String.valueOf(employeeProfileModelList.get(i).getLat());
                empLong[i] = String.valueOf(employeeProfileModelList.get(i).getLong());
                latlngs.add(new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
                        Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong()))));

                empID.add(employeeProfileModelList.get(i).getID());
                empMobile.add(employeeProfileModelList.get(i).getMobileNo());
                empName.add(employeeProfileModelList.get(i).getName());


            }

            getMapvalue();


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void getMapvalue() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latlngs);
        polylineOptions
                .width(5)
                .color(Color.YELLOW);

        Marker[] allMarkers = new Marker[employeeProfileModelList.size()];

        for (int i = 0; i < employeeProfileModelList.size(); i++) {
             latLng = new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
                    Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong())));
            if (googleMap != null) {
                googleMap.setOnMarkerClickListener(this);
                allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(employeeProfileModelList.get(i).getName() + " " + "+91" + employeeProfileModelList.get(i).getMobileNo())
                        .snippet(employeeProfileModelList.get(i).getID()));
                googleMap.addPolyline(polylineOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

               //*Remove google red marker
                googleMap.clear();

//              googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.massage)).anchor(0.5f, 1));

//              String snippet = "This is you";
//                int avatar = R.drawable.massage;
//
//                ClusterMarker newClusterMarker = new ClusterMarker(new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
//                        Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong()))),
//                        employeeProfileModelList.get(i).getName(),
//                        snippet, avatar);
//
//                mClusterManager.addItem(newClusterMarker);
//                mClusterMarkers.add(newClusterMarker);
                selectedEmpId = employeeProfileModelList.get(i).getID();


            }


//        googleMap.setOnMarkerClickListener(this);
//        for (LatLng point : latlngs) {
//
//            mMap.addMarker(new MarkerOptions().position(point)
//                    .title(String.valueOf(empID))
//                    .snippet(String.valueOf(empMobile))
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//            mMap.addPolyline(polylineOptions);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(point));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

//        }


        }


        mGoogleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

           for(int i=0; i<employeeProfileModelList.size();i++)
           {

                     latLng=new LatLng(Double.parseDouble(employeeProfileModelList.get(i).getLat()),Double.parseDouble(employeeProfileModelList.get(i).getLong()));
                     String name=employeeProfileModelList.get(i).getName();
                     String img_str=employeeProfileModelList.get(i).getImage();

                     //                    LatLng customMarkerLocationOne = new LatLng(28.583911, 77.319116);
//                    LatLng customMarkerLocationTwo = new LatLng(28.583078, 77.313744);
//                    LatLng customMarkerLocationThree = new LatLng(28.580903, 77.317408);
//                    LatLng customMarkerLocationFour = new LatLng(28.580108, 77.315271);


                    mGoogleMap.addMarker(new MarkerOptions().position(latLng).
                            icon(BitmapDescriptorFactory.fromBitmap(
                                    createCustomMarker(UserLocationActivity.this,R.drawable.appliance_repair, name)))).setTitle(name);
//                    mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationTwo).
//                            icon(BitmapDescriptorFactory.fromBitmap(
//                                    createCustomMarker(UserLocationActivity.this, R.drawable.appliance_repair, "Narender")))).setTitle("Hotel Nirulas Noida");
//
//                    mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationThree).
//                            icon(BitmapDescriptorFactory.fromBitmap(
//                                    createCustomMarker(UserLocationActivity.this, R.drawable.massage, "Neha")))).setTitle("Acha Khao Acha Khilao");
//                    mGoogleMap.addMarker(new MarkerOptions().position(customMarkerLocationFour).
//                            icon(BitmapDescriptorFactory.fromBitmap(
//                                    createCustomMarker(UserLocationActivity.this, R.drawable.appliance_repair, "Nupur")))).setTitle("Subway Sector 16 Noida");
               }
                //LatLngBound will cover all your marker on Google Maps
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latLng); //Taking Point A (First LatLng)
//                builder.include(customMarkerLocationThree); //Taking Point B (Second LatLng)
                LatLngBounds bounds = builder.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200);
                mGoogleMap.moveCamera(cu);
                mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
            }
        });


    }


    public static Bitmap createCustomMarker(Context context, @DrawableRes int resource, String _name) {

        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);

        CircleImageView   markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageResource(resource);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(_name);

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
    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.i(TAG,"marker arg0 = "+marker);
        if (!marker.equals(null)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
            alertbox.setMessage("Do you want to place this order");
            alertbox.setTitle("Order");
            alertbox.setIcon(R.drawable.ic_dashboard_black_24dp);

            alertbox.setNeutralButton("Yes",
                    new DialogInterface.OnClickListener() {
                        Class fragmentClass = null;

                        public void onClick(DialogInterface arg0,
                                            int arg1) {

                            postOderCreate();
                        }


                    });
            alertbox.setPositiveButton("No", null);

            alertbox.show();
        }
        return false;
    }

    private void postOderCreate() {

//            Date date = new Date();
        String date = new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(new Date());

        String customerid = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID();
        String empId = selectedEmpId;
        String orderDate = String.valueOf(date);
        String catalogId = String.valueOf(2);
        if(priceID.equals(null)) {
            priceIds = priceIDs;
        }else {
            priceIds = priceID;
        }
        try {
            result = new CustomerOrderHelper.OrderPost().execute(customerid, empId, orderDate, catalogId,priceIds).get();
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
}
