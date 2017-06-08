package com.silvia_valdez.ressi_app.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.silvia_valdez.ressi_app.R;
import com.silvia_valdez.ressi_app.helpers.FontHelper;
import com.silvia_valdez.ressi_app.helpers.SessionManager;
import com.silvia_valdez.ressi_app.services.DashboardService;
import com.silvia_valdez.ressi_app.services.HttpClientService;
import com.silvia_valdez.ressi_app.services.delegates.IDashboardServiceDelegate;
import com.silvia_valdez.ressi_app.views.controls.MenuTabs;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 * Created by hunabsys_sr1 on 6/2/17.
 */
public class DashboardFragment extends Fragment implements IDashboardServiceDelegate {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = DashboardFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String DASHBOARD_URL = "https://api.myjson.com/bins/pb6f9"; // Dummy

    private static Context mContext;
    private boolean gotData = false;

    private LineChart mPopularityChart;
    private View mProgressView;
    private LinearLayout mPopularityLayout;
    private TextView mTextRidesNumber;
    private TextView textPointsNumber;
    private TextView textDate;

    private static int mRidesNumber = 0;
    private static float mPointsNumber = 0f;
    private static String mDate = "";


    public DashboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initVariables(rootView);
        setUpFonts(rootView);

        if (!gotData) {
            SessionManager sessionManager = new SessionManager(mContext);
            Log.e(TAG, sessionManager.getUser());

            loadData();
        } else {
            setFields();
        }
        return rootView;

//        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        // TextView textView = (TextView) rootView.findViewById(R.id.frag_intro_text_section);
        // textView.setText(getString(R.string.intro_section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
    }

    private void loadData() {
        final HttpClientService httpClientService = new HttpClientService(mContext);
        if (httpClientService.isNetworkAvailable()) {
            showProgress(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JSONObject response = httpClientService.performHttpGetObject(DASHBOARD_URL);

                    if (response != null) {
                        DashboardService dashboardService = new DashboardService(getActivity(), DashboardFragment.this);
                        dashboardService.validateDashboardResponse(response);
                        gotData = true;
                    } else {
                        Log.e(TAG, "NULL RESPONSE");
                    }
                }
            }).start();

        } else {
            Toast.makeText(mContext, "Revise su conexion a internet", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     * <p>
     * dashboard
     * rides
     * profile
     */
    public static DashboardFragment newInstance(Context context) {
        DashboardFragment fragment = new DashboardFragment();
        mContext = context;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initVariables(View rootView) {
        mContext = getContext();

        mTextRidesNumber = (TextView) rootView.findViewById(R.id.dashboard_frag_text_rides_number);
        textPointsNumber = (TextView) rootView.findViewById(R.id.dashboard_frag_text_points_value);
        textDate = (TextView) rootView.findViewById(R.id.dashboard_frag_text_date);
        mPopularityChart = (LineChart) rootView.findViewById(R.id.dashboard_frag_chart_popularity);
        mProgressView = rootView.findViewById(R.id.dashboard_progress);
        mPopularityLayout = (LinearLayout) rootView.findViewById(R.id.dashboard_frag_layout_popularity);

        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.dashboard_layout_container);
        MenuTabs menuTabs = new MenuTabs(viewPager);

        LinearLayout layoutProgrammedRide = (LinearLayout) rootView.findViewById(R.id.dashboard_frag_layout_card);
        layoutProgrammedRide.setOnClickListener(menuTabs.goToSection);
    }

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        FontHelper fontHelper = new FontHelper(mContext);

        Typeface light = fontHelper.getRobotoLight();
        Typeface medium = fontHelper.getRobotoMedium();

        TextView textHistory = (TextView) rootView.findViewById(R.id.dashboard_frag_text_history);
        TextView textPointsLabel = (TextView) rootView.findViewById(R.id.dashboard_frag_text_points_label);

        textPointsNumber.setTypeface(light);
        mTextRidesNumber.setTypeface(medium);
        textDate.setTypeface(medium);
        textHistory.setTypeface(medium);
        textPointsLabel.setTypeface(medium);
    }

    private void setUpFields(int completedRides, float accumulatedPoints, int rideId, String rideDate,
                             String rideTime) {
        SimpleDateFormat serverDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        mRidesNumber = completedRides;
        mPointsNumber = accumulatedPoints;

        try {
            Date date = serverDateFormat.parse(rideDate);

            String dayOfWeek = (String) DateFormat.format("EEEE", date);
            String day = (String) android.text.format.DateFormat.format("dd", date);
            String month = (String) android.text.format.DateFormat.format("MMMM", date);

            if (day.substring(0, 1).equals("0")) {
                day = day.substring(1);
            }
            dayOfWeek = String.format("%s%s", dayOfWeek.substring(0, 1).toUpperCase(), dayOfWeek.substring(1));
            month = String.format("%s%s", month.substring(0, 1).toUpperCase(), month.substring(1));

            mDate = "12 de Junio";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setFields();
    }

    private void setFields() {
        mTextRidesNumber.setText(String.format("%s %s", mRidesNumber, getString(R.string.dashboard_rides)));
        textPointsNumber.setText(String.valueOf(mPointsNumber));
        textDate.setText(mDate);

        ArrayList<String> days = new ArrayList<>();
        days.add("FEB");
        days.add("MAR");
        days.add("ABR");
        days.add("MAY");

        float[] values = new float[4];
        values[0] = 7.0f;
        values[1] = 12.0f;
        values[2] = 6.0f;
        values[3] = 4.0f;

        setUpPopularityChart(days, values);
    }

    @Override
    public void receiveDashboardSuccess(final int completedRides, final float accumulatedPoints, final int rideId,
                                        final String rideDate, final String rideTime) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setUpFields(completedRides, accumulatedPoints, rideId, rideDate, rideTime);
                showProgress(false);
            }
        });
    }

    @Override
    public void receiveDashboardFail(final String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
                showProgress(false);
            }
        });
    }


    private void setUpPopularityChart(ArrayList<String> days, float[] values) {
        mPopularityChart.setDescription("");  // No description text.
        mPopularityChart.setNoDataTextDescription(getString(R.string.error_no_chart_data));
        setPopularityChartInteraction();

        // xAxis Parameters.
        XAxis xAxis = mPopularityChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.hint_grey));
        xAxis.setDrawLabels(false);

        // yAxis Parameters (left & right).
        YAxis leftAxis = mPopularityChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // Reset all limit lines to avoid overlapping lines.
        leftAxis.setDrawZeroLine(false);
        leftAxis.setZeroLineWidth(0f);
        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);
        leftAxis.setDrawLimitLinesBehindData(false);    // Drawn behind data (and not on top)

        YAxis rightAxis = mPopularityChart.getAxisRight();
        rightAxis.removeAllLimitLines(); // Reset all limit lines to avoid overlapping lines.
        rightAxis.setDrawZeroLine(false);
        leftAxis.setZeroLineWidth(0f);
        rightAxis.setDrawTopYLabelEntry(false);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawLimitLinesBehindData(false);   // Drawn behind data (and not on top).

        // Add the data.
        setPopularityChartData(days, values);

        // Get the legend (only possible after setting data).
        Legend legend = mPopularityChart.getLegend();
        legend.setEnabled(false);

//        validateCallbacks();
    }

    private void setPopularityChartInteraction() {
        mPopularityChart.setScaleEnabled(true);
        mPopularityChart.setTouchEnabled(false);
        mPopularityChart.setDragEnabled(false);
        mPopularityChart.setPinchZoom(false);
        mPopularityChart.setDoubleTapToZoomEnabled(false);
        mPopularityChart.setHighlightPerDragEnabled(false);
        mPopularityChart.setHighlightPerTapEnabled(false);
        mPopularityChart.setDragDecelerationEnabled(false);
    }

    private void setPopularityChartData(ArrayList<String> xValues, float[] values) {
        xValues.add("X");
        ArrayList<Entry> y1Values = new ArrayList<>();

        // Add the values to a list of entries to plot the data.
        for (int i = 0; i < values.length; i++) {
            if (i == 0) {
                y1Values.add(new Entry(values[i], i));
            }
            y1Values.add(new Entry(values[i], i + 1));
        }

        // Create a data set and give it a type.
        LineDataSet set1 = new LineDataSet(y1Values, null);
        set1.setLineWidth(0);
        set1.setDrawValues(false);
        set1.setDrawCircles(false);
        set1.setDrawCubic(true);
        set1.setHighlightEnabled(false);

        Drawable greenDrawable = ContextCompat.getDrawable(mContext,
                R.drawable.bg_gradient_blue);
        set1.setFillDrawable(greenDrawable);
        set1.setDrawFilled(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // Add the data sets.

        // Days of week.
        for (int i = 1; i < xValues.size(); i++) {
            String strNumberId = "dashboard_text_day_" + i;
            int numberId = getResources().getIdentifier(strNumberId, "id", mContext.getPackageName());
//            TextView textDay = (TextView) findViewById(numberId);
//            textDay.setText(xValues.get(i - 1));
        }

        // Create a data object with the data set(s).
        LineData data = new LineData(xValues, dataSets);

        // Set data.
        mPopularityChart.clear();
        mPopularityChart.setData(data);
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mPopularityLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mPopularityLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mPopularityLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mPopularityLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
