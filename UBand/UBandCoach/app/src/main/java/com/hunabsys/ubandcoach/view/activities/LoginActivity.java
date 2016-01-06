package com.hunabsys.ubandcoach.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hunabsys.ubandcoach.R;
import com.hunabsys.ubandcoach.helpers.FontHelper;

public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private Context mContext;
    private TextView mTxvSignUp;

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
        mTxvSignUp = (TextView) findViewById(R.id.login_txv_sign_up);
        mTxvSignUp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void addListenersToInternalLoginButtons() {
        mTxvSignUp.setOnClickListener(new View.OnClickListener() {
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
