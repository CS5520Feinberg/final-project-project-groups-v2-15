package edu.northeastern.plantr;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.view.View;

public class notificationChannel extends AppCompatActivity {
    NotificationManager notificationManager;
    private static final int NOTIFICATION_REQUEST_CODE = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        checkPermissions();
        createNotificationChannel();
    }

    protected void checkPermissions(){
        //int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_NOTIFICATIONS);
    }

    protected void createNotificationChannel(){
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        String idChannel = "edu.northeastern.plantr";
        String description = "Notify it is watering time";
        String name = "Plantr";
        NotificationChannel channel = new NotificationChannel(idChannel, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(View view, String plantName){
        String channelID = "edu.northeastern.plantr";
        int notificationID = 101;
        Intent resultIntent = new Intent(this, MyPlantsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)System.currentTimeMillis(), resultIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this, channelID)
                .setContentTitle("Watering Time!")
                .setSmallIcon(R.drawable.ic_launcher_plantr_foreground)
                .setChannelId(channelID)
                .setContentText(plantName + " needs water!")
                .setContentIntent(pendingIntent)
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(notificationID, notification);


    }
}
