package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlantrHomePage extends AppCompatActivity {

    NotificationManager notificationManager;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;

    private static final int NOTIFICATION_REQUEST_CODE = 101;
    private static final String channelID = "channel101";

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState){

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, notifyIntent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 4);
        calendar.set(Calendar.SECOND, 14);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000, pendingIntent);

        //Set a notification
        requestPermission(Manifest.permission.POST_NOTIFICATIONS, NOTIFICATION_REQUEST_CODE);
        //requestPermission(Manifest.permission.SCHEDULE_EXACT_ALARM, ALARM_REQUEST_CODE);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //createNotificationChannel();

        if(plantrAutologin.getUsername(this).length() == 0) {
            Intent newUserIntent = new Intent(this, PlantrLoginActivity.class);
            startActivity(newUserIntent);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantr_home_page);
    }

    public void openMyPlants(View view){
        Intent myPlantsIntent = new Intent(this, MyPlantsActivity.class);
        startActivity(myPlantsIntent);
    }
    public void openProfile(View view){
        Intent profileIntent = new Intent(this, MyProfileActivity.class);
        startActivity(profileIntent);
    }
    public void openSettings(View view){
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
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

    public void sendNotification(String plantName){
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

    protected void requestPermission(String permissionType, int requestCode){
        int permission = ContextCompat.checkSelfPermission(this, permissionType);
        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[] {permissionType}, requestCode);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == NOTIFICATION_REQUEST_CODE){
            if(grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Notification permission required", Toast.LENGTH_LONG).show();
            }
        }
    }

}