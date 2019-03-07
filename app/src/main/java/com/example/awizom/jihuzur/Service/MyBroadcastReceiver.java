package com.example.awizom.jihuzur.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import com.example.awizom.jihuzur.CustomerActivity.CustomerHomePage;
import com.example.awizom.jihuzur.MyBokingsActivity;
import com.example.awizom.jihuzur.R;

 public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        mp=MediaPlayer.create(context, R.raw.nokia   );
        mp.start();
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        sendNotification(context);
    }

    public void sendNotification(Context context) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context);
        //Create the intent thatâ€™ll fire when the user taps the notification//

        Intent intent = new Intent(context, MyBokingsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_access_alarms_black_24dp);
        mBuilder.setContentTitle("Order Reminder");
        mBuilder.setContentText("Dear Customer, Your Order is 15 minutes left for complete 1 hour ");
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
}