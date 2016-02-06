package com.silvia_valdez.navigationapp.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.silvia_valdez.navigationapp.R;
import com.silvia_valdez.navigationapp.adapters.DrawerArrayAdapter;
import com.silvia_valdez.navigationapp.fragments.AboutFragment;
import com.silvia_valdez.navigationapp.fragments.MainFragment;
import com.silvia_valdez.navigationapp.fragments.SwipeViewTabsFragment;
import com.silvia_valdez.navigationapp.fragments.UpNavigationFragment;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private View mAboutView;

    private DrawerArrayAdapter mAdapter;
    private String[] mFragmentTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mFragmentTitles = getResources().getStringArray(R.array.drawer_items);
        Fragment fragment = MainFragment.newInstance(0);

        setUpFragment(fragment);
        setUpToolbar();
        setUpNavigationDrawer();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(title);
    }

    /**
     * Insert the new fragment by replacing any existing
     * @param fragment The new instance of the fragment.
     */
    private void setUpFragment(Fragment fragment) {
        //
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_activity_content_frame, fragment)
                .commit();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
        if (toolbar != null) setSupportActionBar(toolbar);
        setTitle(getString(R.string.main_title));
    }

    private void setUpNavigationDrawer() {
        mAdapter = new DrawerArrayAdapter(mContext, mFragmentTitles);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Footer
        mAboutView = findViewById(R.id.drawer_item_copyright);
        mAboutView.setOnClickListener(showAboutFragment);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        // Create a new fragment and set it up.
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = MainFragment.newInstance(position);
                break;
            case 1:
                fragment = SwipeViewTabsFragment.newInstance(position);
                break;
            case 2:
                fragment = UpNavigationFragment.newInstance(position);
                break;
            default:
                fragment = MainFragment.newInstance(position);
                break;
        }
        setUpFragment(fragment);

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        highlightSelectedItem(position, false, mFragmentTitles[position]);
    }

    private void highlightSelectedItem(int position, boolean aboutIsSelected, String title) {
        toggleAboutView(aboutIsSelected); // Select/unselect about view
        mAdapter.setSelectedItem(position);
        setTitle(title);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void toggleAboutView(boolean isSelected) {
        ImageView aboutImage = (ImageView) findViewById(R.id.drawer_image_about);
        TextView aboutText = (TextView) findViewById(R.id.drawer_text_about);
        mAdapter.highlightItem(isSelected, mAboutView, aboutImage, aboutText);
    }


    /**
     *  LISTENERS
     */
    private View.OnClickListener showAboutFragment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Fragment fragment = AboutFragment.newInstance(mFragmentTitles.length);
            setUpFragment(fragment);

            // Highlight the selected item, update the title, and close the drawer
            highlightSelectedItem(mFragmentTitles.length, true, getString(R.string.about_title));

            // Unselect the last item selected in ListView
            int lastSelected = mAdapter.getSelectedItem();
            mDrawerList.setItemChecked(lastSelected, false);
            mAdapter.notifyDataSetChanged();
        }
    };

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

}

