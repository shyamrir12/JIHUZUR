package com.example.awizom.jihuzur.CustomerActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeLocationActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.Locationhelper.FetchURL;
import com.example.awizom.jihuzur.Locationhelper.TaskLoadedCallback;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nullable;

public class TrackActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, GoogleMap.OnMarkerClickListener {


    Double lat1, long1;
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
    TextView distancefor, mMsgView, employeeDetails;
    /* Button getDirection;*/
    MarkerOptions place1, place2;
    FirebaseFirestore db;
    de.hdodenhof.circleimageview.CircleImageView employeeImage;
    ImageView call;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track_location_activity);
        db = FirebaseFirestore.getInstance();
        InitView();
    }

    private void InitView() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        /*  catalogName = getIntent().getStringExtra("CatalogName");*/
        toolbar.setTitle("Track Employee");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        customerID = getIntent().getStringExtra("CustomerID");
        employeeID = getIntent().getStringExtra("EmployeeID");
        getCustomerProfileGet();
        employeeDetails = (TextView) findViewById(R.id.employeeDetails);
        employeeImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.employee_dp);
        call = (ImageView) findViewById(R.id.call);
        getEmployeeProfileGet();
        mMsgView = (TextView) findViewById(R.id.msgView);
        distancefor = (TextView) findViewById(R.id.distance);

    /*    getDirection = findViewById(R.id.btnGetDirection);
        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });*/
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getCustomerProfileGet() {

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


                    final DocumentReference docRef = db.collection("CustomerLoc").document(customerID);
                    docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot snapshot,
                                            @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w("failed", "Listen failed.", e);
                                return;
                            }

                            String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                    ? "Local" : "Server";

                            if (snapshot != null && snapshot.exists()) {
                                Log.d("Snapshot data", source + " data: " + snapshot.getData());

                                Double lat1 = Double.parseDouble(String.valueOf(snapshot.get("lat")));
                                Double  long1 = Double.parseDouble(String.valueOf(snapshot.get("long")));
                                place2 = new MarkerOptions().position(new LatLng(Double.valueOf(String.valueOf(lat1)),
                                        Double.valueOf(String.valueOf(long1)))).title("Location 1");

                                if (dataProfileCustomer != null) {

                                    place2 = new MarkerOptions().position(new LatLng(Double.valueOf(String.valueOf(lat1)),
                                            Double.valueOf(String.valueOf(long1)))).title("Location 1");
                                    /*  getMapvalue();*/
                                    cusLatLng = new LatLng(Double.valueOf(String.valueOf(lat1)),
                                            Double.valueOf(String.valueOf(long1)));


                                    place1 = new MarkerOptions().position(new LatLng(Double.valueOf(String.valueOf(lat1)),
                                            Double.valueOf(String.valueOf(long1)))).title("Customer Location");

                                }

                            } else {
                                Log.d("snapshot null", source + " data: null");
                            }
                        }
                    });


                    getMapvalue();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getEmployeeProfileGet() {
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
                    String Image = AppConfig.BASE_URL + dataProfileEmployee.getImage();

                    if (Image == null) {
                        employeeImage.setImageResource(R.drawable.jihuzurblanklogo);
                        /*                 Glide.with(context).load("http://192.168.1.103:7096/Images/Category/1.png").into(markerImage);*/
                    } else {

                        Glide.with(TrackActivity.this)
                                .load(Image)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(employeeImage);

                        /* Glide.with(getApplicationContext()).load(Image).into(employeeImage); */
                    }
                    String name = dataProfileEmployee.getName();
                    final String mobileNo = dataProfileEmployee.getMobileNo();
                    employeeDetails.setText(name + "," +" " + mobileNo);

                    empLatLng = new LatLng(Double.valueOf(String.valueOf(dataProfileEmployee.getLat())),
                            Double.valueOf(String.valueOf(dataProfileEmployee.getLong())));
                    call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            v.startAnimation(buttonClick);
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobileNo));
                            if (ActivityCompat.checkSelfPermission(TrackActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

                    getMapvalue();
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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        final Marker[] marker = new Marker[1];
        final DocumentReference docRef = db.collection("Profile").document(employeeID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("failed", "Listen failed.", e);
                    return;
                }

                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d("Snapshot data", source + " data: " + snapshot.getData());

                    lat1 = Double.parseDouble(String.valueOf(snapshot.get("lat")));
                    long1 = Double.parseDouble(String.valueOf(snapshot.get("long")));



                    int height = 80;
                    int width = 80;
                    BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.map_logo);
                    Bitmap b = bitmapdraw.getBitmap();
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                    empLatLng = new LatLng(Double.valueOf(String.valueOf(lat1)),
                            Double.valueOf(String.valueOf(long1)));
                    place2 = new MarkerOptions().position(new LatLng(Double.valueOf(String.valueOf(lat1)),
                            Double.valueOf(String.valueOf(long1)))).title(dataProfileEmployee.Name).snippet(dataProfileEmployee.MobileNo).icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                    new FetchURL(TrackActivity.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");

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
                    BitmapDrawable bitmapdrawvust = (BitmapDrawable) getResources().getDrawable(R.drawable.greenmappin);
                    Bitmap bcustomer = bitmapdrawvust.getBitmap();
                    Bitmap customermarker = Bitmap.createScaledBitmap(bcustomer, width, height, false);
                    //This is Customer Location
                    mMap.addMarker(new MarkerOptions().position(cusLatLng)
                            .title(dataProfileCustomer.Name).icon(BitmapDescriptorFactory.fromBitmap(customermarker))
                            .snippet(dataProfileCustomer.MobileNo));
                    mMap.setTrafficEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(cusLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    if (marker[0] != null) {
                        marker[0].remove();
                        marker[0] = mMap.addMarker(place2);
                    } else {
                        //This is Employee Location
                        marker[0] = mMap.addMarker(place2);
                    }
                    mMap.setTrafficEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(empLatLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        @Override
                        public void onMapLoaded() {
                        }
                    });
                } else {
                    Log.d("snapshot null", source + " data: null");
                }
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
        distancefor.setText(distances + ","+" " + duration);

    }
}
