package com.hunabsys.ubandcoach.view.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hunabsys.ubandcoach.R;
import com.hunabsys.ubandcoach.helpers.FontHelper;

public class RegistrationActivity extends Activity {

    private View mLoadingDialog;

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
    }

    private void setUpFonts() {
        // Global Font.
        FontHelper.overrideAllFonts(RegistrationActivity.this);

        // Other.
        Typeface semiBold = Typeface.createFromAsset(getAssets(), FontHelper.strTitilliumWebSemiBold);

        TextView txvBtnSend = (TextView) findViewById(R.id.registration_txv_send);
        TextView txvBtnCancel = (TextView) findViewById(R.id.registration_txv_cancel);

        txvBtnSend.setTypeface(semiBold);
        txvBtnCancel.setTypeface(semiBold);
    }

}
