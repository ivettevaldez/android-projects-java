package com.silvia_valdez.ressi_app.helpers;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.silvia_valdez.ressi_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Helper to validate Json responses.
 * Created by Silvia Valdez on 24/02/16.
 */

public class JsonValidationHelper {

    private static final String TAG = JsonValidationHelper.class.getSimpleName();

    private static final int DELAY_LOGOUT = 2500;


    /******************** CONSTRUCTOR ********************/

    protected JsonValidationHelper() {
        // Nothing to do here
    }


    /**
     * ******************* PUBLIC METHODS ********************
     * <p>
     * Gets a String value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key                The name of the value.
     * @return A default value, just in case.
     */
    public static String getStringValue(JSONObject receivedJSONObject, String key) {
        String valueStr;
        try {
            valueStr = receivedJSONObject.getString(key);
            if (receivedJSONObject.has(key)
                    && valueStr != null
                    && !"null".equals(valueStr)) {
                return valueStr;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    /**
     * Gets an Integer value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key                The name of the value.
     * @return A default value, just in case.
     */
    public static Integer getIntValue(JSONObject receivedJSONObject, String key) {
        Integer value;
        try {
            value = receivedJSONObject.getInt(key);
            if (receivedJSONObject.has(key)) {
                return value;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return 0;
    }

    /**
     * Gets a Float value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key                The name of the value.
     * @return A default value, just in case.
     */
    public static Float getFloatValue(JSONObject receivedJSONObject, String key) {
        String valueStr;
        Float valueFloat;
        try {
            valueStr = receivedJSONObject.getString(key);

            if (receivedJSONObject.has(key)
                    && !"".equals(valueStr)
                    && !"null".equals(valueStr)) {
                valueFloat = Float.valueOf(valueStr);
                if (valueFloat != null) {
                    return valueFloat;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return (float) 0;
    }

    /**
     * Gets a Boolean value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key                The name of the value.
     * @return A default value, just in case.
     */
    public static Boolean getBooleanValue(JSONObject receivedJSONObject, String key) {
        Boolean value;
        try {
            value = receivedJSONObject.getBoolean(key);
            if (receivedJSONObject.has(key)) {
                return value;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return false;
    }


    public static String getErrorMessage(final Activity activity, JSONObject response) {
        String message = getStringArray(response, "errors");
        final Context context = activity.getApplicationContext();

        switch (message) {
            case "Authorized users only.":
                Log.e(TAG, "Session expired. Logging out...");

                Looper.prepare();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.closeSession(activity);
                    }
                }, DELAY_LOGOUT);
                Looper.loop();
                return "La sesión ha expirado, favor de iniciar una nueva sesión.";

            case "Internal Server Error":
                return context.getString(R.string.error_standard_msg);

            default:
                if (message.isEmpty()) {
                    message = context.getString(R.string.error_standard_msg);
                }
                return message;
        }
    }


    /**
     * ******************* PRIVATE METHODS ********************
     * <p>
     * Gets a String value from the given JSONObject.
     *
     * @param receivedJSONObject The JSONObject which contains the value.
     * @param key                The name of the value.
     * @return A default value, just in case.
     */
    private static String getStringArray(JSONObject receivedJSONObject, String key) {
        JSONArray responseArray;
        String result = "";
        try {
            if (receivedJSONObject.has(key)) {
                responseArray = receivedJSONObject.getJSONArray(key);
                if (responseArray != null) {
                    if (responseArray.length() > 1) {
                        for (int i = 0; i < responseArray.length(); i++) {
                            result = String.format("%s, %s", result, responseArray.get(i).toString());
                        }
                    } else {
                        result = responseArray.getString(0);
                    }
                    return result;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

}
