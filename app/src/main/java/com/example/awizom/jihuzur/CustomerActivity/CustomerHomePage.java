package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.awizom.jihuzur.ComplaintActivity;
import com.example.awizom.jihuzur.DrawingActivity;
import com.example.awizom.jihuzur.Fragment.CatalogFragment;
import com.example.awizom.jihuzur.Fragment.HelpCenterFragment;
import com.example.awizom.jihuzur.Fragment.MyBookingFragment;
import com.example.awizom.jihuzur.Fragment.SearchFragment;
import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.SettingsActivity;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class CustomerHomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG;
    private Fragment fragment = null;
    private Fragment searchFragment,myBookingFragment,helpCenterFragment,catalogFragment;

    DatabaseReference datauser, datauserpro;
    String dUser,name,role;
    String Url;
    Boolean active = false;
    View header;
    private CardView homeCleaningCardView,appliancecardView;
    private ImageView homecleaning;
    private TextView homeCleaningTextView;
    DatabaseReference datauserprofile;
    private Intent intent;
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
                    getSupportActionBar().setTitle("Customer Home");
                  intent=new Intent(CustomerHomePage.this,CustomerHomePage.class);
                  startActivity(intent);
                    break;

                case R.id.navigation_booking:

                    intent=new Intent(CustomerHomePage.this,MyBokingsActivity.class);
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

    //layout declaration
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       initView();
    }

    private void initView() {


        searchFragment = new SearchFragment();
        myBookingFragment= new MyBookingFragment();
        catalogFragment = new CatalogFragment();

        setContentView(R.layout.activity_customer_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        homeCleaningCardView = findViewById(R.id.homeCleancardViewOne);
        homeCleaningCardView.setOnClickListener(this);
        appliancecardView = findViewById(R.id.appliancesCardViewOne1);
        appliancecardView.setOnClickListener(this);

        homecleaning = findViewById(R.id.homecleaning);


        homeCleaningTextView = findViewById(R.id.homecleaningTextView);
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



    }

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
        getMenuInflater().inflate(R.menu.customer_home_page, menu);
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
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fAuth.signOut();

            return true;
        }
        if (id == R.id.action_customerHome) {
            intent = new Intent(CustomerHomePage.this, CustomerHomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


            return true;
        }
        if(id == R.id.action_settings){
            intent = new Intent(CustomerHomePage.this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


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

        if (id == R.id.nav_booking) {
            intent=new Intent(CustomerHomePage.this,MyBokingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_complaint) {
            intent=new Intent(CustomerHomePage.this,CustomerComplaintActivity.class);
                ActivityOptions startAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left,R.anim.fui_slide_in_right);

            startActivity(intent,startAnimation.toBundle());
        } else if (id == R.id.nav_order) {

        } else if (id == R.id.nav_logout) {
            SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(login);
            finish();
        }else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_send) {

            String phoneNumber="",message="";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);

        } else if (id == R.id.profileImage) {
            Intent imageView = new Intent(CustomerHomePage.this, DrawingActivity.class);
            startActivity(imageView);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.homeCleancardViewOne:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName","Home Cleaning & Repairs");
                startActivity(intent);
                break;
            case R.id.appliancesCardViewOne1:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName","Appliance & Repairs");
                startActivity(intent);
                break;
        }

    }
}

