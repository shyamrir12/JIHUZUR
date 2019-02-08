package com.example.awizom.jihuzur.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.awizom.jihuzur.Model.DataProfile;
import com.example.awizom.jihuzur.Model.PricingView;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "jihuzursharedpref";


    private static final String KEY_USER_Role = "userrole";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_Name = "userName";
    private static final boolean KEY_USER_ACTIVE = false;
    private static final String KEY_USER_ProfileImage = "Image";
    private static final String KEY_PRICING_ID= "PricingID";


    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(DataProfile user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, user.ID);
        editor.putString(KEY_USER_Role, user.Role);
        editor.putString(KEY_USER_Name, String.valueOf(user.Name));
        editor.putBoolean( String.valueOf( KEY_USER_ACTIVE ), user.Active);
        editor.putString(KEY_USER_ProfileImage,user.Image);
        editor.apply();
        return true;
    }



    public DataProfile getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        DataProfile token=new DataProfile();

        token.Role   =  sharedPreferences.getString(KEY_USER_Role, null);
        token.ID   =  sharedPreferences.getString(KEY_USER_ID, null);
        token.Name   =  sharedPreferences.getString(KEY_USER_Name, null);
        token.Active   =  sharedPreferences.getBoolean( String.valueOf( KEY_USER_ACTIVE ), false);
        token.Image   =  sharedPreferences.getString(KEY_USER_ProfileImage, null);
        return  token;
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_ID, null) != null)
            return true;
        return false;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public boolean checked(PricingView pricingView){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_PRICING_ID, pricingView.getPricingID());
        editor.apply();
        return true;
    }
    public PricingView getPricingID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        PricingView pricingView =new PricingView();
        pricingView.PricingID   =  sharedPreferences.getInt(KEY_PRICING_ID, 0);
        return pricingView;
    }

}
