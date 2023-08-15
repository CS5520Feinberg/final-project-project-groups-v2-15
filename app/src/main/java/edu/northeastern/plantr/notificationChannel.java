package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

public class notificationChannel extends BroadcastReceiver{
    NotificationManager notificationManager;
    private static final String NOTIFICATION_CHANNEL_ID="1001";
    public static String NOTIFICATION_ID = "101";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent){
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE) ;
            Notification notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Watering Time!")
                .setSmallIcon(R.drawable.ic_launcher_plantr_foreground)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentText("Basil needs water!")
                    //TODO: Add the pending intent to open MyPlantsActivity
                //.setContentIntent(pendingIntent)
                .build();
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(notificationChannel) ;
            notificationManager.notify(101 , notification);
            Log.w("notify called", "WOHA");
    }


}
