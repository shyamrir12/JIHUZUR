package com.example.awizom.jihuzur.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import static com.example.awizom.jihuzur.App.CHANNEL_ID;

public class AlarmService extends Service {

    FirebaseFirestore db;
    @Override
    public void onCreate() {
        super.onCreate();
        db=FirebaseFirestore.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        String orderid=intent.getStringExtra("orderId");
        final DocumentReference docRef=   db.collection("Order").document(orderid);
         docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
             @Override
             public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
            if(documentSnapshot.get("endTime")!=0)
            {
                onDestroy();
            }
             }
         });
        Intent notificationIntent = new Intent(AlarmService.this, CustomerHomePage.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(input)
                .setContentText("Order is Started")
                .setColor(Color.parseColor("#F5360C"))
                .setSmallIcon( R.drawable.jihuzurapplogo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
