package com.silviavaldez.serviceexample.services.background;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.silviavaldez.serviceexample.helpers.LogFileHelper;

import java.util.Locale;

/**
 * Created by Silvia Valdez on 7/27/17.
 */

public class BackgroundService extends Service {

    private Context mContext;


    /******************** CONSTRUCTOR ********************/

    public BackgroundService() {
        // Nothing to do here.
    }

    /******************** SERVICE LIFECYCLE ********************/

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mContext = getApplicationContext();
        LogFileHelper.writeLog("OnCreate!");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogFileHelper.writeLog("OnStart!");
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        LogFileHelper.writeLog("OnTaskRemoved!");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        LogFileHelper.writeLog("OnDestroy!");
        super.onDestroy();
    }

    /******************** PRIVATE METHODS ********************/

    private void dummyTaskOnLoop() {
        int i = 1;
        try {
            while (true) {
                LogFileHelper.writeLog(String.format(Locale.getDefault(), "%d", i));
                i++;
            }
        } catch (Exception e) {
            LogFileHelper.writeLog(e.toString());
        }
    }

}
