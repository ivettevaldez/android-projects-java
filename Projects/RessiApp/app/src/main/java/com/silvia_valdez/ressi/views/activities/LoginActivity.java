package com.silvia_valdez.ressi.views.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.silvia_valdez.ressi.R;
import com.silvia_valdez.ressi.helpers.SessionManager;
import com.silvia_valdez.ressi.helpers.TypefaceHelper;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0; // Id to identity READ_CONTACTS permission request.

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String DRIVER_CREDENTIALS = "karen.gonzalez@hunabsys.com:driverpass";
    private static final String USER_CREDENTIALS = "silvia.valdez.e@gmail.com:userpass";

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            /*
              A dummy authentication store containing known user names and passwords.
              TODO: remove after connecting to a real authentication system.
            */
            DRIVER_CREDENTIALS, USER_CREDENTIALS
    };

    private Context mContext;
    private UserLoginTask mAuthTask = null; // Keep track of the login task so we can cancel it if requested.
    private int mSelectedId;

    // UI references.
    private AutoCompleteTextView mAutoEmail;
    private EditText mEditPassword;
    private TextView mTextForgotPassword;
    private Button mButtonEmailSignIn;
    private View mViewProgress;
    private View mViewLoginForm;


    /******************** APP LIFECYCLE ********************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();
        setUpTypefaces();
        addListenersToViews();
        populateAutoComplete(); // Set up the login form.
    }

    /******************** OVERRIDE METHODS ********************/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Callback received when a permissions request has been completed.
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                /*
                    Show primary email addresses first. Note that there won't be
                    a primary email address if the user hasn't specified one.
                */
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // Nothing to do here.
    }

    /******************** PRIVATE METHODS ********************/

    private void initVariables() {
        mContext = getApplicationContext();
        mAutoEmail = (AutoCompleteTextView) findViewById(R.id.login_auto_email);
        mEditPassword = (EditText) findViewById(R.id.login_edit_password);
        mTextForgotPassword = (TextView) findViewById(R.id.login_text_forgot_password);
        mButtonEmailSignIn = (Button) findViewById(R.id.login_button_sign_in);
        mViewLoginForm = findViewById(R.id.login_form);
        mViewProgress = findViewById(R.id.login_progress);
    }

    private void setUpTypefaces() {
        // Global typeface.
        TypefaceHelper typefaceHelper = new TypefaceHelper(LoginActivity.this);
        typefaceHelper.overrideAllTypefaces();

        // Specific typefaces.
        Typeface regular = typefaceHelper.getRobotoRegular();
        Typeface medium = typefaceHelper.getRobotoMedium();

        TextView textSignUp = (TextView) findViewById(R.id.login_text_sign_up);
        textSignUp.setTypeface(medium);
        mTextForgotPassword.setTypeface(medium);
        mButtonEmailSignIn.setTypeface(regular);
    }

    private void addListenersToViews() {
        LinearLayout layoutSignUp = (LinearLayout) findViewById(R.id.login_layout_sign_up);
        layoutSignUp.setOnClickListener(goToActivity);

        mTextForgotPassword.setOnClickListener(goToActivity);
        mEditPassword.setOnEditorActionListener(onEditorAction);
        mViewLoginForm.getViewTreeObserver().addOnGlobalLayoutListener(changeBackground);
        mButtonEmailSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mAutoEmail, R.string.msg_contacts_permission, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        // Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LoginActivity.this,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mAutoEmail.setAdapter(adapter);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.),
     * the errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAutoEmail.setError(null);
        mEditPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = mAutoEmail.getText().toString();
        String password = mEditPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mEditPassword.setError(getString(R.string.error_invalid_password));
            focusView = mEditPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mAutoEmail.setError(getString(R.string.error_field_required));
            focusView = mAutoEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mAutoEmail.setError(getString(R.string.error_invalid_email));
            focusView = mAutoEmail;
            cancel = true;
        }

        if (cancel) {
            /*
                There was an error; don't attempt login and focus
                the first form field with an error.
            */
            focusView.requestFocus();
        } else {
            /*
                Show a progress spinner, and kick off a background task
                to perform the user login attempt.
            */
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        /*
            On Honeycomb MR2 we have the ViewPropertyAnimator APIs,
            which allow for very easy animations. If available,
            use these APIs to fade-in the progress spinner.
        */
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mViewLoginForm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
        mViewLoginForm.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mViewLoginForm.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            }
        });

        mViewProgress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        mViewProgress.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mViewProgress.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    /******************** LISTENERS (PRIVATE METHODS) ********************/

    private View.OnClickListener goToActivity = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            if (view.getId() == R.id.login_text_forgot_password) {
                intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            } else if (view.getId() == R.id.login_layout_sign_up) {
                intent = new Intent(LoginActivity.this, SignUpActivity.class);
            }

            if (intent != null) {
                startActivity(intent);
            } else {
                Log.e(TAG, "Next activity not found.");
            }
        }
    };

    private TextView.OnEditorActionListener onEditorAction = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
            if (id == R.id.login || id == EditorInfo.IME_NULL) {
                attemptLogin();
                return true;
            }
            return false;
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener changeBackground = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            LinearLayout rootView = (LinearLayout) findViewById(R.id.login_layout);
            FrameLayout frameLogo = (FrameLayout) findViewById(R.id.login_frame_logo);
            int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

            if (heightDiff > 100) {
                // View height changed, so we change background to white when height size is small.
                frameLogo.setBackgroundResource(0);
            } else {
                // If height size is OK we set the original background again.
                frameLogo.setBackgroundResource(R.mipmap.img_bg_shapes);
            }
        }
    };

    /******************** INNER CLASSES & INTERFACES *********************/

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };
        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    private class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private static final int DELAY_TIME = 1000;

        private final String mEmail;
        private final String mPassword;


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try {
                // Simulate network access.
                Thread.sleep(DELAY_TIME);
            } catch (InterruptedException e) {
                return false;
            }

            for (int i = 0; i < DUMMY_CREDENTIALS.length; i++) {
                String credential = DUMMY_CREDENTIALS[i];
                String[] pieces = credential.split(":");

                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    mSelectedId = i + 1;
                    return pieces[1].equals(mPassword);
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                // TODO: Validate this session in SplashActivity
                SessionManager sessionManager = new SessionManager(mContext);
                sessionManager.saveSession(String.valueOf(mSelectedId));
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                mEditPassword.setError(getString(R.string.error_incorrect_password));
                mEditPassword.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }

    }

}

