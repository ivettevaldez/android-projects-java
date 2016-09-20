package com.silviavaldez.gettingstartedapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "com.silviavaldez.gettingstartedapp.EXTRA_USER";
    public static final String EXTRA_PASS = "com.silviavaldez.gettingstartedapp.EXTRA_PASS";

    private Context mContext;
    private EditText mEditUser;
    private EditText mEditPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();
        getSession();
    }

    private void initVariables() {
        mContext = getApplicationContext();

        mEditUser = (EditText) findViewById(R.id.login_edit_user);
        mEditPass = (EditText) findViewById(R.id.login_edit_password);

        Button buttonGo = (Button) findViewById(R.id.login_button_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, "Showing Toast!", Toast.LENGTH_SHORT).show();
//                Snackbar.make(view, "Showing SnackBar!", Snackbar.LENGTH_SHORT).show();

                if (validateUserData()) {
                    String strUser = mEditUser.getText().toString();
                    String strPass = mEditPass.getText().toString();

                    goToMainActivity(strUser, strPass);

                } else {
                    Snackbar.make(view, "Should fill both fields", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToMainActivity(String user, String password) {
        // Put extras into the intent
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_USER, user);
        extras.putString(EXTRA_PASS, password);
        intent.putExtras(extras);

        startActivity(intent);
        finish();
    }

    private boolean validateUserData() {
        return !(mEditUser.getText().toString().isEmpty() || mEditPass.getText().toString().isEmpty());
    }

    private void getSession() {
        SharedPreferences preferences = getSharedPreferences(MainActivity.PREF_USER_DATA, MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean(MainActivity.PREF_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            String name = preferences.getString(MainActivity.PREF_USER_NAME, "No name defined");
            String pass = preferences.getString(MainActivity.PREF_USER_PASS, "No password defined");
            Toast.makeText(mContext, String.format("Hello %s!", name), Toast.LENGTH_SHORT).show();
            goToMainActivity(name, pass);
        }
    }

}
