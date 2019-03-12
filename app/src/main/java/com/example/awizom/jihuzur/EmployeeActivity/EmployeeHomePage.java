package com.example.awizom.jihuzur.EmployeeActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.DrawingActivity;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter.EmployeePageAdapter;
import com.example.awizom.jihuzur.Fragment.HelpCenterFragment;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.Service.GPS_Service;
import com.example.awizom.jihuzur.Service.LocationMonitoringNotificationService;
import com.example.awizom.jihuzur.SettingsActivity;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by Ravi on 07/01/2019.
 */

public class EmployeeHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String result = "";
    String TAG;
    ImageView imageView;
    String dUser, name, role, Url;
    Boolean active = false;
    View header;
    String img_str;
    TextView userName, userContact, identityNo, identityType;
    android.widget.Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    EmployeePageAdapter pageAdapter;
    TabItem outGoing, history;
    private Fragment fragment = null;
    private Fragment helpCenterFragment;
    private Intent intent;
    private CardView homeCleaningCardView, appliancecardView;
    private TextView btn_start, btn_stop;
    private TextView textView;
    private BroadcastReceiver broadcastReceiver;
    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        //bottom navigation Button Onclick
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class framentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    getSupportActionBar().setTitle("Employee Home");
                    intent = new Intent(EmployeeHomePage.this, EmployeeHomePage.class);
                    startActivity(intent);
                    break;

                case R.id.navigation_booking:
                    intent = new Intent(EmployeeHomePage.this, EmployeeBookingsActivity.class);
                    startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    textView.append("\n" + intent.getExtras().get("coordinates"));
                    //Log.d("myTag", "\n" +intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    //layout declaration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        // searchFragment = new SearchFragment();

        setContentView(R.layout.activity_employee_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btn_start = findViewById(R.id.button);
        btn_stop = findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        tabLayout = findViewById(R.id.tablayout);
        outGoing = findViewById(R.id.outgoing);
        history = findViewById(R.id.history);
        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new EmployeePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(EmployeeHomePage.this,
                            R.color.colorPrimary));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(EmployeeHomePage.this,
                                R.color.colorPrimary));
                    }
                } else if (tab.getPosition() == 2) {

                    tabLayout.setBackgroundColor(ContextCompat.getColor(EmployeeHomePage.this,
                            android.R.color.black));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(ContextCompat.getColor(EmployeeHomePage.this,
                                android.R.color.black));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        if (!runtime_permissions())
            enable_buttons();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        imageView = headerview.findViewById(R.id.imageView);
        img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();
        {
            try {
                if (SharedPrefManager.getInstance(this).getUser().getImage() == null) {
                    imageView.setImageResource(R.drawable.jihuzurblanklogo);
                    //     Glide.with(mCtx).load("http://192.168.1.105:7096/Images/Category/1.png").into(holder.categoryImage);
                } else {
                    Glide.with(EmployeeHomePage.this)
                            .load(img_str)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                    //                    Glide.with(this).load(img_str).into(imageView);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userName = headerview.findViewById(R.id.profileName);
        userContact = headerview.findViewById(R.id.empContact);
        String uname = SharedPrefManager.getInstance(EmployeeHomePage.this).getUser().getName().toString();
        String ucontact = SharedPrefManager.getInstance(EmployeeHomePage.this).getUser().getMobileNo().toString();
        userContact.setText(ucontact);
//
        identityNo = headerview.findViewById(R.id.identityNo);
        identityType = headerview.findViewById(R.id.identityType);

        getProfile();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeHomePage.this, DrawingActivity.class);
                startActivity(intent);
            }
        });
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
                userName.setText(dataProfile.getName().toString());
                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();
                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void enable_buttons() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(EmployeeHomePage.this, LocationMonitoringNotificationService.class);
                serviceIntent.putExtra("inputExtra", "Location Service Enabled");
                ContextCompat.startForegroundService(EmployeeHomePage.this, serviceIntent);
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                startService(i);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent serviceIntent = new Intent(EmployeeHomePage.this, LocationMonitoringNotificationService.class);
                stopService(serviceIntent);
                Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                stopService(i);
            }
        });
    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
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
        getMenuInflater().inflate(R.menu.employee_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();
            return true;
        }
        if (id == R.id.action_customerHome) {
            Intent i = new Intent(EmployeeHomePage.this, EmployeeHomePage.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_settings) {
            Intent i = new Intent(EmployeeHomePage.this, SettingsActivity.class);
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
        if (id == R.id.nav_profile) {
            intent = new Intent(EmployeeHomePage.this, DrawingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_booking) {
            intent = new Intent(EmployeeHomePage.this, EmployeeBookingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_skill) {
            intent = new Intent(EmployeeHomePage.this, EmployeeSkillActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {

            SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(login);
            finish();

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_send) {

            String phoneNumber = "", message = "";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}


