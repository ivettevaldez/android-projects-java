package com.silvia_valdez.hackathonapp.services.delegates;

import org.json.JSONObject;

/**
 * Interface for the MapCapturesServiceDelegate.
 * Created by Silvia Valdez on 4/27/17.
 */

public interface IRideRequestsServiceDelegate {

    void onRideRequestsSuccess(JSONObject requests);

    void onRideRequestsFail(String error);

}
