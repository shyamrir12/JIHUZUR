package com.example.awizom.jihuzur;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.awizom.jihuzur.Helper.CustomerOrderHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap mMap;
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

    List<EmployeeProfileModel> employeeProfileModelList;
    private String[] empNameList, empLat, empLong;
    Double latitude, latitude1;
    Double longitude, longitude1;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        InitView();
    }

    public void InitView() {
        getMapvalue();
        //latlngs.add(new LatLng(latitude, longitude));
        // latlngs.add(new LatLng(latitude1, longitude1));

        employeeProfileGet();


    }

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

        mMap = googleMap;
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(latlngs);
        polylineOptions
                .width(5)
                .color(Color.YELLOW);

        Marker[] allMarkers = new Marker[employeeProfileModelList.size()];

        for (int i = 0; i < employeeProfileModelList.size(); i++)
        {
            LatLng latLng = new LatLng(Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLat())),
                    Double.valueOf(String.valueOf(employeeProfileModelList.get(i).getLong())));
            if (googleMap != null) {
                googleMap.setOnMarkerClickListener(this);
                allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(employeeProfileModelList.get(i).getName() +" "+ "+91"+employeeProfileModelList.get(i).getMobileNo())
                        .snippet(employeeProfileModelList.get(i).getID()));
                googleMap.addPolyline(polylineOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13.0f));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            }
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
        String empId = "c5e2f335-2258-4f41-98d9-c26f25495745";
        String orderDate = String.valueOf(date);
        String catalogId = String.valueOf(2);
        try {
            result = new CustomerOrderHelper.OrderPost().execute(customerid, empId, orderDate, catalogId).get();
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
