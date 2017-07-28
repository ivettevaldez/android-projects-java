package com.silviavaldez.serviceexample.views.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.silviavaldez.serviceexample.R;
import com.silviavaldez.serviceexample.helpers.UtilHelper;
import com.silviavaldez.serviceexample.services.background.receivers.SleepReceiver;
import com.silviavaldez.serviceexample.services.background.receivers.WakeUpReceiver;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int ALARM_WAKE_UP_HOUR = 11;
    private static final int ALARM_SLEEP_HOUR = 11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            if (UtilHelper.hasPermissions(MainActivity.this)) {
                setUpWakeUpAlarmForService();
                setUpSleepAlarmForService();
            } else {
                Log.e(TAG, "No permissions for U");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpWakeUpAlarmForService() {
        Intent wakeUpIntent = new Intent(MainActivity.this, WakeUpReceiver.class);
        wakeUpIntent.setAction(WakeUpReceiver.WAKE_UP_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                0, wakeUpIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the alarm
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, ALARM_WAKE_UP_HOUR);
        calendar.set(Calendar.MINUTE, 45);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Log.e(TAG, "-----> Service started");
    }

    private void setUpSleepAlarmForService() {
        try {
            Intent sleepIntent = new Intent(MainActivity.this, SleepReceiver.class);
            sleepIntent.setAction(SleepReceiver.SLEEP_ACTION);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                    0, sleepIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            // Set the alarm
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, ALARM_SLEEP_HOUR);
            calendar.set(Calendar.MINUTE, 46);

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.cancel(pendingIntent);
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            Log.e(TAG, "-----> Service stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
