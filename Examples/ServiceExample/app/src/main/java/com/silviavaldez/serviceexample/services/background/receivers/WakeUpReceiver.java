package com.silviavaldez.serviceexample.services.background.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.silviavaldez.serviceexample.helpers.LogFileHelper;
import com.silviavaldez.serviceexample.services.background.BackgroundService;

/**
 * Created by Silvia Valdez on 7/27/17.
 */

public class WakeUpReceiver extends BroadcastReceiver {

    public static final String WAKE_UP_ACTION = "com.silviavaldez.serviceexample.WAKE_UP_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String action = intent.getAction();
            if (WAKE_UP_ACTION.equals(action)) {
                // When alarm is fired, it starts the Location Service in background
                Intent broadcastIntent = new Intent(context, BackgroundService.class);
                context.startService(broadcastIntent);
            } else {
                LogFileHelper.writeLog(String.format("Not the expected action: %s", action));
            }
        } catch (Exception e) {
            LogFileHelper.writeLog(e.toString());
        }
    }

}
