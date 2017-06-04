package com.silvia_valdez.hackathonapp.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.FontHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 * Created by hunabsys_sr1 on 6/2/17.
 */
public class SelectMaterialsFragment extends Fragment implements Step {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String TAG = SelectMaterialsFragment.class.getSimpleName();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String DASHBOARD_URL = "https://api.myjson.com/bins/pb6f9"; // Dummy

    private static Context mContext;
    private boolean gotData = false;

    private ArrayList<String> mSelectedItems = new ArrayList<>();



    public SelectMaterialsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_materials, container, false);

        initVariables(rootView);
        setUpFonts(rootView);

        return rootView;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     * <p>
     * dashboard
     * rides
     * profile
     */
    public static SelectMaterialsFragment newInstance(Context context) {
        SelectMaterialsFragment fragment = new SelectMaterialsFragment();
        mContext = context;
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initVariables(View rootView) {
        mContext = getContext();

//        ImageView imagePaperboard = (ImageView) rootView.findViewById(R.id.select_material_image_paperboard);
//        ImageView imagePlastic = (ImageView) rootView.findViewById(R.id.select_material_image_plastic);
//        ImageView imageNewspaper = (ImageView) rootView.findViewById(R.id.select_material_image_newspaper);
//        ImageView imageGlass = (ImageView) rootView.findViewById(R.id.select_material_image_glass);
//        ImageView imageAluminium = (ImageView) rootView.findViewById(R.id.select_material_image_aluminium);
//        ImageView imagePaper = (ImageView) rootView.findViewById(R.id.select_material_image_paper);

        FrameLayout framePaperboard = (FrameLayout) rootView.findViewById(R.id.select_material_frame_paperboard);
        FrameLayout framePlastic = (FrameLayout) rootView.findViewById(R.id.select_material_frame_plastic);
        FrameLayout frameNewspaper = (FrameLayout) rootView.findViewById(R.id.select_material_frame_newspaper);
        FrameLayout frameGlass = (FrameLayout) rootView.findViewById(R.id.select_material_frame_glass);
        FrameLayout frameAluminium = (FrameLayout) rootView.findViewById(R.id.select_material_frame_aluminium);
        FrameLayout framePaper = (FrameLayout) rootView.findViewById(R.id.select_material_frame_paper);

        framePaperboard.setOnTouchListener(changeStyle);
        framePlastic.setOnTouchListener(changeStyle);
        frameNewspaper.setOnTouchListener(changeStyle);
        frameGlass.setOnTouchListener(changeStyle);
        frameAluminium.setOnTouchListener(changeStyle);
        framePaper.setOnTouchListener(changeStyle);
    }

    private View.OnTouchListener changeStyle = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (v.isPressed()) {
                v.setPressed(false);
                mSelectedItems.remove(v.getTag().toString());
            } else {
                v.setPressed(true);
                mSelectedItems.add(v.getTag().toString());
            }
//            v.setPressed(!v.isPressed());
            return true;
        }
    };

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        FontHelper fontHelper = new FontHelper(mContext);

        Typeface light = fontHelper.getRobotoLight();
        Typeface medium = fontHelper.getRobotoMedium();

        TextView textInstructions = (TextView) rootView.findViewById(R.id.select_material_text_info);
        TextView textPaperboard = (TextView) rootView.findViewById(R.id.select_material_text_paperboard);
        TextView textPlastic = (TextView) rootView.findViewById(R.id.select_material_text_plastic);
        TextView textNewspaper = (TextView) rootView.findViewById(R.id.select_material_text_newspaper);
        TextView textGlass = (TextView) rootView.findViewById(R.id.select_material_text_glass);
        TextView textAluminium = (TextView) rootView.findViewById(R.id.select_material_text_aluminium);
        TextView textPaper = (TextView) rootView.findViewById(R.id.select_material_text_paper);

        textInstructions.setTypeface(medium);
        textPlastic.setTypeface(medium);
        textPaperboard.setTypeface(medium);
        textNewspaper.setTypeface(medium);
        textGlass.setTypeface(medium);
        textAluminium.setTypeface(medium);
        textPaper.setTypeface(medium);
    }

    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
//
//    private void setUpFields(int completedRides, float accumulatedPoints, int rideId, String rideDate,
//                             String rideTime) {
//        SimpleDateFormat serverDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//        mRidesNumber = completedRides;
//        mPointsNumber = accumulatedPoints;
//
//        try {
//            Date date = serverDateFormat.parse(rideDate);
//
//            String dayOfWeek = (String) DateFormat.format("EEEE", date);
//            String day = (String) DateFormat.format("dd", date);
//            String month = (String) DateFormat.format("MMMM", date);
//
//            if (day.substring(0, 1).equals("0")) {
//                day = day.substring(1);
//            }
//            dayOfWeek = String.format("%s%s", dayOfWeek.substring(0, 1).toUpperCase(), dayOfWeek.substring(1));
//            month = String.format("%s%s", month.substring(0, 1).toUpperCase(), month.substring(1));
//
//            mDate = String.format("%s %s de %s", dayOfWeek, day, month);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        setFields();
//    }
//
//    private void setFields() {
//        mTextInstructions.setText(String.format("%s %s", mRidesNumber, getString(R.string.dashboard_rides)));
//        mTextPaperboard.setText(String.valueOf(mPointsNumber));
//        mTextNewspaper.setText(mDate);
//
//        ArrayList<String> days = new ArrayList<>();
//        days.add("FEB");
//        days.add("MAR");
//        days.add("ABR");
//        days.add("MAY");
//
//        float[] values = new float[4];
//        values[0] = 7.0f;
//        values[1] = 12.0f;
//        values[2] = 6.0f;
//        values[3] = 4.0f;
//
//        setUpPopularityChart(days, values);
//    }
//
//    @Override
//    public void receiveDashboardSuccess(final int completedRides, final float accumulatedPoints, final int rideId,
//                                        final String rideDate, final String rideTime) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                setUpFields(completedRides, accumulatedPoints, rideId, rideDate, rideTime);
//                showProgress(false);
//            }
//        });
//    }
//
//    @Override
//    public void receiveDashboardFail(final String error) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
//                showProgress(false);
//            }
//        });
//    }
//
//
//    private void setUpPopularityChart(ArrayList<String> days, float[] values) {
//        mTextGlass.setDescription("");  // No description text.
//        mTextGlass.setNoDataTextDescription(getString(R.string.error_no_chart_data));
//        setPopularityChartInteraction();
//
//        // xAxis Parameters.
//        XAxis xAxis = mTextGlass.getXAxis();
//        xAxis.setDrawAxisLine(false);
//        xAxis.setDrawLimitLinesBehindData(false);
//        xAxis.setGridColor(ContextCompat.getColor(mContext, R.color.hint_grey));
//        xAxis.setDrawLabels(false);
//
//        // yAxis Parameters (left & right).
//        YAxis leftAxis = mTextGlass.getAxisLeft();
//        leftAxis.removeAllLimitLines(); // Reset all limit lines to avoid overlapping lines.
//        leftAxis.setDrawZeroLine(false);
//        leftAxis.setZeroLineWidth(0f);
//        leftAxis.setDrawTopYLabelEntry(false);
//        leftAxis.setDrawAxisLine(false);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setDrawLabels(false);
//        leftAxis.setDrawLimitLinesBehindData(false);    // Drawn behind data (and not on top)
//
//        YAxis rightAxis = mTextGlass.getAxisRight();
//        rightAxis.removeAllLimitLines(); // Reset all limit lines to avoid overlapping lines.
//        rightAxis.setDrawZeroLine(false);
//        leftAxis.setZeroLineWidth(0f);
//        rightAxis.setDrawTopYLabelEntry(false);
//        rightAxis.setDrawAxisLine(false);
//        rightAxis.setDrawGridLines(false);
//        rightAxis.setDrawLabels(false);
//        rightAxis.setDrawLimitLinesBehindData(false);   // Drawn behind data (and not on top).
//
//        // Add the data.
//        setPopularityChartData(days, values);
//
//        // Get the legend (only possible after setting data).
//        Legend legend = mTextGlass.getLegend();
//        legend.setEnabled(false);
//
////        validateCallbacks();
//    }
//
//    private void setPopularityChartInteraction() {
//        mTextGlass.setScaleEnabled(true);
//        mTextGlass.setTouchEnabled(false);
//        mTextGlass.setDragEnabled(false);
//        mTextGlass.setPinchZoom(false);
//        mTextGlass.setDoubleTapToZoomEnabled(false);
//        mTextGlass.setHighlightPerDragEnabled(false);
//        mTextGlass.setHighlightPerTapEnabled(false);
//        mTextGlass.setDragDecelerationEnabled(false);
//    }
//
//    private void setPopularityChartData(ArrayList<String> xValues, float[] values) {
//        xValues.add("X");
//        ArrayList<Entry> y1Values = new ArrayList<>();
//
//        // Add the values to a list of entries to plot the data.
//        for (int i = 0; i < values.length; i++) {
//            if (i == 0) {
//                y1Values.add(new Entry(values[i], i));
//            }
//            y1Values.add(new Entry(values[i], i + 1));
//        }
//
//        // Create a data set and give it a type.
//        LineDataSet set1 = new LineDataSet(y1Values, null);
//        set1.setLineWidth(0);
//        set1.setDrawValues(false);
//        set1.setDrawCircles(false);
//        set1.setDrawCubic(true);
//        set1.setHighlightEnabled(false);
//
//        Drawable greenDrawable = ContextCompat.getDrawable(mContext,
//                R.drawable.bg_gradient_blue);
//        set1.setFillDrawable(greenDrawable);
//        set1.setDrawFilled(true);
//
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//        dataSets.add(set1); // Add the data sets.
//
//        // Days of week.
//        for (int i = 1; i < xValues.size(); i++) {
//            String strNumberId = "dashboard_text_day_" + i;
//            int numberId = getResources().getIdentifier(strNumberId, "id", mContext.getPackageName());
////            TextView textDay = (TextView) findViewById(numberId);
////            textDay.setText(xValues.get(i - 1));
//        }
//
//        // Create a data object with the data set(s).
//        LineData data = new LineData(xValues, dataSets);
//
//        // Set data.
//        mTextGlass.clear();
//        mTextGlass.setData(data);
//    }
//
//
//    /**
//     * Shows the progress UI and hides the login form.
//     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
//        // for very easy animations. If available, use these APIs to fade-in
//        // the progress spinner.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//            mTextPaper.setVisibility(show ? View.GONE : View.VISIBLE);
//            mTextPaper.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mTextPaper.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });
//
//            mTextAluminium.setVisibility(show ? View.VISIBLE : View.GONE);
//            mTextAluminium.animate().setDuration(shortAnimTime).alpha(
//                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mTextAluminium.setVisibility(show ? View.VISIBLE : View.GONE);
//                }
//            });
//        } else {
//            // The ViewPropertyAnimator APIs are not available, so simply show
//            // and hide the relevant UI components.
//            mTextAluminium.setVisibility(show ? View.VISIBLE : View.GONE);
//            mTextPaper.setVisibility(show ? View.GONE : View.VISIBLE);
//        }
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String title);
    }
}
