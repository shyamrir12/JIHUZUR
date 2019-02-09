package com.example.awizom.jihuzur.Service;

import android.app.Service;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class GPS_Service extends Service {
    private LocationListener listener;
    private LocationManager locationManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
