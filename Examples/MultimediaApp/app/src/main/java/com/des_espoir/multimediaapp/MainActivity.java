package com.des_espoir.multimediaapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
    }

    private void initVariables() {
        TextView menuTitle = (TextView) findViewById(R.id.navbar_text_title);
        menuTitle.setText(getString(R.string.app_name));
    }

}
