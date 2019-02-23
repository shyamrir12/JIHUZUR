package com.example.awizom.jihuzur;


import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Adapter.ServiceListAdapter;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.Result;
import com.example.awizom.jihuzur.Model.Service;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectServices extends AppCompatActivity implements View.OnClickListener {

    String categoryName, catalogID,imageLink,empskill="";
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
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setSubtitleTextColor(Color.BLACK);
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

        categoryname.setText(categoryName);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getServiceList();

        if(SharedPrefManager.getInstance(SelectServices.this).getUser().getRole().equals("Employee") ){
            addService.setVisibility(View.GONE);
        }else if(SharedPrefManager.getInstance(SelectServices.this).getUser().getRole().equals("Customer")){
            addService.setVisibility(View.GONE);
        }else {
            addService.setVisibility(View.VISIBLE);
        }


        if(imageLink != null) {

            Glide.with(getApplicationContext())
                    .load(imageLink)
                    .placeholder(R.drawable.home_cleaning).dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }


    }

    public void getServiceList() {


        try {
//            mSwipeRefreshLayout.setRefreshing(true);

              result=new AdminHelper.GETServiceList().execute(catalogID).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Service>>() {
                }.getType();
                serviceList = new Gson().fromJson(result, listType);
                serviceListAdapter = new ServiceListAdapter(SelectServices.this, serviceList,empskill);

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
        displayType=(Spinner)dialogView.findViewById(R.id.displayType);
        List<String> list = new ArrayList<String>();
        list.add("Radio");
        list.add("Checkbox");
        list.add("Range");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);

        displayType.setAdapter(dataAdapter);

        final Button buttonAddCatalog = (Button) dialogView.findViewById(R.id.buttonAddService);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.buttonCancel);

        dialogBuilder.setTitle("Add Service");
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonAddCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String service = editServicename.getText().toString().trim();
                String descriptions = editDescription.getText().toString().trim();
                String displaytype = displayType.getSelectedItem().toString();
                String serviceid = "0";

                if (displaytype=="Range")
                {
                    displaytype="";

                }

                try {

                result= new AdminHelper.POSTService().execute(serviceid,catalogID, service, descriptions,displaytype).get();
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

}


