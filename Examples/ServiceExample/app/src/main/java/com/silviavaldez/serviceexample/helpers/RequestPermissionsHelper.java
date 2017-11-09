package com.silviavaldez.serviceexample.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Helper for checking permissions.
 * Created by Silvia Valdez on 11/11/16.
 */

public final class RequestPermissionsHelper {

    public static final int REQUEST_PERMISSIONS_ID = 1;

    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static RequestPermissionsHelper sRequestPermissionsHelper;

    private Activity mActivity;


    /******************** CONSTRUCTOR ********************/

    private RequestPermissionsHelper(Activity activity) {
        this.mActivity = activity;
    }

    /******************** PRIVATE METHODS ********************/

    static RequestPermissionsHelper getInstance(Activity activity) {
        if (sRequestPermissionsHelper == null) {
            sRequestPermissionsHelper = new RequestPermissionsHelper(activity);
        }
        return sRequestPermissionsHelper;
    }

    boolean verifyPermissions() {
        // Check if we have storage permissions
        int readExternal = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternal = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int fineLocation = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (readExternal != PackageManager.PERMISSION_GRANTED
                || writeExternal != PackageManager.PERMISSION_GRANTED
                || fineLocation != PackageManager.PERMISSION_GRANTED
                || coarseLocation != PackageManager.PERMISSION_GRANTED) {
            // We don't have all permissions, so prompt the user
            ActivityCompat.requestPermissions(mActivity, ALL_PERMISSIONS, REQUEST_PERMISSIONS_ID);
            return false;
        } else {
            return true;
        }
    }

}
