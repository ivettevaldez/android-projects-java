package com.silvia_valdez.hackathonapp.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageButton;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.UtilHelper;
import com.silvia_valdez.hackathonapp.views.adapters.PagerAdapter;
import com.silvia_valdez.hackathonapp.views.controls.MenuTabs;
import com.silvia_valdez.hackathonapp.views.fragments.ProfileFragment;
import com.silvia_valdez.hackathonapp.views.fragments.RideFragment;

public class DashboardActivity extends AppCompatActivity implements RideFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Context mContext;
    private MenuTabs menuTabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVariables();
        setUpViewPager();
        setUpTabs();
        setActionBarTitle(getString(R.string.dashboard_fragment_title));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.action_open_navigation, R.string.action_close_navigation);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dashboard, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
//            getSupportActionBar().setTitle(title);
            UtilHelper.changeActionBarTextColor(mContext, getSupportActionBar(), title);
        }
    }

    private void initVariables() {
        mContext = getApplicationContext();
    }

    private void setUpViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mPagerAdapter = new PagerAdapter(mContext, getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.dashboard_layout_container);
        mViewPager.setAdapter(mPagerAdapter);
    }

    private void setUpTabs() {
        menuTabs = new MenuTabs(mViewPager);

        ImageButton button1 = (ImageButton) findViewById(R.id.tabs_button_1);
        ImageButton button2 = (ImageButton) findViewById(R.id.tabs_button_2);
        ImageButton button3 = (ImageButton) findViewById(R.id.tabs_button_3);
        ImageButton button4 = (ImageButton) findViewById(R.id.tabs_button_4);

        button1.setOnClickListener(menuTabs.goToSection);
        button2.setOnClickListener(menuTabs.goToSection);
        button3.setOnClickListener(menuTabs.goToSection);
        button4.setOnClickListener(menuTabs.goToSection);
    }

    @Override
    public void onFragmentInteraction(String title) {
        setActionBarTitle(title);
    }

}
