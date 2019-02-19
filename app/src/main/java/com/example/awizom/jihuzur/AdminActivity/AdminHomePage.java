package com.example.awizom.jihuzur.AdminActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Adapter.DiscountListAdapter;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.DrawingActivity;
import com.example.awizom.jihuzur.Fragment.CatalogFragment;
import com.example.awizom.jihuzur.Fragment.HelpCenterFragment;
import com.example.awizom.jihuzur.Fragment.MyBookingFragment;
import com.example.awizom.jihuzur.Fragment.SearchFragment;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Helper.EmployeeOrderHelper;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.DiscountView;
import com.example.awizom.jihuzur.Model.EmployeeProfileModel;
import com.example.awizom.jihuzur.Model.UserLogin;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.SettingsActivity;
import com.example.awizom.jihuzur.UpdateProfile;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminHomePage extends AppCompatActivity

        //side navigation drawer start

        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static int SPLASH_TIME_OUT = 2000;
    String TAG;
    DatabaseReference datauser, datauserpro;
    String dUser;
    String img_str;
    String name, result = "";
    String role;
    String Url;
    Intent intent;
    Boolean active = false;
    View header;
    de.hdodenhof.circleimageview.CircleImageView profileImages;
    CardView homecleaning, appliance;
    TextView userName, identityNo, identityType;
    List<DataProfile> listtype;
    private Fragment fragment = null;
    private Fragment searchFragment, myBookingFragment, helpCenterFragment, catalogFragment;
    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        //bottom navigation Button Onclick
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class framentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    getSupportActionBar().setTitle("Catalog");
                    fragment = catalogFragment;
                    framentClass = CatalogFragment.class;
//                    intent = new Intent(AdminHomePage.this, AdminCatalogActivity.class);
//                    startActivity(intent);

                    break;
                case R.id.navigation_booking:
                    getSupportActionBar().setTitle("My Booking");
                    fragment = myBookingFragment;
                    framentClass = MyBookingFragment.class;

//                    intent = new Intent(AdminHomePage.this, AdminPricingActivity.class);
//                    startActivity(intent);
                    break;
                case R.id.navigation_helpCenter:
                    getSupportActionBar().setTitle("Help Center");
                    fragment = helpCenterFragment;
                    framentClass = HelpCenterFragment.class;
                    break;

            }
            try {
                fragment = (Fragment) framentClass.newInstance();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
                setTitle("");
            } catch (Exception e) {
                e.printStackTrace();
            }


            return false;
        }
    };

    //layout declaration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchFragment = new SearchFragment();
        myBookingFragment = new MyBookingFragment();
        catalogFragment = new CatalogFragment();

        setContentView(R.layout.activity_admin_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homecleaning = (CardView) findViewById(R.id.homeCleancardViewOne);
        appliance = (CardView) findViewById(R.id.appliancecardview);
        appliance.setOnClickListener(this);
        homecleaning.setOnClickListener(this);

        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initView();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerview = navigationView.getHeaderView(0);
        profileImages = headerview.findViewById(R.id.profileImage);

        userName = headerview.findViewById(R.id.profileName);
        userName.setText(SharedPrefManager.getInstance(this).getUser().getName());
        userName.setOnClickListener(this);


        img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();
        {

            try {
                if (SharedPrefManager.getInstance(this).getUser().getImage() == null)

                {

                    profileImages.setImageResource(R.drawable.jihuzurblanklogo);
                    //     Glide.with(mCtx).load("http://192.168.1.105:7096/Images/Category/1.png").into(holder.categoryImage);
                } else {


                    Glide.with(this).load(img_str).into(profileImages);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        profileImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomePage.this, DrawingActivity.class);
                startActivity(intent);


            }
        });


    }


    private void initView() {

        getProfile();

    }

    private void getProfile() {

        String id = SharedPrefManager.getInstance(this).getUser().getID();

        try {


            result = new AdminHelper.GetProfileForShow().execute(id).get();
            if (result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Invalid request", Toast.LENGTH_SHORT).show();
            } else {

                Gson gson = new Gson();
                Type listType = new TypeToken<DataProfile>() {
                }.getType();
                DataProfile dataProfile = new Gson().fromJson(result, listType);
                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();

                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;

//                        SharedPrefManager.getInstance(this).userLogin(dataProfile1);


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //fumctionalities for side navigation drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
//            FirebaseAuth fAuth = FirebaseAuth.getInstance();
//            fAuth.signOut();

            return true;
        }
        if (id == R.id.action_customerHome) {
            Intent i = new Intent(this, CustomerHomePage.class);
            startActivity(i);


            return true;
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(AdminHomePage.this, SettingsActivity.class);
            startActivity(i);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //side navigation drwaer started onCLick

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_employee) {
            // Handle the camera action

            intent = new Intent(AdminHomePage.this, AdminsEmployeeListActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_master) {


        } else if (id == R.id.nav_catalogName) {

            intent = new Intent(AdminHomePage.this, AdminCatalogActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_catalogpricing) {
            intent = new Intent(AdminHomePage.this, AdminPricingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_complaintReply) {
            intent = new Intent(AdminHomePage.this, AdminComplaintReply.class);
            startActivity(intent);
        }
//        else if (id == R.id.nav_ReviewReply) {
//            intent = new Intent(AdminHomePage.this, AdminReviewReply.class);
//            startActivity(intent);
//        }

        else if (id == R.id.nav_discount) {
            intent = new Intent(AdminHomePage.this, AdminDiscountActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(login);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onClick(View v) {


        if (v.getId() == homecleaning.getId()) {
            Intent intent = new Intent(AdminHomePage.this, AdminCategoryActivity.class);
            intent.putExtra("CatalogName", "Home Cleaning & Repairs");
            startActivity(intent);


        }
        if (v.getId() == appliance.getId()) {
            Intent intent = new Intent(AdminHomePage.this, AdminCategoryActivity.class);
            intent.putExtra("CatalogName", "Appliance & Repairs");
            startActivity(intent);


        }


    }
}

