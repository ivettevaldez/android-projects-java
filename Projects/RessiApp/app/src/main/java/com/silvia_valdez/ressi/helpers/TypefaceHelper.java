package com.silvia_valdez.ressi.helpers;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Silvia Valdez on 6/2/17.
 */

public class TypefaceHelper {

    private static final String TAG = TypefaceHelper.class.getSimpleName();

    private Activity mActivity;

    private Typeface mRobotoMedium;
    private Typeface mRobotoRegular;
    private Typeface mRobotoLight;
    private Typeface mExoSoftLightItalic;
    private Typeface mExoSoftBold;
    private Typeface mExoSoftSemiBold;
    private Typeface mExoSoftThin;


    /******************** CONSTRUCTOR ********************/

    public TypefaceHelper(Activity activity) {
        this.mActivity = activity;
        AssetManager assets = activity.getResources().getAssets();

        String strRobotoMedium = "fonts/Roboto-Medium.ttf";
        this.mRobotoMedium = Typeface.createFromAsset(assets, strRobotoMedium);

        String strRobotoRegular = "fonts/Roboto-Regular.ttf";
        this.mRobotoRegular = Typeface.createFromAsset(assets, strRobotoRegular);

        String strRobotoLight = "fonts/Roboto-Light.ttf";
        this.mRobotoLight = Typeface.createFromAsset(assets, strRobotoLight);
//
//        String strExoSoftLighItalic = "fonts/exo_soft_light_italic.ttf";
//        this.mExoSoftLightItalic = Typeface.createFromAsset(activity.getResources().getAssets(), strExoSoftLighItalic);
//
//        String strExoSoftSemiBold = "fonts/exo_soft_semi_bold.ttf";
//        this.mExoSoftSemiBold = Typeface.createFromAsset(activity.getResources().getAssets(), strExoSoftSemiBold);
//
//        String strExoSoftBold = "fonts/exo_soft_bold.ttf";
//        this.mExoSoftBold = Typeface.createFromAsset(activity.getResources().getAssets(), strExoSoftBold);
//
//        String strJustAnotherHand = "fonts/just_another_hand.ttf";
//        this.mJustAnotherHand = Typeface.createFromAsset(activity.getResources().getAssets(), strJustAnotherHand);
//
//        String strExoSoftThin = "fonts/exo_soft_thin.ttf";
//        this.mExoSoftThin = Typeface.createFromAsset(activity.getResources().getAssets(), strExoSoftThin);
    }

    /******************** ACCESSORS (PUBLIC METHODS) ********************/

    public Typeface getRobotoMedium() {
        return mRobotoMedium;
    }

    public Typeface getRobotoRegular() {
        return mRobotoRegular;
    }

    public Typeface getRobotoLight() {
        return mRobotoLight;
    }

    public Typeface getExoSoftLightItalic() {
        return mExoSoftLightItalic;
    }

    public Typeface getExoSoftSemiBold() {
        return mExoSoftSemiBold;
    }

    public Typeface getExoSoftBold() {
        return mExoSoftBold;
    }

    public Typeface getExoSoftThin() {
        return mExoSoftThin;
    }


    /**
     * ******************* PUBLIC METHODS ********************
     * <p>
     * Overrides all fonts in a given layout with text of any kind in it.
     * The default font for the whole application is ExoSoftMedium.
     */
    public void overrideAllTypefaces() {
        try {
            View view = mActivity.getWindow().getDecorView().getRootView();
            overrideTypefaces(view, mRobotoRegular);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /******************** PRIVATE METHODS ********************/

    private void overrideTypefaces(View view, Typeface typeface) {
        try {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    overrideTypefaces(child, typeface);
                }
            } else if (view instanceof TextView) {
                ((TextView) view).setTypeface(typeface);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

}
