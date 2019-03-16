package com.example.awizom.jihuzur.CustomerActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.DrawingActivity;
import com.example.awizom.jihuzur.Fragment.CatalogFragment;
import com.example.awizom.jihuzur.Fragment.HelpCenterFragment;
import com.example.awizom.jihuzur.Fragment.MyBookingFragment;
import com.example.awizom.jihuzur.Fragment.SearchFragment;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.MenuActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.SettingsActivity;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


/**
 * Created by Ravi on 07/01/2019.
 */

public class CustomerHomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG;
    private Fragment fragment = null;
    private Fragment searchFragment,myBookingFragment,helpCenterFragment,catalogFragment;
    String result="";
    DatabaseReference datauser, datauserpro;
    String dUser,name,role;
    String Url;
    Boolean active = false;
    View header;
    private CardView electricianCardView,appliancecardView,movingTruckCardViewTwo, washingCardViewThree1,tutorcardViewThree,ringcardViewTwo;
    private ImageView homecleaning;
    private TextView homeCleaningTextView;
    DatabaseReference datauserprofile;
    private Intent intent;
    TextView userName, identityNo, identityType, userContact;
    ImageView imageView;
    String img_str;
    boolean check = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    //bottom navigation drawer started
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
        electricianCardView = findViewById(R.id.homecleaningCardViewOne);
        electricianCardView.setOnClickListener(this);
        appliancecardView = findViewById(R.id.appliancesCardViewOne1);
        appliancecardView.setOnClickListener(this);
        movingTruckCardViewTwo = findViewById(R.id.movingTruckLoaderCardViewTwo);
        movingTruckCardViewTwo.setOnClickListener(this);
        washingCardViewThree1 = findViewById(R.id.washingMachineCardViewThree1);
        washingCardViewThree1.setOnClickListener(this);
        tutorcardViewThree = findViewById(R.id.tutorCardViewThree);
        tutorcardViewThree.setOnClickListener(this);
        ringcardViewTwo = findViewById(R.id.ringCardViewTwo1);
        ringcardViewTwo.setOnClickListener(this);
        homecleaning = findViewById(R.id.homecleaning);
        homeCleaningTextView = findViewById(R.id.homecleaningTextView);
        setSupportActionBar(toolbar);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);
        imageView=headerview.findViewById(R.id.imageView);
        img_str = AppConfig.BASE_URL + SharedPrefManager.getInstance(this).getUser().getImage();
        {
            try {
                if (SharedPrefManager.getInstance(this).getUser().getImage() == null)
                {
                    imageView.setImageResource(R.drawable.jihuzurblanklogo);
                    //     Glide.with(mCtx).load("http://192.168.1.105:7096/Images/Category/1.png").into(holder.categoryImage);
                } else {
                    Glide.with(CustomerHomePage.this)
                            .load(img_str)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(imageView);
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userName = headerview.findViewById(R.id.profileName);
        userContact =headerview.findViewById(R.id.cusContact);
        try{
        String uname=SharedPrefManager.getInstance(CustomerHomePage.this).getUser().getName().toString();
        String ucontact=SharedPrefManager.getInstance(CustomerHomePage.this).getUser().getMobileNo().toString();

/**/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(CustomerHomePage.this, DrawingActivity.class);
                startActivity(intent);
            }
        });
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
                userName.setText(dataProfile.getName().toString());
                userContact.setText(dataProfile.MobileNo);
                if (dataProfile != null) {
                    DataProfile dataProfile1 = new DataProfile();
                    dataProfile1.Image = dataProfile.Image;
                    dataProfile1.Name = dataProfile.Name;
                    dataProfile1.MobileNo = dataProfile.MobileNo;
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile1);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* For OnBackPRess in HomePage */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(CustomerHomePage.this);
            alertbox.setIcon(R.drawable.exit);
            alertbox.setTitle("Do You Want To Exit Programme?");
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // finish used for destroyed activity
                    finishAffinity();
                    System.exit(0);


                }
            });

            alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    // Nothing will be happened when clicked on no button
                    // of Dialog
                }
            });
            alertbox.show();
        }
        return super.onKeyDown(keyCode, event);
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
         try{   SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), RegistrationActivity.class);
            startActivity(login);
            finish();}
            catch (Exception e)
            {e.printStackTrace();}
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
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        Class framentClass = null;
        int id = item.getItemId();

        if (id == R.id.nav_booking) {
            intent=new Intent(CustomerHomePage.this,MyBokingsActivity.class);
            startActivity(intent);
        }
       /* else if (id == R.id.nav_review) {
                   getSupportActionBar().setTitle("My Review");
                    fragment = myBookingFragment;
                    framentClass = CustomerReviewFragment.class;

          }*/ else if (id == R.id.nav_complaint) {
            intent=new Intent(CustomerHomePage.this,CustomerComplaintActivity.class);
                ActivityOptions startAnimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.fui_slide_out_left,R.anim.fui_slide_in_right);

            startActivity(intent,startAnimation.toBundle());
        }  else if (id == R.id.nav_logout) {
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
        try {
            fragment = (Fragment) framentClass.newInstance();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.home_container, fragment).commit();
            setTitle("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(buttonClick);
        switch (v.getId()){

            case R.id.homecleaningCardViewOne:
//                check = true;
//                electricianCardView.setBackgroundColor(Color.WHITE);

                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",5);
                startActivity(intent);
                break;
            case R.id.appliancesCardViewOne1:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",4);
                startActivity(intent);
                break;
            case R.id.movingTruckLoaderCardViewTwo:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",1);
                startActivity(intent);
                break;
            case R.id.ringCardViewTwo1:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",3);
                startActivity(intent);
                break;
            case R.id.tutorCardViewThree:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",0);
                startActivity(intent);
                break;
            case R.id.washingMachineCardViewThree1:
                intent=new Intent(CustomerHomePage.this,MenuActivity.class);
                intent.putExtra("CategoryName",2);
                startActivity(intent);
                break;
        }

    }
}

