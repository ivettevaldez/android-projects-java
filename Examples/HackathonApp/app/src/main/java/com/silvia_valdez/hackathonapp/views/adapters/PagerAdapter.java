package com.silvia_valdez.hackathonapp.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.silvia_valdez.hackathonapp.views.fragments.DashboardFragment;
import com.silvia_valdez.hackathonapp.views.fragments.LiftFragment;
import com.silvia_valdez.hackathonapp.views.fragments.ProfileFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * Created by Silvia Valdez on 6/2/17.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_PAGES = 3;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DashboardFragment (defined as a static inner class below).
//        return DashboardFragment.newInstance(position + 1);

        switch (position){
            case 0:
                return DashboardFragment.newInstance(position + 1);
            case 1:
                return LiftFragment.newInstance();
            case 2:
                return ProfileFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        // Show total pages.
        return TOTAL_PAGES;
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
            case 3:
                return "SECTION 4";
        }
        return null;
    }

}
