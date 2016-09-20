package com.silviavaldez.gettingstartedapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String PREF_USER_DATA = "PREF_USER_DATA";
    public static final String PREF_USER_NAME = "PREF_USER_NAME";
    public static final String PREF_USER_PASS = "PREF_USER_PASS";
    public static final String PREF_IS_LOGGED_IN = "PREF_IS_LOGGED_IN";

    private Context mContext;
    private ListView mListUsers;
    private ArrayList<String> mArrayUsers;


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
        mArrayUsers = new ArrayList<>();
    }

    private void showExtras() {
        Bundle extras = this.getIntent().getExtras();
        String user = extras.getString(LoginActivity.EXTRA_USER);
        String pass = extras.getString(LoginActivity.EXTRA_PASS);

        saveSession(user, pass);

        mArrayUsers.add(user);
        mArrayUsers.add("Karen");
        mArrayUsers.add("Mano");
        mArrayUsers.add("Jorge");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                R.layout.item_list_view,
                mArrayUsers);
        mListUsers.setAdapter(adapter);

        Toast.makeText(mContext, user + " - " + pass, Toast.LENGTH_SHORT).show();
    }

    private void saveSession(String name, String pass) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_USER_DATA, MODE_PRIVATE).edit();
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_USER_PASS, pass);
        editor.putBoolean(PREF_IS_LOGGED_IN, true);
        editor.apply();
    }

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
        if (id == R.id.action_settings) {
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
