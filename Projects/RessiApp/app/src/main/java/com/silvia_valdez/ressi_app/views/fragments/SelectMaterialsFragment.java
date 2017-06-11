package com.silvia_valdez.ressi_app.views.fragments;

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

import com.silvia_valdez.ressi_app.R;
import com.silvia_valdez.ressi_app.helpers.TypefaceHelper;
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
            return true;
        }
    };

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        TypefaceHelper typefaceHelper = new TypefaceHelper(getActivity());

        Typeface light = typefaceHelper.getRobotoLight();
        Typeface medium = typefaceHelper.getRobotoMedium();

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
