package com.silvia_valdez.ressi.helpers;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.silvia_valdez.ressi.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Utility class for generic purposes.
 * Created by Silvia Valdez on 31/05/2017.
 */

public class UtilHelper {

    private static Toast mToast;


    public static void showToast(Context context, String message) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void setActionBarTitle(AppCompatActivity activity, String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    public static void setActionBarTitleColor(Context context, ActionBar actionBar, String title) {
        // Set a title
        SpannableString spannableString = new SpannableString(title);
        // Also set a text color
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.charcoal_grey)),
                0,
                title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(spannableString);
        }
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
