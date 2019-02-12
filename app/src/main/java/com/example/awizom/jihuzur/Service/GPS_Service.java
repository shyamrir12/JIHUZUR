package com.example.awizom.jihuzur.Service;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;

    private BroadcastReceiver broadcastReceiver;
    private MediaPlayer player;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
//        //setting loop play to true
//        //this will make the ringtone continuously playing
//        player.setLooping(true);
//
//        //staring the player
//        player.start();

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {



                // Intent i = new Intent( "location_update" );
                //i.putExtra( "coordinates", location.getLongitude() + " " + location.getLatitude() );
               // sendBroadcast( i );
                Log.d("myTag", "\n" +location.getLongitude() + " " + location.getLatitude());


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( i );
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService( Context.LOCATION_SERVICE );

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return START_NOT_STICKY;
        }
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 0, listener );



        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent( "location_update" );
                i.putExtra( "coordinates", location.getLongitude() + " " + location.getLatitude() );
                sendBroadcast( i );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity( i );
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService( Context.LOCATION_SERVICE );

        //noinspection MissingPermission
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 3000, 0, listener );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
