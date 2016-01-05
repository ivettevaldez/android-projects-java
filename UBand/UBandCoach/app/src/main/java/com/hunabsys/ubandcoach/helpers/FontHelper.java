package com.hunabsys.ubandcoach.helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Silvia Valdez on 04/01/16.
 */
public class FontHelper {

    // Fonts.
    public static String strVarelaRoundRegular = "fonts/varela_round_regular.ttf";
    public static String strTitilliumWebBold = "fonts/titillium_web_bold.ttf";
    public static String strTitilliumWebSemiBold = "fonts/titillium_web_semibold.ttf";
    public static String strTitilliumWebSemiBoldItalic = "fonts/titillium_web_semibold_italic.ttf";


    public static void overrideAllFonts(final Context context) {
        try {
            final View v = ((Activity) context).getWindow().getDecorView().getRootView();
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), strVarelaRoundRegular));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), strVarelaRoundRegular));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
