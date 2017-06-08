package com.silvia_valdez.ressi_app.services.delegates;

/**
 * Created by Silvia Valdez on 6/3/17.
 */

public interface IDashboardServiceDelegate {

    void receiveDashboardSuccess(int completedRides, float accumulatedPoints, int rideId,
                                 String rideDate, String rideTime);

    void receiveDashboardFail(String error);

}
