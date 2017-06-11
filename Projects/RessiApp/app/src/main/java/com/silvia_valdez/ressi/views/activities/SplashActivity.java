package com.silvia_valdez.ressi.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.silvia_valdez.ressi.R;
import com.silvia_valdez.ressi.helpers.TypefaceHelper;

public class SplashActivity extends Activity {

    private static final int DELAY_TIME = 1500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setUpTypefaces();
        waitAMoment();
    }

    private void setUpTypefaces() {
        // Global typeface.
        TypefaceHelper typefaceHelper = new TypefaceHelper(SplashActivity.this);
        typefaceHelper.overrideAllTypefaces();
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
