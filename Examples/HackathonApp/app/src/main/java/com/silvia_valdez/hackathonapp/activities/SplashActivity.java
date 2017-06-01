package com.silvia_valdez.hackathonapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.UtilHelper;

public class SplashActivity extends AppCompatActivity {

    private static final int DELAY_TIME = 2000;

    private Context context;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initVariables();
        String activityTitle = getString(R.string.app_name);
        UtilHelper.changeActionBarTextColor(context, actionBar, activityTitle);
        waitAMoment();
    }

    private void initVariables() {
        context = getApplicationContext();
        actionBar = getSupportActionBar();
    }

    private void waitAMoment() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }

}
