package com.example.awizom.jihuzur.CustomerActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import com.example.awizom.jihuzur.R;
import com.example.awizom.jihuzur.Util.SharedPrefManager;

public class CustomerSplashActivity  extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 6000;
    private Intent intent;
    String result = "";
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_splash);

        new Handler().postDelayed(new Runnable() {
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
            @Override
            public void run() {

                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                      if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("Customer")) {

                        intent = new Intent(CustomerSplashActivity.this, CustomerHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                          finish();

                    } else {
                        Intent intent = new Intent(CustomerSplashActivity.this, CustomerLoginRegActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                          finish();
                    }
                } else {
                    Intent intent = new Intent(CustomerSplashActivity.this, CustomerLoginRegActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    finish();
                }
                // close this activity

            }
        }, SPLASH_TIME_OUT);
    }



}
