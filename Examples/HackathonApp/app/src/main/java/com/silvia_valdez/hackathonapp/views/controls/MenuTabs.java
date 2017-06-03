package com.silvia_valdez.hackathonapp.views.controls;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Tabs control.
 * Created by Silvia Valdez on 6/2/17.
 */

public class MenuTabs {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public MenuTabs(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public View.OnClickListener goToSection = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = Integer.valueOf(v.getTag().toString());
            mViewPager.setCurrentItem(index, true);
        }
    };

}
