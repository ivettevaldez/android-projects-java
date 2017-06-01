package com.silvia_valdez.hackathonapp.helpers;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.silvia_valdez.hackathonapp.R;

/**
 * Utility class for generic purposes.
 * Created by Silvia Valdez on 31/05/2017.
 */

public class UtilHelper {

    public static void changeActionBarTextColor(Context context, ActionBar actionBar, String title) {
        SpannableString spannableString = new SpannableString(title);
        spannableString.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(context, R.color.colorDark)), 0, title.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (actionBar != null) {
            actionBar.setTitle(spannableString);
        }
    }

}
