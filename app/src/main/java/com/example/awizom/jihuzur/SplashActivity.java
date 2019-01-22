package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.awizom.jihuzur.Model.DataProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SplashActivity extends AppCompatActivity {

    private  static int SPLASH_TIME_OUT=4000;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    DataProfile customerDataProfile;
    private String mVerificationId;
    String role="",userId="";
    boolean active=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initView();

    }

    private void initView() {


        splashImageOut();


    }





    private void splashImageOut() {

        ImageView imageView=findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

            }
        },SPLASH_TIME_OUT);
    }

}
