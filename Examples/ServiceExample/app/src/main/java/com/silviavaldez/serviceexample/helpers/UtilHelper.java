package com.silviavaldez.serviceexample.helpers;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Utilities helper.
 * Created by Silvia Valdez on 7/11/17.
 */

public final class UtilHelper {

    private static Toast sToast;


    /******************** CONSTRUCTOR ********************/

    protected UtilHelper() {
        // Nothing to do here.
    }

    /******************** PUBLIC METHODS ********************/

    public static boolean hasPermissions(Activity activity) {
        RequestPermissionsHelper permissionsHelper = RequestPermissionsHelper.getInstance(activity);
        return permissionsHelper.verifyPermissions();
    }

    public static boolean isValidPhone(String phone) {
        String regex = "[0-9]{10}$";
        return !TextUtils.isEmpty(phone) && phone.matches(regex);
    }

    public static void showToast(Context context, String message) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        sToast.show();
    }

    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

}
