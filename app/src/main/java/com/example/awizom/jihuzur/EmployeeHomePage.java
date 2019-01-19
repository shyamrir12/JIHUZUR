package com.example.awizom.jihuzur;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.Fragment.SearchFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import okhttp3.internal.http2.Header;

public class EmployeeHomePage extends AppCompatActivity

        //side navigation drawer start

        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    String TAG;
    private Fragment fragment = null;
    private Fragment searchFragment;

    DatabaseReference datauser, datauserpro;
    String dUser;
    String name;
    String role;
    String Url;
    Boolean active = false;
    View header;
    ImageView profileImage;
    TextView userName, identityNo, identityType;
    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        //bottom navigation Button Onclick
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Class framentClass = null;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    getSupportActionBar().setTitle("Search");
                    fragment = searchFragment;
                    framentClass = SearchFragment.class;

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

        setContentView(R.layout.activity_employee_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerview = navigationView.getHeaderView(0);
        profileImage = headerview.findViewById(R.id.profileImage);
        userName = headerview.findViewById(R.id.profileName);
        identityNo = headerview.findViewById(R.id.identityNo);
        identityType = headerview.findViewById(R.id.identityType);


        identityNo.setOnClickListener(this);
        identityType.setOnClickListener(this);
        userName.setOnClickListener(this);




            Url = "https://firebasestorage.googleapis.com/v0/b/jihuzurdb.appspot.com/o/blank-profile.png?alt=media&token=72065919-9ed9-44ee-916e-e41fc97996da";
            Glide.with(EmployeeHomePage.this).load(Url).into(profileImage);

            String identNo = "identity no";
            String name = "welcome user";

            String identType = "identity type";
            identityType.setText(identType);
            identityNo.setText(identNo);
            userName.setText(name);



        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EmployeeDrawingActivity.class);
                startActivity(intent);
            }
        });


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
        // Handle action bar item clicks here. The action b
        // ar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        if(id == R.id.action_settings){
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.profileImage) {
            Intent imageView = new Intent(EmployeeHomePage.this, DrawingActivity.class);
            startActivity(imageView);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == identityNo.getId()) {
            Intent intent = new Intent(EmployeeHomePage.this, UpdateProfile.class);


            String uname = userName.getText().toString();
            String idenNo = identityNo.getText().toString();
            String idenType = identityType.getText().toString();
//Create the bundle
            Bundle bundle = new Bundle();

//Add your data to bundle
            bundle.putString("uname", uname);
            bundle.putString("idenNo", idenNo);
            bundle.putString("idenType", idenType);

//Add the bundle to the intent
            intent.putExtras(bundle);



            startActivity(intent);


        }
    }
}


