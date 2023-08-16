package edu.northeastern.plantr;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


public class PlantrHomePage extends AppCompatActivity {

    private static final int NOTIFICATION_REQUEST_CODE = 101;

    @SuppressLint({"MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState){
        if(plantrAutologin.getUsername(this).length() == 0) {
            Intent newUserIntent = new Intent(this, PlantrLoginActivity.class);
            startActivity(newUserIntent);
        }
        super.onCreate(savedInstanceState);
        requestPermission(Manifest.permission.POST_NOTIFICATIONS, NOTIFICATION_REQUEST_CODE);
        setContentView(R.layout.activity_plantr_home_page);
        scheduleNotification(5000);
    }

    private void scheduleNotification(int delay){
        Intent notificationIntent = new Intent(this, notificationChannel.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0 , notificationIntent , PendingIntent.FLAG_IMMUTABLE);
        //TODO: set Time to correct time
        long futureInMillis = SystemClock.elapsedRealtime() + delay ;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE) ;
        assert alarmManager != null;
        alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , futureInMillis , pendingIntent) ;
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