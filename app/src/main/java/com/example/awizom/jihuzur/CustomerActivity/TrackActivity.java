package com.example.awizom.jihuzur.CustomerActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awizom.jihuzur.EmployeeActivity.EmployeeLocationActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Locationhelper.FetchURL;
import com.example.awizom.jihuzur.Locationhelper.TaskLoadedCallback;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {

    String result = "";
    LatLng cusLatLng, empLatLng;
    private MapView mapView;
    private GoogleMap mMap;
    private DataProfile dataProfileCustomer;
    private DataProfile dataProfileEmployee;
    private ArrayList<LatLng> latlngCustomer = new ArrayList<>();
    private ArrayList<LatLng> latlngEmployee = new ArrayList<>();
    private String customerID = "", employeeID = "";
    private Polyline currentPolyline;
    TextView  distancefor, mMsgView ;
    Button getDirection;
    MarkerOptions place1,place2;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_location_activity);
        db = FirebaseFirestore.getInstance();
        InitView();
    }

    private void InitView() {
        getSupportActionBar().setTitle("Track");
        customerID = getIntent().getStringExtra("CustomerID");
        employeeID = getIntent().getStringExtra("EmployeeID");
        db.collection("Profile").document(employeeID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();

                        String lat1= String.valueOf(document.get("lat"));
                        String long1=String.valueOf(document.get("lat"));

                        empLatLng = new LatLng(Double.valueOf(lat1),
                                Double.valueOf(long1));
                        place2=new MarkerOptions().position(new LatLng(Double.valueOf(lat1),
                                Double.valueOf(long1))).title("Employee Location");
                        getMapvalue();

                        //       Log.d( "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //  Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        getCustomerProfileGet();
        getEmployeeProfileGet();
        mMsgView = (TextView) findViewById(R.id.msgView);
        distancefor = (TextView) findViewById(R.id.distance);
        getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(TrackActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        });
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCustomerProfileGet() {

        String id = SharedPrefManager.getInstance(this).getUser().getID();
        try {
            result = new AdminHelper.GetProfileForShow().execute(customerID).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                dataProfileCustomer = new Gson().fromJson(result, listType);
                if (dataProfileCustomer != null) {
                    cusLatLng = new LatLng(Double.valueOf(String.valueOf(dataProfileCustomer.Lat)),
                            Double.valueOf(String.valueOf(dataProfileCustomer.Long)));

                    place1=new MarkerOptions().position(new LatLng(Double.valueOf(String.valueOf(dataProfileCustomer.Lat)),
                            Double.valueOf(String.valueOf(dataProfileCustomer.Long)))).title("Employee Location");

                    getMapvalue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeProfileGet() {

        String id = SharedPrefManager.getInstance(this).getUser().getID();

        try {
            result = new AdminHelper.GetProfileForShow().execute(employeeID).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                dataProfileEmployee = new Gson().fromJson(result, listType);
                if (dataProfileEmployee != null) {




                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getMapvalue() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        PolylineOptions polylinesOptions = new PolylineOptions();
        polylinesOptions.add()
                .add(new LatLng(Double.valueOf(String.valueOf(dataProfileCustomer.Lat)),
                        Double.valueOf(String.valueOf(dataProfileCustomer.Long))))
                .add(new LatLng(Double.valueOf(String.valueOf(dataProfileEmployee.Lat)),
                        Double.valueOf(String.valueOf(dataProfileEmployee.Long))));
        polylinesOptions
                .width(4)
                .color(Color.YELLOW);
        mMap.addPolyline(polylinesOptions);

        //This is Customer Location
        mMap.addMarker(new MarkerOptions().position(cusLatLng)
                .title(dataProfileCustomer.Name)
                .snippet(dataProfileCustomer.MobileNo));
        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cusLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


        //This is Employee Location
        mMap.addMarker(new MarkerOptions().position(empLatLng)
                .title(dataProfileEmployee.Name)
                .snippet(dataProfileEmployee.MobileNo));

        mMap.setTrafficEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(empLatLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                mMap.addMarker(place1);
                mMap.addMarker(place1);
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        String distances = values[1].toString();
        String duration = values[2].toString();
        distancefor.setText(distances + "," + duration);


    }
}
