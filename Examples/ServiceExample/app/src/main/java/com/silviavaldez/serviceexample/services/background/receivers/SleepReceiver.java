package com.silviavaldez.serviceexample.services.background.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.silviavaldez.serviceexample.helpers.LogFileHelper;
import com.silviavaldez.serviceexample.services.background.BackgroundService;

/**
 * Created by Silvia Valdez on 7/27/17.
 */

public class SleepReceiver extends BroadcastReceiver {

    public static final String SLEEP_ACTION = "com.silviavaldez.serviceexample.SLEEP_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (SLEEP_ACTION.equals(action)) {
                // When alarm is fired, it kills the Location Service in background
                Intent broadcastIntent = new Intent(context, BackgroundService.class);
                context.stopService(broadcastIntent);
            } else {
                LogFileHelper.writeLog(String.format("Not the expected action: %s", action));
            }
        } catch (Exception e) {
            LogFileHelper.writeLog(e.toString());
        }
    }

}
