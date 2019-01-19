package com.example.awizom.jihuzur;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference datauserprofile;
    String lat="", longitud="";
    private static final String TAG = "LocationActivity";


    private GoogleMap googleMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);





        InitView();



    }



    public void InitView() {

        getMapvalue();
        datauserprofile = FirebaseDatabase.getInstance().getReference("profile");

        latlngs.add(new LatLng(21.21895, 81.676263));
        latlngs.add(new LatLng(21.24495, 81.633263));
        latlngs.add(new LatLng(21.21495, 81.699263));
        latlngs.add(new LatLng(21.21795, 81.678263));
        latlngs.add(new LatLng(21.26495, 81.622263));
        latlngs.add(new LatLng(21.21035, 81.678883));
        latlngs.add(new LatLng(21.21236, 81.633263));
    }











    private void getMapvalue() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        for (LatLng point : latlngs) {
            options.position(point);
            options.title("someTitle");
            options.snippet("someDesc");
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(options.getPosition()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }


//        mMap = googleMap;
//       // LatLng raipur = new LatLng(Double.parseDouble(lat.trim()),Double.parseDouble(longitud.trim()));
//
//        LatLng raipur = new LatLng(21.21895,81.676263);
//        mMap.addMarker(new MarkerOptions().position(raipur).title("Marker in Raipur"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(raipur));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//
//        LatLng Bhilai = new LatLng(21.24495,81.633263);
//        mMap.addMarker(new MarkerOptions().position(Bhilai).title("Marker in Bhilai"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Bhilai));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//
//        LatLng Durg = new LatLng(21.21495,81.699263);
//        mMap.addMarker(new MarkerOptions().position(Durg).title("Marker in Bhilai"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Durg));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
//
//        LatLng Dhamtari = new LatLng(21.21795,81.678263);
//        mMap.addMarker(new MarkerOptions().position(Dhamtari).title("Marker in Bhilai"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(Dhamtari));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

    }


}
