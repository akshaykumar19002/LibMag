package com.kumar.akshay.libmag.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class
MyNotificationBroadcastReciever extends BroadcastReceiver {

    public static String TAG = "NotificationBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {

            // It is better to reset alarms using Background IntentService
            Intent i = new Intent(context, NotificationService.class);
            ComponentName service = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                service = context.startForegroundService(i);
            }else
                service = context.startService(i);

            // Set the alarm to start at approximately 2:00 p.m.
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);

            // With setInexactRepeating(), you have to use one of the AlarmManager interval
            // constants--in this case, AlarmManager.INTERVAL_DAY.
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            PendingIntent alarmIntent = PendingIntent.getService(context, 0, new Intent(context, NotificationService.class), 0);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

            if (null == service) {
                // something really wrong here
                Log.e(TAG, "Could not start service ");
            }
            else {
                Log.e(TAG, "Successfully started service ");
            }

        } else {
            Log.e(TAG, "Received unexpected intent " + intent.toString());
        }
    }
}
