package com.silviavaldez.gettingstartedapp.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.silviavaldez.gettingstartedapp.BuildConfig;

import org.json.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Silvia on 9/21/16.
 */
public class HttpClientService {

    private static final String TAG = HttpClientService.class.getSimpleName();


    public static Boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static JSONObject performHttpGetObject(String strUrl) {
        try {
            HttpURLConnection connection;
            String response;

            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            response = validateResponse(connection);

            return new JSONObject(response);

        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }
        return null;
    }

    private static String validateResponse(HttpURLConnection connection) {
        String response = "";
        InputStream inputStream;

        try {
            int status = connection.getResponseCode();

            if (status >= HttpURLConnection.HTTP_BAD_REQUEST)
                inputStream = new BufferedInputStream(connection.getErrorStream());
            else
                inputStream = new BufferedInputStream(connection.getInputStream());

            response = IOUtils.toString(inputStream, "UTF-8");

            // Debug information.
            if (BuildConfig.DEBUG) Log.d(TAG, "<JSONObject>" + response + "</JSONObject>");

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

}
