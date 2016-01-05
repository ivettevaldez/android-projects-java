package com.hunabsys.ubandcoach.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hunabsys.ubandcoach.R;
import com.hunabsys.ubandcoach.helpers.FontHelper;
import com.hunabsys.ubandcoach.pojos.User;
import com.hunabsys.ubandcoach.session.SessionManager;
import com.pnikosis.materialishprogress.ProgressWheel;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final int WHEEL_DELAY = 2000;
    private static final int START_INTENT_DELAY = 4500;

    private Context mContext;
    private SessionManager mSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initVariables();
        validateLogin();
        setupProgressWheel();

        // Setup Fonts.
        FontHelper.overrideAllFonts(SplashActivity.this);
    }

    private void initVariables() {
        mContext = getApplicationContext();
        mSessionManager = SessionManager.getInstance(mContext);

        User.typeFace = Typeface.createFromAsset(mContext.getAssets(),
                FontHelper.strVarelaRoundRegular);
    }

    private void validateLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSessionManager.isLoggedIn()) {
                    // mSessionManager.saveUserData(mContext);
                }
                SharedPreferences sharedPreferences = mSessionManager.getSharedPreferences();
                String loginType = sharedPreferences.getString(SessionManager.KEY_LOGIN_TYPE, "");
                mSessionManager.checkLogin(SplashActivity.this, loginType);
            }
        }, START_INTENT_DELAY);
    }

    private void setupProgressWheel() {
        final TextView txvCopyright = (TextView) findViewById(R.id.splash_txt_copyright);
        final ProgressWheel progressWheel = (ProgressWheel) findViewById(R.id.splash_progress_wheel);

        // ProgressWheel properties.
        progressWheel.setBarWidth(2);
        progressWheel.stopSpinning();
        progressWheel.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            // Hide the copyright legend and show up the progress wheel (loading).
            @Override
            public void run() {
                txvCopyright.setVisibility(View.INVISIBLE);
                progressWheel.spin();
                progressWheel.setVisibility(View.VISIBLE);
            }
        }, WHEEL_DELAY);
    }

}
