package com.silvia_valdez.ressi_app.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.silvia_valdez.ressi_app.R;
import com.silvia_valdez.ressi_app.helpers.TypefaceHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment implements Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static Context mContext;
    private SimpleDateFormat mDateFormat;

    private Spinner mSpinnerDate;
    private Spinner mSpinnerTime;
    private ArrayAdapter<String> dateAdapter;
    private ArrayAdapter<String> timeAdapter;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(Context context) {
        mContext = context;
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);
        setUpFonts(rootView);

        mSpinnerDate = (Spinner) rootView.findViewById(R.id.schedule_spinner_date);
        mSpinnerTime = (Spinner) rootView.findViewById(R.id.schedule_spinner_time);
        populateSpinners();

        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        TypefaceHelper typefaceHelper = new TypefaceHelper(getActivity());

        Typeface medium = typefaceHelper.getRobotoMedium();

        TextView textInstructions = (TextView) rootView.findViewById(R.id.schedule_text_info);
        textInstructions.setTypeface(medium);
    }

    private void populateSpinners() {
        // DATE
        List<String> dateArray = new ArrayList<>();
        dateArray.add(getString(R.string.schedule_date));

        dateAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, dateArray);
        dateAdapter.setDropDownViewResource(R.layout.item_spinner);

        mSpinnerDate.setAdapter(dateAdapter);
        mSpinnerDate.setClickable(false);
        mSpinnerDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    selectDate();
                }
                return true;
            }
        });

        // TIME
        List<String> timeArray = new ArrayList<>();
        timeArray.add(getString(R.string.schedule_time));

        timeAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_spinner, timeArray);
        timeAdapter.setDropDownViewResource(R.layout.item_spinner);

        mSpinnerTime.setAdapter(timeAdapter);
        mSpinnerTime.setClickable(false);
        mSpinnerTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    selectTime();
                }
                return true;
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            try {
                String strDate = String.format(Locale.getDefault(), "%d-%d-%d", year, monthOfYear + 1, dayOfMonth);
                Date date = mDateFormat.parse(strDate);

                String dayOfWeek = (String) DateFormat.format("EEEE", date);
                String day = (String) android.text.format.DateFormat.format("dd", date);
                String month = (String) android.text.format.DateFormat.format("MMMM", date);

                if (day.substring(0, 1).equals("0")) {
                    day = day.substring(1);
                }
                dayOfWeek = String.format("%s%s", dayOfWeek.substring(0, 1).toUpperCase(), dayOfWeek.substring(1));
                month = String.format("%s%s", month.substring(0, 1).toUpperCase(), month.substring(1));

                strDate = String.format("%s %s de %s", dayOfWeek, day, month);

                dateAdapter.add(strDate);
                dateAdapter.notifyDataSetChanged();
                mSpinnerDate.setSelection(dateAdapter.getCount() - 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };


    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            String format;
            if (hourOfDay >= 12) {
                hourOfDay -= 12;
                format = "PM";
            } else {
                format = "AM";
            }
            String strDate = String.format(Locale.getDefault(), "%d:%d %s", hourOfDay, minute, format);
            timeAdapter.add(strDate);
            timeAdapter.notifyDataSetChanged();
            mSpinnerTime.setSelection(timeAdapter.getCount() - 1);
        }
    };

    private void selectDate() {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(new Date());

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );

        Calendar twoDaysLater = Calendar.getInstance();
        twoDaysLater.add(Calendar.DAY_OF_MONTH, 2);
        datePickerDialog.setMinDate(twoDaysLater);
        datePickerDialog.dismissOnPause(true);

        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(mContext, R.color.caribbean_green));
        datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    private void selectTime() {
        Calendar selectedTime = Calendar.getInstance();
        selectedTime.setTime(new Date());

        com.wdullaer.materialdatetimepicker.time.TimePickerDialog
                datePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                timeSetListener,

                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE),
                false
        );

        datePickerDialog.dismissOnPause(true);

        datePickerDialog.setVersion(TimePickerDialog.Version.VERSION_1);
        datePickerDialog.setAccentColor(ContextCompat.getColor(mContext, R.color.caribbean_green));
        datePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
