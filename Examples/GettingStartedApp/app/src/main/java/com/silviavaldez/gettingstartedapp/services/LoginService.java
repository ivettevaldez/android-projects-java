package com.silviavaldez.gettingstartedapp.services;

import android.content.Context;
import android.util.Log;

import com.silviavaldez.gettingstartedapp.R;
import com.silviavaldez.gettingstartedapp.services.delegates.ILoginServiceDelegate;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Example LoginService class.
 * Created by Silvia Valdez on 9/21/16.
 */
public class LoginService {

    private static final String TAG = LoginService.class.getSimpleName();
    private static final String LOGIN_SERVICE_URL = "https://api.myjson.com/bins/3e7po";

    private Context mContext;
    private ILoginServiceDelegate mDelegate;


    public LoginService(Context context, ILoginServiceDelegate delegate) {
        this.mContext = context;
        this.mDelegate = delegate;
    }

    public void validateLogin(String user, String password) {
        try {
            final String url = String.format("%s?user=%s&password=%s", LOGIN_SERVICE_URL, user, password);
            Log.d(TAG, url);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final JSONObject response = HttpClientService.performHttpGetObject(url);
                    validateLoginResponse(response);
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void validateLoginResponse(JSONObject jsonObject) {
        try {
            if (jsonObject != null
                    && (jsonObject.optJSONObject("response")) != null) {
                JSONObject response = jsonObject.getJSONObject("response");
                onLoginSuccess(response);
            } else {
                onLoginFail(jsonObject);

                if (jsonObject != null) {
                    Log.e(TAG, jsonObject.toString());
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            onLoginFail(jsonObject);
        }
    }

    private void onLoginSuccess(JSONObject response) {
        try {
            if (response.has("result")) {
                boolean isLoggedIn = response.getBoolean("result");
                mDelegate.onLoginSuccess(isLoggedIn);
            } else {
                Log.e(TAG, response.toString());
                onLoginFail(response);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
            onLoginFail(response);
        }
    }

    private void onLoginFail(JSONObject response) {
        try {
            String message = mContext.getResources().getString(R.string.error_standard_msg);
            if (response != null) {
                if (response.has("error")) {
                    message = response.getString("error");
                }
            } else {
                Log.e(TAG, "NULL RESPONSE.");
            }
            mDelegate.onLoginFail(message);

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

}
