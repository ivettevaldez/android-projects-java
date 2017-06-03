package com.silvia_valdez.hackathonapp.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.FontHelper;

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

    private static Context mContext;

    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        setUpFonts(rootView);
        return rootView;

//        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
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
    public static DashboardFragment newInstance(Context context) {
        DashboardFragment fragment = new DashboardFragment();
        mContext = context;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        FontHelper fontHelper = new FontHelper(mContext);

        Typeface light = fontHelper.getRobotoLight();
        Typeface medium = fontHelper.getRobotoMedium();

        TextView textHistory = (TextView) rootView.findViewById(R.id.dashboard_frag_text_history);
        TextView textRidesNumber = (TextView) rootView.findViewById(R.id.dashboard_frag_text_rides_number);
        TextView textPointsNumber = (TextView) rootView.findViewById(R.id.dashboard_frag_text_points_value);
        TextView textPointsLabel = (TextView) rootView.findViewById(R.id.dashboard_frag_text_points_label);
        TextView textDate = (TextView) rootView.findViewById(R.id.dashboard_frag_text_date);

        textPointsNumber.setTypeface(light);
        textRidesNumber.setTypeface(medium);
        textHistory.setTypeface(medium);
        textPointsLabel.setTypeface(medium);
        textDate.setTypeface(medium);
    }

}
