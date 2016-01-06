package com.hunabsys.ubandcoach.view.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hunabsys.ubandcoach.R;
import com.hunabsys.ubandcoach.helpers.FontHelper;

public class RegistrationActivity extends Activity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();

    private View mLoadingDialog;
    private Button mBtnSignUp;
    private Button mBtnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initVariables();
        setUpFonts();
    }

    private void initVariables() {
        mLoadingDialog = findViewById(R.id.loading_dialog);
        mLoadingDialog.setVisibility(View.INVISIBLE);

        mBtnSignUp = (Button) findViewById(R.id.registration_btn_sign_up);
        mBtnCancel = (Button) findViewById(R.id.registration_btn_cancel);
    }

    private void setUpFonts() {
        // Global Font.
        FontHelper.overrideAllFonts(RegistrationActivity.this);

        // Other.
        Typeface semiBold = Typeface.createFromAsset(getAssets(), FontHelper.strTitilliumWebSemiBold);

        mBtnSignUp.setTypeface(semiBold);
        mBtnCancel.setTypeface(semiBold);
    }

}
