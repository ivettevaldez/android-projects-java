package com.silvia_valdez.hackathonapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.UtilHelper;

public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initVariables();
        String activityTitle = getString(R.string.sign_up_activity_title);
        UtilHelper.changeActionBarTextColor(context, actionBar, activityTitle);
    }

    private void initVariables() {
        context = getApplicationContext();
        actionBar = getSupportActionBar();
    }

}
