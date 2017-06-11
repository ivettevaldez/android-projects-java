package com.silvia_valdez.ressi_app.views.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.silvia_valdez.ressi_app.R;
import com.silvia_valdez.ressi_app.helpers.UtilHelper;
import com.silvia_valdez.ressi_app.views.adapters.DashboardPagerAdapter;
import com.silvia_valdez.ressi_app.views.adapters.ProgramRidePagerAdapter;
import com.silvia_valdez.ressi_app.views.adapters.ProgramRideStepperAdapter;
import com.silvia_valdez.ressi_app.views.fragments.QuantitiesFragment;
import com.silvia_valdez.ressi_app.views.fragments.RouteFragment;
import com.silvia_valdez.ressi_app.views.fragments.ScheduleFragment;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class ProgramRideActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        StepperLayout.StepperListener, QuantitiesFragment.OnFragmentInteractionListener,
        RouteFragment.OnFragmentInteractionListener, ScheduleFragment.OnFragmentInteractionListener {

    private Context mContext;
    private StepperLayout mStepperLayout;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link DashboardPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ProgramRidePagerAdapter mProgramRidePagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_ride);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initVariables();
//        setUpViewPager();
        setUpStepper();
        setActionBarTitle(getString(R.string.program_ride_activity_title));

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.action_open_navigation, R.string.action_close_navigation);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (mStepperLayout.getCurrentStepPosition() == 3) {
            UtilHelper.showToast(mContext, "Tu viaje fue programado");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else {
            super.onBackPressed();
        }
    }

    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            UtilHelper.setActionBarTitle(ProgramRideActivity.this, title);
        }
    }

    private void initVariables() {
        mContext = getApplicationContext();
    }

//    private void setUpViewPager() {
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mProgramRidePagerAdapter = new ProgramRidePagerAdapter(mContext, getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.program_ride_layout_container);
//        mViewPager.setAdapter(mProgramRidePagerAdapter);
//    }

    private void setUpStepper() {
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new ProgramRideStepperAdapter(getSupportFragmentManager(), mContext));
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

    @Override
    public void onCompleted(View completeButton) {
        this.finish();
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {
    }

    @Override
    public void onReturn() {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
