package com.example.awizom.jihuzur.Service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;
import java.util.Date;

import static com.example.awizom.jihuzur.App.CHANNEL_ID;

public class AlarmService extends Service {

    FirebaseFirestore db;
    int i = 2700;
    Intent intentalarm;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    //boolean check=true;
    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseFirestore.getInstance();
        intentalarm = new Intent(this, MyBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, intentalarm, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        final String orderid = intent.getStringExtra("orderId");
        final NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        final DocumentReference docRef = db.collection("Order").document(orderid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (!String.valueOf(documentSnapshot.get("endTime")).equals("0")) {
                    // AlarmService.super.onDestroy();
                    //   check=false;
                    mNotificationManager.cancel(Integer.parseInt(orderid));
                    stopalert();
                }
            }
        });
        // if(check==true) {


        final Intent emptyIntent = new Intent(AlarmService.this, MyBokingsActivity.class);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.shopping)
                .setContentTitle("Jihuzzur")
                .setUsesChronometer(true)
                .setContentText("Order is Started");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        /*   stackBuilder.addNextIntent(intent);*/
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(String.valueOf(orderid)), emptyIntent, 0);
        mBuilder.setContentIntent(pendingIntent);
        notificationManager.notify(Integer.parseInt(String.valueOf(orderid)), mBuilder.build());

       // Toast.makeText(getApplicationContext(), randomNumber + " number", Toast.LENGTH_LONG).show();

       // return randomNumber;



       /* Intent notificationIntent = new Intent(AlarmService.this, MyBokingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, Integer.parseInt(String.valueOf(orderid)), notificationIntent, 0);
        Date currentTime = Calendar.getInstance().getTime();
        Long setthewn = System.currentTimeMillis();
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(input)
                .setContentText("Order is Started")
                .setColor(Color.parseColor("#F5360C"))
                .setWhen(setthewn)  // the time stamp, you will probably use System.currentTimeMillis() for most scenarios
                .setUsesChronometer(true)
                .setSmallIcon(R.drawable.shopping)
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        //Id allows you to update the notification later on.
        mNotificationManager.notify(Integer.parseInt(String.valueOf(orderid)), notification);
        startAlert();*/
        return START_NOT_STICKY;
    }

    private void startAlert() {
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (i * 1000), pendingIntent);
     /*   alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
*/
        Toast.makeText(this, "Alarm set in " + i + " seconds", Toast.LENGTH_LONG).show();
    }

    private void stopalert() {
        alarmManager.cancel(pendingIntent);
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