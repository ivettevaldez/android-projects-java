package com.silvia_valdez.ressi_app.views.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.silvia_valdez.ressi_app.views.fragments.ProfileFragment;
import com.silvia_valdez.ressi_app.views.fragments.RideFragment;
import com.silvia_valdez.ressi_app.views.fragments.SelectMaterialsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * Created by Silvia Valdez on 6/2/17.
 */
public class ProgramRidePagerAdapter extends FragmentPagerAdapter {

    private static final int TOTAL_PAGES = 1;

    private Context mContext;

    public ProgramRidePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DashboardFragment (defined as a static inner class below).
//        return DashboardFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                return SelectMaterialsFragment.newInstance(mContext);
            case 1:
                return RideFragment.newInstance(mContext);
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
        }
        return null;
    }

}
