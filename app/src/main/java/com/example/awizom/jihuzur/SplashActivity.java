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
import com.example.awizom.jihuzur.Helper.AdminHelper;
import com.example.awizom.jihuzur.LoginRegistrationActivity.LoginActivity;
import com.example.awizom.jihuzur.LoginRegistrationActivity.RegistrationActivity;
import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Util.SharedPrefManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private Intent intent;
    String result = "";
    String id;

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

                if (SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
                    if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("Employee")) {
                        id = SharedPrefManager.getInstance(getApplicationContext()).getUser().getID().toString();


                        if (SharedPrefManager.getInstance(getApplicationContext()).getUser().isActive()) {
                            intent = new Intent(SplashActivity.this, EmployeeHomePage.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                        } else {
                            getProfile();

                        }
                    } else if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("Customer")) {

                        intent = new Intent(SplashActivity.this, CustomerHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                    } else if (SharedPrefManager.getInstance(getApplicationContext()).getUser().getRole().equals("Admin")) {

                        intent = new Intent(SplashActivity.this, AdminHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);

                    } else {
                        Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    }
                } else {
                    Intent intent = new Intent(SplashActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void getProfile() {

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
                    dataProfile1.Active = dataProfile.Active;
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(dataProfile1);
                    if (dataProfile1.isActive()) {
                        intent = new Intent(SplashActivity.this, EmployeeHomePage.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out, R.anim.slide_in);
                    } else {
                        Toast.makeText(getApplicationContext(), "Contact Your Admin", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
