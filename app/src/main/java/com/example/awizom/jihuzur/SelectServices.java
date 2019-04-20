package com.example.awizom.jihuzur;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Adapter.CustomAdapter;
import com.example.awizom.jihuzur.Adapter.ServiceListAdapter;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.CustomerActivity.SingleShotLocationProvider;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SelectServices extends AppCompatActivity implements View.OnClickListener {

    String categoryName, catalogID, imageLink, empskill = "";
    RecyclerView recyclerView;
    List<Service> serviceList;
    ServiceListAdapter serviceListAdapter;
    private String result = "";
    TextView categoryname;
    FloatingActionButton addService;
    AutoCompleteTextView editServicename, editDescription;
    Spinner displayType;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_and_repair);
        initView();
    }

    private void initView() {

        categoryName = getIntent().getStringExtra("CategoryName");
        catalogID = getIntent().getStringExtra("CatalogID");
        imageLink = getIntent().getStringExtra("Image");
        empskill = getIntent().getStringExtra("EmployeeSkill");
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Services");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.BLACK);
        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addService = (FloatingActionButton) findViewById(R.id.addService);
        addService.setOnClickListener(this);
        categoryname = findViewById(R.id.categoryName);
        imageView = findViewById(R.id.backdrop);
        categoryname.setText( "    " +categoryName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getServiceList();
        if (SharedPrefManager.getInstance(SelectServices.this).getUser().getID() == null) {
            addService.setVisibility(View.GONE);
        }

        try {
            if (SharedPrefManager.getInstance(SelectServices.this).getUser().getRole().equals("Employee")) {
                addService.setVisibility(View.GONE);
            } else if (SharedPrefManager.getInstance(SelectServices.this).getUser().getRole().equals("Customer")) {
                openGPSSettings();
                addService.setVisibility(View.GONE);
            } else if (SharedPrefManager.getInstance(SelectServices.this).getUser().getRole().equals("Admin")) {
                addService.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (imageLink != null) {
            Glide.with(getApplicationContext())
                    .load(imageLink)
                    .placeholder(R.drawable.home_cleaning).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    private void openGPSSettings() {
        //Get GPS now state (open or closed)
        try {
            int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            if(locationMode==3)
            {
                enable_buttons();
                //   Toast.makeText(getApplicationContext(),"All is Fine",Toast.LENGTH_LONG).show();

            }
            else {
                showdialogforGPS();
                //   Toast.makeText(getApplicationContext(),"Set Your Location Method:High Accurecy",Toast.LENGTH_LONG).show();
                //    runtime_permissions();
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void showdialogforGPS() {

        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
        android.app.AlertDialog.Builder alertbox = new android.app.AlertDialog.Builder(SelectServices.this);
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

    private void enable_buttons() {

        Intent i = new Intent(this, SingleShotLocationProvider.class);
        startService(i);
    }


    public void getServiceList() {

        try {
//            mSwipeRefreshLayout.setRefreshing(true);
            result = new AdminHelper.GETServiceList().execute(catalogID).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Service>>() {
                }.getType();
                serviceList = new Gson().fromJson(result, listType);
                serviceListAdapter = new ServiceListAdapter(SelectServices.this, serviceList, empskill,imageLink);
                recyclerView.setAdapter(serviceListAdapter);

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
//            mSwipeRefreshLayout.setRefreshing(false);
            // System.out.println("Error: " + e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == addService.getId()) {
            showAddServiceDialog();

        }
    }

    private void showAddServiceDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_servicelayout, null);
        dialogBuilder.setView(dialogView);

        editServicename = (AutoCompleteTextView) dialogView.findViewById(R.id.editServiceName);
        editDescription = (AutoCompleteTextView) dialogView.findViewById(R.id.description);
        displayType = (Spinner) dialogView.findViewById(R.id.displayType);
        String[] Service_Terms = {"Radio", "Checkbox", "Range"};
        int flags[] = {R.drawable.ic_radio_button_checked_black_24dp, R.drawable.ic_check_box_black_24dp, R.drawable.ic_date_range_black_24dp};
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), flags, Service_Terms);
        displayType.setAdapter(customAdapter);


        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddService);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);
        dialogBuilder.setTitle("Add Service");
        dialogBuilder.setIcon(R.drawable.icon_services);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation()) {
                    String service = editServicename.getText().toString().trim();
                    String descriptions = editDescription.getText().toString().trim();
                    String displaytype = displayType.getSelectedItem().toString();
                    String serviceid = "0";
                    if (displaytype == "Range") {
                        displaytype = "";

                    }

                    try {
                        result = new AdminHelper.POSTService().execute(serviceid, catalogID, service, descriptions, displaytype).get();
                        Gson gson = new Gson();
                        final Result jsonbodyres = gson.fromJson(result, Result.class);
                        Toast.makeText(getApplicationContext(), jsonbodyres.getMessage(), Toast.LENGTH_SHORT).show();
                        getServiceList();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                        // System.out.println("Error: " + e);
                    }
                    b.dismiss();

                }
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
    }

    private boolean validation() {

        if (editServicename.getText().toString().isEmpty()) {
            editServicename.setError("Enter  valid Servicename");
            editServicename.requestFocus();
            return false;
        }
        if (editDescription.getText().toString().isEmpty()) {
            editDescription.setError("Enter valid Description");
            editDescription.requestFocus();
            return false;
        }


        return true;
    }

}


