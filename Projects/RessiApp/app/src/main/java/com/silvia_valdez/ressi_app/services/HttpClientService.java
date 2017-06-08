package com.silvia_valdez.ressi_app.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.silvia_valdez.ressi_app.helpers.SessionManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Silvia Valdez on 6/3/17.
 */

public class HttpClientService {


    private static final String TAG = HttpClientService.class.getSimpleName();

    private static final String PRODUCTION_SERVER = "http://177.247.141.50:5000/";
    private static final String DEVELOPMENT_SERVER = "http://192.168.1.32:5000/";

    private static final int TIMEOUT = 15000;

    private Context mContext;
    private SessionManager mSessionManager;

    static final String APP_ID = "5";


    /******************** CONSTRUCTOR ********************/

    public HttpClientService(Context context) {
        mContext = context;
        mSessionManager = new SessionManager(mContext);
    }

    /******************** PUBLIC METHODS ********************/

    public String getServer() {
        return DEVELOPMENT_SERVER;
    }


    public Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public JSONObject performHttpGetObject(String strUrl) {
        try {
            HttpURLConnection connection;
            String response;

//            SessionManager sessionManager = new SessionManager(mContext);
//            String userId = sessionManager.getUser();
//            strUrl = String.format("%s%s", strUrl, userId);

            Log.i(TAG, String.format("Requested URL: %s", strUrl));

            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

//            String user = mSessionManager.getUser();
//            Log.e(TAG, String.format("GET request --> User: %s, Access token: %s, Client: %s",
//                    user, accessToken, client));

            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("access-token", accessToken);
//            connection.setRequestProperty("uid", user);
//            connection.setRequestProperty("client", client);
            connection.connect();

//            saveAuthenticationTokens(connection);
            response = validateResponse(connection);

            return new JSONObject(response);

        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }
        return null;
    }

    /******************** PRIVATE METHODS ********************/

    JSONObject performHttpPostObject(String strUrl, JSONObject paramsObject) {
        try {
            HttpURLConnection connection;
            DataOutputStream outputStream;
            byte[] paramsByteArray;
            String response;

            Log.i(TAG, String.format("Requested URL: %s", strUrl));

            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();

            // Send POST output.
            outputStream = new DataOutputStream(connection.getOutputStream());
            paramsByteArray = paramsObject.toString().getBytes("UTF-8");

            outputStream.write(paramsByteArray);
            outputStream.flush();
            outputStream.close();

//            saveAuthenticationTokens(connection);
            response = validateResponse(connection);

            return new JSONObject(response);

        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }
        return null;
    }

    // Exclusive method for Logout.
    JSONObject performHttpDeleteCall(String strUrl) {
        try {
            HttpURLConnection connection;
            String response;

            Log.i(TAG, String.format("Requested URL: %s", strUrl));

            URL url = new URL(strUrl);

//            String accessToken = mSessionManager.getAccessToken();
//            String user = mSessionManager.getUser();
//            String client = mSessionManager.getClient();
//            Log.e(TAG, String.format("Closing session --> User: %s, Access token: %s, Client: %s",
//                    user, accessToken, client));

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("access-token", accessToken);
//            connection.setRequestProperty("uid", user);
//            connection.setRequestProperty("client", client);
            connection.setDoOutput(false);
            connection.connect();

//            saveAuthenticationTokens(connection);
            response = validateResponse(connection);

            return new JSONObject(response);

        } catch (Exception e) {
            Log.e(TAG, String.valueOf(e));
        }
        return null;
    }

    private String validateResponse(HttpURLConnection connection) {
        String response = "";
        InputStream inputStream;

        try {
            int status = connection.getResponseCode();

            if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = new BufferedInputStream(connection.getErrorStream());
            } else {
                inputStream = new BufferedInputStream(connection.getInputStream());
            }

            response = IOUtils.toString(inputStream, "UTF-8");

            // Debug information.
            Log.d(TAG, String.format("RECEIVED RESPONSE: %s", response));

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

//    private String[] getAuthenticationTokens(HttpURLConnection connection) {
//        String[] tokens = {"", ""};
//
//        Map<String, List<String>> headerFields = connection.getHeaderFields();
//        Log.d(TAG, String.format("HEADER FIELDS: %s", headerFields.toString()));
//
//        List<String> accessTokenField = headerFields.get("access-token");
//        if (accessTokenField != null) {
//            tokens[0] = headerFields.get("access-token").get(0);
//            tokens[1] = headerFields.get("client").get(0);
//
//            Log.i(TAG, String.format("Access Token: %s, Client: %s", tokens[0], tokens[1]));
//        } else {
//            Log.e(TAG, mContext.getString(R.string.error_access_token_not_found));
//        }
//        return tokens;
//    }

//    private void saveAuthenticationTokens(HttpURLConnection connection) {
//        try {
//            int status = connection.getResponseCode();
//
//            if (status == HttpURLConnection.HTTP_BAD_REQUEST) {
//                Log.e(TAG, mContext.getString(R.string.error_invalid_access_token));
//            } else if (status == HttpURLConnection.HTTP_UNAUTHORIZED) {
//                Log.e(TAG, mContext.getString(R.string.error_unauthorized_access_token));
//            } else if (status == HttpURLConnection.HTTP_NOT_FOUND) {
//                Log.e(TAG, mContext.getString(R.string.error_access_token_not_found));
//            } else if (status == HttpURLConnection.HTTP_INTERNAL_ERROR) {
//                Log.e(TAG, mContext.getString(R.string.error_no_access_token));
//            } else if (status >= HttpURLConnection.HTTP_BAD_REQUEST) {
//                Log.e(TAG, mContext.getString(R.string.error_access_token_not_found));
//            } else {
//                String[] tokens = getAuthenticationTokens(connection);
//
//                if (tokens[0] != null && tokens[1] != null
//                        && !"".equals(tokens[1]) && !"".equals(tokens[0])) {
//                    SessionManager sessionManager = new SessionManager(mContext);
//                    sessionManager.setAccessTokens(tokens);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
