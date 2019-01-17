package com.example.awizom.jihuzur.Helper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.example.awizom.jihuzur.AdminHomePage;
import com.example.awizom.jihuzur.CustomerHomePage;
import com.example.awizom.jihuzur.EmployeeHomePage;
import com.example.awizom.jihuzur.LoginActivity;
import com.example.awizom.jihuzur.Model.Profile;
import com.example.awizom.jihuzur.Model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginHelper extends AppCompatActivity{



    DatabaseReference datauserprofile;

    Profile customerProfile;
    private String mVerificationId;
    String role="",userId="";
    boolean active=false;
    List<ProfileModel> profilelist;
    ProfileModel profile;

    private void getUserProfile() {
        try {

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
}
