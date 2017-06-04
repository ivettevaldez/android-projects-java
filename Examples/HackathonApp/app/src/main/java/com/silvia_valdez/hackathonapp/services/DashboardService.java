package com.silvia_valdez.hackathonapp.services;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.JsonValidationHelper;
import com.silvia_valdez.hackathonapp.services.delegates.IDashboardServiceDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Silvia Valdez on 6/3/17.
 */

public class DashboardService {

    private static final String TAG = DashboardService.class.getSimpleName();

    private Activity mActivity;
    private Context mContext;
    private IDashboardServiceDelegate mDelegate;


    /******************** CONSTRUCTOR ********************/

    public DashboardService(Activity activity, IDashboardServiceDelegate delegate) {
        this.mActivity = activity;
        this.mContext = activity.getApplicationContext();
        this.mDelegate = delegate;
    }

    /******************** PUBLIC METHODS ********************/

    public void validateDashboardResponse(JSONObject response) {
        try {
            if (response != null) {
                onGettingDashboardSuccess(response);

            } else {
                Log.e(TAG, "NULL RESPONSE");
                mDelegate.receiveDashboardFail(mContext.getString(R.string.error_standard_msg));
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            onGettingDashboardFail(response);
        }
    }

    /******************** PRIVATE METHODS ********************/

    private void onGettingDashboardSuccess(JSONObject response) {
        try {
            int completedRides = 0;
            float accumulatedPoints = 0f;
            int rideId = 0;
            String rideDate = "";
            String rideTime = "";

            if (response.has("completedRides")) {
                completedRides = JsonValidationHelper.getIntValue(response, "completedRides");
            }
            if (response.has("accumulatedPoints")) {
                accumulatedPoints = JsonValidationHelper.getFloatValue(response, "accumulatedPoints");
            }
            if (response.has("scheduledRides")) {
                JSONArray scheduledRides = response.getJSONArray("scheduledRides");

                if (scheduledRides.length() != 0) {
                    JSONObject ride = scheduledRides.getJSONObject(0);

                    if (ride.has("id")) {
                        rideId = JsonValidationHelper.getIntValue(ride, "id");
                    }
                    if (ride.has("rideDate")) {
                        rideDate = JsonValidationHelper.getStringValue(ride, "rideDate");
                    }
                    if (ride.has("rideTime")) {
                        rideTime = JsonValidationHelper.getStringValue(ride, "rideTime");
                    }
                }
            }

            mDelegate.receiveDashboardSuccess(completedRides, accumulatedPoints, rideId, rideDate, rideTime);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            String message = mContext.getResources().getString(R.string.error_standard_msg);
            mDelegate.receiveDashboardFail(message);
        }
    }

    private void onGettingDashboardFail(JSONObject response) {
        String message;

        if (response != null) {
            if (response.has("errors")) {
                message = JsonValidationHelper.getErrorMessage(mActivity, response);
                mDelegate.receiveDashboardFail(message);
            } else {
                message = mContext.getResources().getString(R.string.error_standard_msg);
                mDelegate.receiveDashboardFail(message);
            }
        } else {
            Log.e(TAG, "NULL RESPONSE.");
            message = mContext.getResources().getString(R.string.error_standard_msg);
            mDelegate.receiveDashboardFail(message);
        }
    }

}
