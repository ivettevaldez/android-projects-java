package com.hunabsys.ubanduserapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int WHEEL_DELAY = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        setupProgressWheel();

        // Setup Fonts.
        Util.overrideAllFonts(SplashActivity.this);
    }

    private void setupProgressWheel() {
        final TextView copyright = (TextView) findViewById(R.id.splash_txt_copyright);
        final ProgressWheel wheel = (ProgressWheel) findViewById(R.id.splash_progress_wheel);

        wheel.setBarWidth(2);
        wheel.stopSpinning();
        wheel.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                copyright.setVisibility(View.INVISIBLE);
                wheel.spin();
                wheel.setVisibility(View.VISIBLE);
            }
        }, WHEEL_DELAY);
    }

}
