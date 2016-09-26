package com.silviavaldez.gettingstartedapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.silviavaldez.gettingstartedapp.R;
import com.silviavaldez.gettingstartedapp.daos.UserDao;
import com.silviavaldez.gettingstartedapp.datamodels.User;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREF_USER_DATA = "PREF_USER_DATA";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_USER_PASS = "PREF_USER_PASS";
    public static final String PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN";

    private Context mContext;
    private ListView mListUsers;
    private DrawerLayout mDrawerLayout;
    private ArrayList<String> mArrayUsers;

    private UserDao mUserDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Intent settings = new Intent(Settings.ACTION_SETTINGS);
                startActivity(settings);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Custom methods
        initVariables();
        showExtras();
    }

    private void initVariables() {
        mContext = getApplicationContext();
        mListUsers = (ListView) findViewById(R.id.main_list_users);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mArrayUsers = new ArrayList<>();

        mUserDao = new UserDao(mContext);
    }

    /**
     * SHARED PREFERENCES
     */
    private void showExtras() {
        Bundle extras = this.getIntent().getExtras();
        String name = extras.getString(LoginActivity.EXTRA_USER);
        String pass = extras.getString(LoginActivity.EXTRA_PASS);

        saveSession(name, pass);    // Shared Preferences
        persistUser(name, pass);    // Realm

        mArrayUsers.add(name);
        mArrayUsers.add("Karen");
        mArrayUsers.add("Mano");
        mArrayUsers.add("Jorge");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                R.layout.item_list_view,
                mArrayUsers);
        mListUsers.setAdapter(adapter);

//        Toast.makeText(mContext, name + " - " + pass, Toast.LENGTH_SHORT).show();
    }

    private void saveSession(String name, String pass) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_USER_DATA, MODE_PRIVATE).edit();
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_USER_PASS, pass);
        editor.putBoolean(PREF_IS_LOGGED_IN, true);
        editor.apply();
    }

    /**
     * PERSISTENCE
     *
     * @param name User name
     * @param pass User password
     */
    private void persistUser(String name, String pass) {
        User user;
        if ((user = mUserDao.getUserByName(name)) == null) {
            user = new User();
            user.setId(mUserDao.getLastId());
            user.setCreatedAt(new Date());
        }
        user.setName(name);
        user.setPassword(pass);

        mUserDao.createOrUpdate(user);
        showSavedData(name);
    }

    private void showSavedData(String name) {
        User user;
        if ((user = mUserDao.getUserByName(name)) != null) {
            String userData = String.format("%s - %s - %s", user.getName(), user.getPassword(), user.getCreatedAt());
            Snackbar.make(mDrawerLayout, userData, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(mDrawerLayout, "No user found", Snackbar.LENGTH_SHORT).show();
        }
    }


    /**
     * INTERFACES IMPLEMENTATIONS
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            SharedPreferences.Editor editor = getSharedPreferences(PREF_USER_DATA, MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
