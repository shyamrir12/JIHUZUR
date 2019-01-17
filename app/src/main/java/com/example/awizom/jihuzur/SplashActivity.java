package com.example.awizom.jihuzur;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
    boolean active=false;
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
        mAuth=FirebaseAuth.getInstance();
        splashImageOut();


    }

    private void getUserProfile() {

            datauserprofile = FirebaseDatabase.getInstance().getReference("profile").child(userId);
           // Query query= FirebaseDatabase.getInstance().getReference("profile");
            datauserprofile.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    profile = dataSnapshot.getValue(ProfileModel.class);
                    role = profile.getRole().toString();
                    active = profile.isActive();
                    if(active == true){
                        getCheckuserRole();
                    }else {

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                        builder1.setMessage("You are not activated by Admin");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
//                                Toast.makeText(getApplicationContext(), "You are not activated by Admin", Toast.LENGTH_SHORT).show();

                    }



                        }




                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





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

                if (mAuth.getCurrentUser()!= null) {
                    userId=mAuth.getCurrentUser().getUid();
                   getUserProfile();
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }


            }
        },SPLASH_TIME_OUT);
    }

}