package com.example.awizom.jihuzur.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.awizom.jihuzur.Model.DataProfile;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "jihuzursharedpref";


    private static final String KEY_USER_Role = "userrole";
    private static final String KEY_USER_ID = "userID";
    private static final String KEY_USER_Name = "userName";
    private static final boolean KEY_USER_ACTIVE = false;

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

}
