package com.example.awizom.jihuzur.EmployeeActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.awizom.jihuzur.Config.AppConfig;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeAdapter.EmployeePageAdapter;
import com.example.awizom.jihuzur.HelpCenterActivity;
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.LoginRegistrationActivity.EmployeeRegistration;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Service.GPS_Service;
import com.example.awizom.jihuzur.Service.LocationMonitoringNotificationService;
import com.example.awizom.jihuzur.SettingsActivity;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.example.awizom.jihuzur.ViewDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.security.SecureRandom;

import javax.annotation.Nullable;

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
    ViewDialog viewDialog;
    private Fragment fragment = null;
    private Fragment helpCenterFragment;
    private Intent intent;
    private CardView homeCleaningCardView, appliancecardView;
    private TextView btn_start, btn_stop;
    private TextView textView;
    FirebaseFirestore db;
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
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    break;

                case R.id.navigation_booking:
                    intent = new Intent(EmployeeHomePage.this, EmployeeBookingsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    break;

                case R.id.navigation_helpCenter:
                    intent = new Intent(EmployeeHomePage.this, HelpCenterActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
//                    getSupportActionBar().setTitle("Help Center");
//                    fragment = helpCenterFragment;
//                    framentClass = HelpCenterFragment.class;
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
        db = FirebaseFirestore.getInstance();
        initView();
    }

    private void initView() {
        // searchFragment = new SearchFragment();

        setContentView(R.layout.activity_employee_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.jihuzzur_home_logo);
        toolbar.setTitle("");
        toolbar.setSubtitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(),R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);



        btn_start = findViewById(R.id.button);
        btn_stop = findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);
        tabLayout = findViewById(R.id.tablayout);
        outGoing = findViewById(R.id.outgoing);
        history = findViewById(R.id.history);
        viewDialog = new ViewDialog(this);
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

            try {
                enable_buttons();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        userName = headerview.findViewById(R.id.profileName);
        userContact = headerview.findViewById(R.id.empContact);
        identityNo = headerview.findViewById(R.id.identityNo);
        identityType = headerview.findViewById(R.id.identityType);
        final String id = SharedPrefManager.getInstance(this).getUser().getID();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(EmployeeHomePage.this, EmployeeMyProfileActivity.class);
                startActivity(intent);
            }
        });
        getProfile();
        final DocumentReference docRef = db.collection("OrderNotification").document(id);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("failed", "Listen failed.", e);
                    return;
                }
                String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("Snapshot data", source + " data: " + documentSnapshot.getData());
                    final Intent emptyIntent = new Intent(EmployeeHomePage.this,EmployeeHomePage.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(EmployeeHomePage.this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification noti = new Notification.Builder(EmployeeHomePage.this)
                            .setContentTitle("Hey! You Have Order")
                            .setContentText(String.valueOf("Jihuzzur, You Have One Order Click Here For Details.")).setSmallIcon(R.drawable.jihuzurapplogo)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    int randomNumber;
                    int range = 9;  // to generate a single number with this range, by default its 0..9
                    int length = 4;
                    SecureRandom secureRandom = new SecureRandom();
                    String s = "";
                    for (int i = 0; i < length; i++) {
                        int number = secureRandom.nextInt(range);
                        if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                            i = -1;
                            continue;
                        }
                        s = s + number;
                    }
                    randomNumber = Integer.parseInt(s);
                    notificationManager.notify(1000,noti);

                    db.collection("OrderNotification").document(id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });

                  Toast.makeText(getApplicationContext(),"Your Order Is",Toast.LENGTH_LONG).show();
                }

            }
        });

        final DocumentReference docRefs = db.collection("SendOrderPhoto").document(id);
        docRefs.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("failed", "Listen failed.", e);
                    return;
                }
                String source = documentSnapshot != null && documentSnapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.d("Snapshot data", source + " data: " + documentSnapshot.getData());
                    final Intent emptyIntent = new Intent(EmployeeHomePage.this,EmployeeHomePage.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(EmployeeHomePage.this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    Notification noti = new Notification.Builder(EmployeeHomePage.this)
                            .setContentTitle("Send Your Current Order Photo ")
                            .setContentText(String.valueOf("Jihuzzur,Hey! Admin Is wants to See Yoour Current Order Photo")).setSmallIcon(R.drawable.jihuzurapplogo)
                            .setContentIntent(pendingIntent)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .build();
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    // hide the notification after its selected
                    int randomNumber;
                    int range = 9;  // to generate a single number with this range, by default its 0..9
                    int length = 4;
                    SecureRandom secureRandom = new SecureRandom();
                    String s = "";
                    for (int i = 0; i < length; i++) {
                        int number = secureRandom.nextInt(range);
                        if (number == 0 && i == 0) { // to prevent the Zero to be the first number as then it will reduce the length of generated pin to three or even more if the second or third number came as zeros
                            i = -1;
                            continue;
                        }
                        s = s + number;
                    }
                    randomNumber = Integer.parseInt(s);
                    notificationManager.notify(260,noti);
                    db.collection("SendOrderPhoto").document(id)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                }

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
                userContact.setText(dataProfile.getMobileNo().toString());
                userName.setText(dataProfile.getName().toString());
                img_str = AppConfig.BASE_URL + dataProfile.getImage();
                Glide.with(EmployeeHomePage.this)
                        .load(img_str)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(imageView);
                //                    Glide.with(this).load(img_str).into(imageView);

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

    /* For OnBackPRess in HomePage */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder alertbox = new AlertDialog.Builder(EmployeeHomePage.this);
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

    public void enable_buttons() {
        try {
          /*  Intent serviceIntent = new Intent(EmployeeHomePage.this, LocationMonitoringNotificationService.class);
            serviceIntent.putExtra("inputExtra", "Location Service Enabled");
            ContextCompat.startForegroundService(EmployeeHomePage.this, serviceIntent);*/
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            startService(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
        final MenuItem settingsItem = menu.findItem(R.id.action_profile);


        Glide.with(this).load(img_str).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(new SimpleTarget<Bitmap>(100, 100) {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                settingsItem.setIcon(new BitmapDrawable(getResources(), getCroppedBitmap(resource)));
            }
        });
        return true;
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            Intent i = new Intent(EmployeeHomePage.this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_profile) {

            Intent i = new Intent(EmployeeHomePage.this, EmployeeMyProfileActivity.class);
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
            showCustomLoadingDialog();
            intent = new Intent(EmployeeHomePage.this, EmployeeMyProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_booking) {
           showCustomLoadingDialog();
            intent = new Intent(EmployeeHomePage.this, EmployeeBookingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_skill) {
            showCustomLoadingDialog();
            intent = new Intent(EmployeeHomePage.this, EmployeeSkillActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            showCustomLoadingDialog();
            SharedPrefManager.getInstance(this).logout();
            Intent login = new Intent(getApplicationContext(), EmployeeRegistration.class);
            startActivity(login);
            finish();

        } else if (id == R.id.nav_share) {
            showCustomLoadingDialog();
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } else if (id == R.id.nav_send) {
            showCustomLoadingDialog();
            String phoneNumber = "", message = "";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showCustomLoadingDialog() {

        //..show gif
        viewDialog.showDialog();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //...here i'm waiting 5 seconds before hiding the custom dialog
                //...you can do whenever you want or whenever your work is done
                viewDialog.hideDialog();
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}


