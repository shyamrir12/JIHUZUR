package com.example.awizom.jihuzur;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import com.example.awizom.jihuzur.Helper.GetEmployeeProfileHelper;
import com.example.awizom.jihuzur.Helper.OrderPostHelper;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private MapView mapView;
    private GoogleMap mMap;
    Polyline line;
    PolylineOptions polylineOptions;
    ArrayList markerPoints= new ArrayList();

    String lat="", longitud="",result="";
    private static final String TAG = "LocationActivity";
    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    List<EmployeeProfileModel> employeeProfileModelList;
    private String[] empNameList,empLat,empLong;
    Double latitude,latitude1;
    Double longitude,longitude1;
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

                result   = new GetEmployeeProfileHelper.GetEmployeeProfileForShow().execute().get();

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
                }
                latitude =Double.valueOf(String.valueOf(empLat[0]));
                longitude = Double.valueOf(String.valueOf(empLong[0]));
                latitude1 =Double.valueOf(String.valueOf(empLat[1]));
                longitude1 = Double.valueOf(String.valueOf(empLong[1]));
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
//        PolylineOptions polylineOptions = new PolylineOptions();
//        polylineOptions.addAll(latlngs);
//        polylineOptions
//                .width(5)
//                .color(Color.BLUE);
//        for (LatLng point : latlngs) {
//            options.position(point);
//            options.title("someTitle");
//            options.snippet("someDesc");
//            mMap.addMarker(options);
//            mMap.addPolyline(polylineOptions);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(options.getPosition()));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
//
//        }






        mMap = googleMap;
        googleMap.setOnMarkerClickListener(this);
        LatLng raipur = new LatLng(Double.parseDouble(String.valueOf(latitude)),Double.parseDouble(String.valueOf(longitude)));
        mMap.addMarker(new MarkerOptions().position(raipur)
                .title("Marker in Raipur")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(raipur));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        LatLng Bhilai = new LatLng(Double.parseDouble(String.valueOf(latitude1)),Double.parseDouble(String.valueOf(longitude1)));
        mMap.addMarker(new MarkerOptions().position(Bhilai).title("Marker in Bhilai"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Bhilai));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.equals(null))
        {
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

            Date date = new Date();
            String customerid = "f2176447-19c3-4d33-a181-13c466037e28";
            String empId ="e29a466e-9391-411d-b0eb-f25da65abd7e";
            String orderDate= String.valueOf(date);
            String catalogId = String.valueOf(3);
        try {
            result   = new OrderPostHelper.OrderPost().execute(customerid,empId,orderDate,catalogId).get();
            if(!result.equals(null)){
                intent = new Intent(this,CurrentOrderActivity.class);
                startActivity(intent);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
            UserLogin.RootObject jsonbody = gson.fromJson(result, UserLogin.RootObject.class);
            Toast.makeText(this,jsonbody.Message,Toast.LENGTH_SHORT).show();

            if(!result.equals(null)){


            }


    }
}
