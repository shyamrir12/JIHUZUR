package com.example.awizom.jihuzur;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.awizom.jihuzur.Model.Profile;
import com.example.awizom.jihuzur.Model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private  static int SPLASH_TIME_OUT=4000;
    DatabaseReference datauserprofile;
    private FirebaseAuth mAuth;
    Profile customerProfile;
    private String mVerificationId;
    String role="",userId="";
    List<ProfileModel> profilelist;
    ProfileModel profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        initView();

    }

    private void initView() {

        profilelist = new ArrayList<>();
        splashImageOut();

        try {
            datauserprofile = FirebaseDatabase.getInstance().getReference("profile");
            mAuth = FirebaseAuth.getInstance();
            userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            getUserProfile();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getUserProfile() {
        try {

            datauserprofile = FirebaseDatabase.getInstance().getReference("profile");
            Query query= FirebaseDatabase.getInstance().getReference("profile");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try{
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            profile = postSnapshot.getValue(ProfileModel.class);
                            profilelist.add(profile);
                            if(!profilelist.equals(null)) {
                                for(int i=0 ; i<profilelist.size(); i++) {
                                    role = String.valueOf(profilelist.get(i).getRole());

                                    getCheckuserRole();//If User already logged in
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getCheckuserRole() {

       //Here checked user login
        if(role.equals("Customer")){
            Intent intent = new Intent(getApplicationContext(), CustomerHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(role.equals("Employee")){
            Intent intent = new Intent(getApplicationContext(), EmployeeHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }else if(role.equals("Admin")){
            Intent intent = new Intent(getApplicationContext(), AdminHomePage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void splashImageOut() {
        ImageView imageView=findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(userId.equals(null)) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    finish();
                }else if(!userId.equals(null)){
                    getCheckuserRole();
                }
            }
        },SPLASH_TIME_OUT);
    }

}
