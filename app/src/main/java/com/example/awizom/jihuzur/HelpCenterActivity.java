package com.example.awizom.jihuzur;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.awizom.jihuzur.AdminActivity.AdminCusList;
import com.example.awizom.jihuzur.CustomerActivity.CustomerAdapter.CustomerPageAdapterBookings;
import com.example.awizom.jihuzur.CustomerActivity.CustomerNewChatBoat;
import com.example.awizom.jihuzur.CustomerActivity.TrackActivity;
import com.example.awizom.jihuzur.EmployeeActivity.AppSignatureHashHelper;
import com.example.awizom.jihuzur.Util.SharedPrefManager;

public class HelpCenterActivity extends AppCompatActivity {

    private ImageView call, chat;
    private String mobileNo = "";
    private TextView tollFreeNumber;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_helpcentre_layout);
        initview();
    }

    private void initview() {

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Help Center");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setSubtitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextAppearance(getApplicationContext(), R.style.styleA);
        toolbar.setTitleTextColor(Color.WHITE);
        call = findViewById(R.id.calling);
        chat = findViewById(R.id.chatting);

        tollFreeNumber = findViewById(R.id.tollNumber);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getInstance(HelpCenterActivity.this).getUser().getRole().equals("Customer"))
                {
                    Intent intent = new Intent(HelpCenterActivity.this, CustomerNewChatBoat.class);
                    startActivity(intent);
                } else if (SharedPrefManager.getInstance(HelpCenterActivity.this).getUser().getRole().equals("Admin")) {
                    Intent intent = new Intent(HelpCenterActivity.this, AdminCusList.class);
                    startActivity(intent);
                }
            }
        });

        try {
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.startAnimation(buttonClick);
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tollFreeNumber.getText().toString()));
                    if (ActivityCompat.checkSelfPermission(HelpCenterActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
