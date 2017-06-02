package com.silvia_valdez.hackathonapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silvia_valdez.hackathonapp.R;

public class SplashActivity extends Activity {

    private static final int DELAY_TIME = 2000;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initVariables();
        waitAMoment();
    }

    private void initVariables() {
        context = getApplicationContext();
    }

    private void waitAMoment() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);
                finish();
            }
        }, DELAY_TIME);
    }

}
