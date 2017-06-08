package com.silvia_valdez.ressi_app.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Silvia Valdez on 6/4/17.
 */

public class RequestPermissionsHelper {

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static RequestPermissionsHelper sRequestPermissionsHelper;

    private Activity mActivity;

    private static final String[] ALL_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final String[] CAMERA_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private static final String[] LOCATION_PERMISSIONS = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };


    /******************** CONSTRUCTOR ********************/

    private RequestPermissionsHelper(Activity activity) {
        this.mActivity = activity;
    }

    /******************** PUBLIC METHODS ********************/

    public static RequestPermissionsHelper getInstance(Activity activity) {
        if (sRequestPermissionsHelper == null) {
            sRequestPermissionsHelper = new RequestPermissionsHelper(activity);
        }
        return sRequestPermissionsHelper;
    }

    public boolean verifyPermissions() {
        // Check if we have permissions for making phone calls
        int storage = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        int location = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (storage != PackageManager.PERMISSION_GRANTED
                || camera != PackageManager.PERMISSION_GRANTED
                || location != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so prompt the user
            ActivityCompat.requestPermissions(mActivity, ALL_PERMISSIONS, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyCameraPermissions() {
        // Check if we have permissions for making phone calls
        int storage = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);

        if (storage != PackageManager.PERMISSION_GRANTED
                || camera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so prompt the user
            ActivityCompat.requestPermissions(mActivity, CAMERA_PERMISSIONS, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

    public boolean verifyLocationPermissions() {
        // Check if we have storage permissions
        int fineLocation = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation = ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (fineLocation != PackageManager.PERMISSION_GRANTED
                || coarseLocation != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission, so prompt the user
            ActivityCompat.requestPermissions(mActivity, LOCATION_PERMISSIONS, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }
    }

}
