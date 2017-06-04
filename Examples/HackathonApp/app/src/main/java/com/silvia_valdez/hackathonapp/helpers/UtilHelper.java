package com.silvia_valdez.hackathonapp.helpers;

import android.content.Context;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.silvia_valdez.hackathonapp.R;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Utility class for generic purposes.
 * Created by Silvia Valdez on 31/05/2017.
 */

public class UtilHelper {

    public static void changeActionBarTextColor(Context context, ActionBar actionBar, String title) {
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(context, R.color.charcoal_grey)), 0, title.length(),
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
