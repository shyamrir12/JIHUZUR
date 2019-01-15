package com.example.awizom.jihuzur;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Model.Profile;
import com.example.awizom.jihuzur.Model.ProfileModel;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DatabaseReference datauserprofile;
    String lat="", longitud="";
    private static final String TAG = "LocationActivity";
    List<ProfileModel> profilelist;
    ProfileModel profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        datauserprofile = FirebaseDatabase.getInstance().getReference("profile");
        profilelist = new ArrayList<>();
        InitView();



    }



    public void InitView() {
        try {

            datauserprofile = FirebaseDatabase.getInstance().getReference("profile");
            Query query= FirebaseDatabase.getInstance().getReference("profile");
            query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                try{
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        profile = postSnapshot.getValue(ProfileModel.class);
                                        profilelist.add(profile);
                                        if(!profilelist.equals(null)) {
                                            for(int i=0 ; i<profilelist.size(); i++) {
                                                lat = String.valueOf(profilelist.get(i).getLat());
                                                longitud = String.valueOf(profilelist.get(i).getLat());
                                                getMapvalue();
                                            }
                                        }
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
             });



        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
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
        LatLng raipur = new LatLng(Double.parseDouble(lat.trim()),Double.parseDouble(longitud.trim()));

      //  LatLng raipur = new LatLng(21.21495,81.676263);
        mMap.addMarker(new MarkerOptions().position(raipur).title("Marker in Raipur"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(raipur));
    }


}
