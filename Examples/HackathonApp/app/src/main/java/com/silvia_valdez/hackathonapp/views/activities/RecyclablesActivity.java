package com.silvia_valdez.hackathonapp.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.UtilHelper;

public class RecyclablesActivity extends AppCompatActivity {

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclables);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVariables();
        setActionBarTitle(getString(R.string.recyclables_activity_title));
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
            UtilHelper.changeActionBarTextColor(mContext, getSupportActionBar(), title);
        }
    }

    private void initVariables() {
        mContext = getApplicationContext();
    }

}
