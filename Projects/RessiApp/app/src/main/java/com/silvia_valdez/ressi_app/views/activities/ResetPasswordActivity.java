package com.silvia_valdez.ressi_app.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.silvia_valdez.ressi_app.R;
import com.silvia_valdez.ressi_app.helpers.UtilHelper;

public class ResetPasswordActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initVariables();
        setUpActionBar();
    }

    private void initVariables() {
        mContext = getApplicationContext();
    }

    private void setUpActionBar() {
        String activityTitle = getString(R.string.reset_pass_activity_title);
        UtilHelper.setActionBarTitle(ResetPasswordActivity.this, activityTitle);
    }

}
