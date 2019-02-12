package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.awizom.jihuzur.AdminActivity.AdminHomePage;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeActivity.EmployeeHomePage;
import com.example.awizom.jihuzur.LoginRegistrationActivity.LoginActivity;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                if(SharedPrefManager.getInstance( getApplicationContext() ).isLoggedIn()){

                    if(SharedPrefManager.getInstance( getApplicationContext()).getUser().getRole().equals( "Employee" )){

                        if(SharedPrefManager.getInstance( getApplicationContext() ).getUser().isActive()){
                            intent = new Intent( SplashActivity.this, EmployeeHomePage.class );
                            startActivity( intent );
                        }else {
                            Toast.makeText(getApplicationContext(),"Please Contact Your Admin",Toast.LENGTH_SHORT).show();
                        }

                    }else  if(SharedPrefManager.getInstance( getApplicationContext()).getUser().getRole().equals( "Customer" )){

                        intent = new Intent( SplashActivity.this, CustomerHomePage.class );
                        startActivity( intent );

                    }else  if(SharedPrefManager.getInstance( getApplicationContext()).getUser().getRole().equals( "Admin" )){

                        intent = new Intent( SplashActivity.this, AdminHomePage.class );
                        startActivity( intent );

                    }else {
                        Intent intent = new Intent(SplashActivity.this,RegistrationActivity.class);
                        startActivity(intent);
                    }
                }else {
                    Intent intent = new Intent(SplashActivity.this,RegistrationActivity.class);
                    startActivity(intent);
                }



                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
