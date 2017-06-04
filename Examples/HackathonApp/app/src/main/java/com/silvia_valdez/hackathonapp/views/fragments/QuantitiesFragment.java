package com.silvia_valdez.hackathonapp.views.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.FontHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QuantitiesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QuantitiesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuantitiesFragment extends Fragment implements Step {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private static Context mContext;
    private View viewPaperboard;
    private View viewPlastic;

    private OnFragmentInteractionListener mListener;

    public QuantitiesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuantitiesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuantitiesFragment newInstance(Context context) {
        mContext = context;
        QuantitiesFragment fragment = new QuantitiesFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_quantities, container, false);
        setUpFonts(rootView);
        setUpViews(rootView);
        return rootView;
    }

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        FontHelper fontHelper = new FontHelper(mContext);

        Typeface medium = fontHelper.getRobotoMedium();

        TextView textInstructions = (TextView) rootView.findViewById(R.id.quantities_text_info);
        textInstructions.setTypeface(medium);
    }

    private void setUpViews(View rootView) {
        viewPaperboard = rootView.findViewById(R.id.quantities_view_paperboard);
        viewPlastic = rootView.findViewById(R.id.quantities_view_plastic);

        ImageButton buttonMinusPaperboard = (ImageButton) viewPaperboard.findViewById(R.id
                .partial_material_image_button_minus);
        buttonMinusPaperboard.setTag(1);
        buttonMinusPaperboard.setOnClickListener(minusOne);
        ImageButton buttonPlusPaperboard = (ImageButton) viewPaperboard.findViewById(R.id
                .partial_material_image_button_plus);
        buttonPlusPaperboard.setTag(1);
        buttonPlusPaperboard.setOnClickListener(plusOne);
        Spinner spinnerPaperboard = (Spinner) viewPaperboard.findViewById(R.id.partial_material_spinner_container);
        populateSpinner(spinnerPaperboard);

        TextView textPlasticTitle = (TextView) viewPlastic.findViewById(R.id.partial_material_text_paperboard);
        textPlasticTitle.setText(R.string.program_ride_plastic);
        ImageView imagePlastic = (ImageView) viewPlastic.findViewById(R.id.partial_material_image_paperboard);
        imagePlastic.setImageResource(R.mipmap.ic_plastic);
        ImageButton buttonMinus = (ImageButton) viewPlastic.findViewById(R.id.partial_material_image_button_minus);
        buttonMinus.setTag(2);
        buttonMinus.setOnClickListener(minusOne);
        ImageButton buttonPlus = (ImageButton) viewPlastic.findViewById(R.id.partial_material_image_button_plus);
        buttonPlus.setTag(2);
        buttonPlus.setOnClickListener(plusOne);
        Spinner spinnerPlastic = (Spinner) viewPlastic.findViewById(R.id.partial_material_spinner_container);
        populateSpinner(spinnerPlastic);
    }

    private void populateSpinner(Spinner spinner) {
        // you need to have a list of data that you want the spinner to display
        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(getString(R.string.default_big_bag));
        spinnerArray.add(getString(R.string.default_medium_bag));
        spinnerArray.add(getString(R.string.default_small_bag));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(), R.layout.item_spinner, spinnerArray);

        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(adapter);
    }

    private View.OnClickListener plusOne = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag().toString().equals("1")) {
                TextView textQuantity = (TextView) viewPaperboard.findViewById(R.id.partial_material_text_quantity);
                int quantity = Integer.valueOf(textQuantity.getText().toString());
                quantity++;
                textQuantity.setText(String.valueOf(quantity));
            } else {
                TextView textQuantity = (TextView) viewPlastic.findViewById(R.id.partial_material_text_quantity);
                int quantity = Integer.valueOf(textQuantity.getText().toString());
                quantity++;
                textQuantity.setText(String.valueOf(quantity));
            }
        }
    };

    private View.OnClickListener minusOne = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getTag().toString().equals("1")) {
                TextView textQuantity = (TextView) viewPaperboard.findViewById(R.id.partial_material_text_quantity);
                int quantity = Integer.valueOf(textQuantity.getText().toString());
                quantity--;
                textQuantity.setText(String.valueOf(quantity));
            } else {
                TextView textQuantity = (TextView) viewPlastic.findViewById(R.id.partial_material_text_quantity);
                int quantity = Integer.valueOf(textQuantity.getText().toString());
                quantity--;
                textQuantity.setText(String.valueOf(quantity));
            }
        }
    };

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
