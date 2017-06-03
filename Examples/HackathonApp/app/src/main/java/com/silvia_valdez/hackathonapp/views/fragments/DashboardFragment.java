package com.silvia_valdez.hackathonapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.silvia_valdez.hackathonapp.R;

/**
 * A placeholder fragment containing a simple view.
 * Created by hunabsys_sr1 on 6/2/17.
 */
public class DashboardFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static Context context;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        context = getContext();
//
//        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
//        int color = getTabColor(sectionNumber);
//
        //        rootView.setBackgroundColor(color);
        return inflater.inflate(R.layout.fragment_dashboard, container, false);

        // TextView textView = (TextView) rootView.findViewById(R.id.frag_intro_text_section);
        // textView.setText(getString(R.string.intro_section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * dashboard
     * rides
     * profile
     *
     */
    public static DashboardFragment newInstance(int sectionNumber) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private int getTabColor(int sectionNumber) {
        switch (sectionNumber) {
            case 1:
                return ContextCompat.getColor(context, android.R.color.holo_blue_bright);

            case 2:
                return ContextCompat.getColor(context, android.R.color.holo_green_light);

            case 3:
                return ContextCompat.getColor(context, android.R.color.holo_orange_light);

            case 4:
                return ContextCompat.getColor(context, android.R.color.holo_red_light);

            default:
                return ContextCompat.getColor(context, android.R.color.holo_blue_bright);
        }
    }

}
