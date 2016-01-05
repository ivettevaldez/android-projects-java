package com.hunabsys.ubandcoach.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hunabsys.ubandcoach.R;
import com.hunabsys.ubandcoach.helpers.FontHelper;

public class LoginActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();
        addListenersToInternalLoginButtons();

        // Setup Fonts.
        FontHelper.overrideAllFonts(LoginActivity.this);
    }

    private void initVariables() {
        mContext = getApplicationContext();

        // Underline TextView's text.
        TextView txvRegister = (TextView) findViewById(R.id.login_txv_register);
        txvRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void addListenersToInternalLoginButtons() {
        FrameLayout txvRegister = (FrameLayout) findViewById(R.id.login_frame_register);
        txvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(mContext, RegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
