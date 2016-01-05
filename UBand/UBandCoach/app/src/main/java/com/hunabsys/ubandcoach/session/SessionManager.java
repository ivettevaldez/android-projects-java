package com.hunabsys.ubandcoach.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hunabsys.ubandcoach.view.activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by Silvia Valdez  on 04/01/16.
 */
public class SessionManager {

    // Instance of SessionManager.
    private static SessionManager instance = null;

    // Shared Preferences.
    private SharedPreferences sharedPreferences;

    // Editor for Shared preferences.
    private SharedPreferences.Editor editor;

    // Context.
    private Context context;

    private String PREFERENCES_NAME;
    private int PRIVATE_MODE;

    // All Shared Preferences Keys.
    private final String IS_LOGGED_IN = "isLoggedIn";
    private final String IS_PROFILE_SAVED = "isProfileSaved";
    public final String KEY_ID = "id";
    public final String KEY_NAME = "name";
    public final String KEY_PICTURE = "picture";
    public static final String KEY_LOGIN_TYPE = "loginType";


    private SessionManager(Context context) {
        this.context = context;

        PREFERENCES_NAME = "UBandSession";
        PRIVATE_MODE = 0;
        sharedPreferences = getSharedPreferences();
        editor = sharedPreferences.edit();
    }

    /**
     * Get the instance of the class. If it hasn't been created, creates a new one;
     * else, it returns the created instance.
     */
    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
    }

    /**
     * Create login session.
     */
    public void createLoginSession(String id, String name, String picture, String loginType) {
        // Storing login values.
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putBoolean(IS_PROFILE_SAVED, false);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_PICTURE, picture);
        editor.putString(KEY_LOGIN_TYPE, loginType);

        // Commit changes.
        editor.commit();
        // saveUserData(context);
    }

    /**
     * Add the user's ID to preferences.
     */
    public void saveProfile() {
        // Storing id value.
        editor.putBoolean(IS_PROFILE_SAVED, true);

        // Commit changes.
        editor.commit();
    }

    /**
     * Check login method wil check user login status. If false,
     * it will redirect user to login page, else won't do anything.
     */
    public void checkLogin(Activity activity, String loginType) {
        // Check login status.
        if (this.isLoggedIn()) {
            if (isFirstLogin()) {
                // Starts a new intent to open Main Activity before Profile.
                // startMainIntent(loginType);

                // User has not saved his profile, redirect to Profile Activity.
                // startProfileIntent(loginType);
            } else {
                // User has saved his profile, redirect to Main Activity.
                // startMainIntent(loginType);
            }
        } else {
            // User is not logged in, redirect him to Login Activity.
            startLoginIntent();
        }
        activity.finish();
    }

    private void startLoginIntent() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
/*

    private void startProfileIntent(String loginType) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("loginType", loginType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void startMainIntent(String loginType) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("loginType", loginType);
        context.startActivity(intent);
    }
*/


    /**
     * Get stored session data.
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();

        user.put(KEY_ID, sharedPreferences.getString(KEY_ID, null));
        user.put(KEY_NAME, sharedPreferences.getString(KEY_NAME, null));
        user.put(KEY_PICTURE, sharedPreferences.getString(KEY_PICTURE, null));

        // Return user.
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent intent = new Intent(context, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Staring Login Activity.
        context.startActivity(intent);
    }

/*

    public void saveUserData(Context context) {
        // Save user data in User static class.
        HashMap<String, String> userData = getUserDetails();

        User.Id = userData.get(KEY_ID);
        User.Name = userData.get(KEY_NAME);
        User.ImageURL = userData.get(KEY_PICTURE);

        if (User.ImageURL.equals("training_image_5")) {
            User.ImageProfile = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.training_image_5);
        } else {
            User.loadImage();
        }
    }
*/

    /**
     * Quick check for login.
     **/
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Check if the user already saved his profile data (first login).
     */
    public boolean isFirstLogin() {
        return !sharedPreferences.getBoolean(IS_PROFILE_SAVED, false);
    }

}
