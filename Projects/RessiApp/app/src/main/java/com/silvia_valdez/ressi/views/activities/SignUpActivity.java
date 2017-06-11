package com.silvia_valdez.ressi.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.silvia_valdez.ressi.R;
import com.silvia_valdez.ressi.helpers.UtilHelper;

public class SignUpActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initVariables();
        setUpActionBar();
    }

    private void initVariables() {
        mContext = getApplicationContext();
    }

    private void setUpActionBar() {
        String activityTitle = getString(R.string.sign_up_activity_title);
        UtilHelper.setActionBarTitle(SignUpActivity.this, activityTitle);
    }

}
