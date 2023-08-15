package edu.northeastern.plantr;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        //ToDo Set this to be a notification
        Intent alarmedIntent = new Intent(context, notificationChannel.class);
        context.startService(alarmedIntent);
    }

}
