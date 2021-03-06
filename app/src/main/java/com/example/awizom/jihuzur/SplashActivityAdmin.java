
package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.LoginRegistrationActivity.AdminRegistration;
import com.example.awizom.jihuzur.Util.SharedPrefManager;

public class SplashActivityAdmin extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 300;
    private Intent intent;
    String result = "";
    String id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_adminlayout);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("Admin")) {
                        {
                            intent = new Intent(SplashActivityAdmin.this, AdminHomePage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                        }
                    }
                    else {
                        Intent intent = new Intent(SplashActivityAdmin.this, AdminRegistration.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    }
                } else {
                    Intent intent = new Intent(SplashActivityAdmin.this, AdminRegistration.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                }
                // close this activity
              //  finish();
            }
        }, SPLASH_TIME_OUT);
    }



}
