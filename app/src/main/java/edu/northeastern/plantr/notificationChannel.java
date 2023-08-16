package edu.northeastern.plantr;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class notificationChannel extends BroadcastReceiver{
    private static final String NOTIFICATION_CHANNEL_ID="1001";

    public void onReceive(Context context, Intent intent){
            Log.w("onReceive Called", "Time");
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            Intent resultIntent = new Intent(context, MyPlantsActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_IMMUTABLE);

            Notification notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Watering Time!")
                .setSmallIcon(R.drawable.ic_launcher_plantr_foreground)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setContentText("Remember to Water Your Plants Today!")
                .setContentIntent(pendingIntent)
                .build();
            int importance = NotificationManager. IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME" , importance) ;
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(notificationChannel) ;
            notificationManager.notify(101 , notification);
    }


}
