package com.silvia_valdez.hackathonapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.silvia_valdez.hackathonapp.views.activities.LoginActivity;

/**
 * Created by Silvia Valdez on 6/3/17.
 */

public class SessionManager {

    public static final String PREF_USER = "PREF_USER";
//    public static final String PREF_CANDIDATE_ID = "PREF_CANDIDATE_ID";
//    public static final String PREF_ASSIGNED_CAMPAIGNS = "PREF_ASSIGNED_CAMPAIGNS";

    private static final String PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN";
//    private static final String PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN";
//    private static final String PREF_CLIENT = "PREF_CLIENT";
//    private static final String PREF_CANDIDATE_SELECTED = "PREF_CANDIDATE_SELECTED";

    private static TinyDB mTinyDB;


    /******************** CONSTRUCTOR ********************/

    public SessionManager(Context context) {
        mTinyDB = new TinyDB(context);
    }


    /******************** PUBLIC METHODS ********************/

    public boolean isLoggedIn() {
        return mTinyDB.getBoolean(PREF_IS_LOGGED_IN);
    }

//    public boolean isCandidateSelected() {
//        return mTinyDB.getBoolean(PREF_CANDIDATE_SELECTED);
//    }
//
//    public String getAccessToken() {
//        return mTinyDB.getString(PREF_ACCESS_TOKEN);
//    }
//
//    public String getClient() {
//        return mTinyDB.getString(PREF_CLIENT);
//    }

    public String getUser() {
        return mTinyDB.getString(PREF_USER);
    }

    public void saveSession(String user) {
        mTinyDB.putBoolean(PREF_IS_LOGGED_IN, true);
        mTinyDB.putString(PREF_USER, user);
    }

    public void saveUser(String user) {
        mTinyDB.putString(PREF_USER, user);
    }

//    public void saveCampaign(String selectedCandidateId, ArrayListAnySize<Candidate> candidatesArray) {
//        mTinyDB.putListCampaign(selectedCandidateId, candidatesArray);
//    }
//
//    public void saveAssignedCampaigns(ArrayList<String> campaignsArray) {
//        mTinyDB.putListString(PREF_ASSIGNED_CAMPAIGNS, campaignsArray);
//    }
//
//    public void setCandidate(String candidate) {
//        mTinyDB.putBoolean(PREF_CANDIDATE_SELECTED, true);
//        mTinyDB.putString(PREF_CANDIDATE_ID, candidate);
//    }
//
//    public void setAccessTokens(String[] tokens) {
//        mTinyDB.putString(PREF_ACCESS_TOKEN, tokens[0]);
//        mTinyDB.putString(PREF_CLIENT, tokens[1]);
//    }

    public void closeSession(final Activity mActivity) {
        mTinyDB.clear();

        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

}
