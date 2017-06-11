package com.silvia_valdez.ressi.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.silvia_valdez.ressi.R;

public class IntroActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Context context;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        initVariables();
        setUpViewPager();
        addListenersToViews();

//        String activityTitle = getString(R.string.intro_activity_title);
//        UtilHelper.changeActionBarTextColor(context, actionBar, activityTitle);
    }

    private void setUpViewPager() {
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.intro_layout_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_intro, container, false);
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            int color = ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright); // Default

            switch (sectionNumber) {
                case 1:
                    color = ContextCompat.getColor(getContext(), android.R.color.holo_blue_bright);
                    break;

                case 2:
                    color = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
                    break;

                case 3:
                    color = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
                    break;
            }
            rootView.setBackgroundColor(color);
            return rootView;

            // TextView textView = (TextView) rootView.findViewById(R.id.frag_intro_text_section);
            // textView.setText(getString(R.string.intro_section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DashboardFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private void initVariables() {
        context = getApplicationContext();
        actionBar = getSupportActionBar();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    private void addListenersToViews() {
        Button buttonSignUp = (Button) findViewById(R.id.intro_button_sign_up);
        buttonSignUp.setOnClickListener(goToActivity);

        Button buttonSignIn = (Button) findViewById(R.id.intro_button_sign_in);
        buttonSignIn.setOnClickListener(goToActivity);
    }

    private View.OnClickListener goToActivity = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tag = ((Button) view).getText().toString();
            Intent intent;
            if (tag.equals(getString(R.string.action_sign_up))) {
                intent = new Intent(IntroActivity.this, SignUpActivity.class);
            } else {
                intent = new Intent(IntroActivity.this, LoginActivity.class);
            }
            startActivity(intent);
        }
    };

}
