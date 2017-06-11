package com.silvia_valdez.ressi.services;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.silvia_valdez.ressi.R;
import com.silvia_valdez.ressi.helpers.JsonValidationHelper;
import com.silvia_valdez.ressi.services.delegates.IRideRequestsServiceDelegate;

import org.json.JSONObject;

/**
 * Created by Silvia Valdez on 6/4/17.
 */

public class RideRequestsService {

    private static final String TAG = RideRequestsService.class.getSimpleName();
    private static final String REQUESTS_URL = "/api/ressi/get_ride_requests";

    private Context mContext;
    private Fragment mFragment;
    private IRideRequestsServiceDelegate mDelegate;


    /******************** CONSTRUCTOR ********************/

    public RideRequestsService(Fragment fragment) {
        this.mFragment = fragment;
        this.mContext = fragment.getActivity().getApplicationContext();
        this.mDelegate = (IRideRequestsServiceDelegate) fragment;
    }

    /******************** PUBLIC METHODS ********************/

    public void getRideRequests(double latitude, double longitude) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpClientService httpClientService = new HttpClientService(mContext);

//                    String url = String.format("%s%s", httpClientService.getServer(), MAP_CAPTURES_URL);
                    String url = "https://api.myjson.com/bins/mzlxp";

                    final JSONObject response = httpClientService.performHttpGetObject(url);
                    validateRequestsResponse(response);
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /******************** PRIVATE METHODS ********************/

    private void validateRequestsResponse(JSONObject response) {
        try {
            if (response != null) {
                if (response.optJSONObject("captures") != null) {
                    JSONObject requests = response.getJSONObject("captures");
                    mDelegate.onRideRequestsSuccess(requests);
                } else {
                    onGettingRequestsFail(response);
                    Log.e(TAG, String.format("Invalid configuration found in: %s", response.toString()));
                }
            } else {
                Log.e(TAG, "NULL RESPONSE");
                String message = mContext.getResources().getString(R.string.error_standard_msg);
                mDelegate.onRideRequestsFail(message);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            onGettingRequestsFail(response);
        }
    }

    private void onGettingRequestsFail(JSONObject response) {
        String message;

        if (response != null) {
            if (response.has("errors")) {
                message = JsonValidationHelper.getErrorMessage(mFragment.getActivity(), response);
                mDelegate.onRideRequestsFail(message);
            } else {
                message = mContext.getResources().getString(R.string.error_standard_msg);
                mDelegate.onRideRequestsFail(message);
            }
        } else {
            Log.e(TAG, "NULL RESPONSE.");
            message = mContext.getResources().getString(R.string.error_standard_msg);
            mDelegate.onRideRequestsFail(message);
        }
    }

}
