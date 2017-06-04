package com.silvia_valdez.hackathonapp.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.silvia_valdez.hackathonapp.R;
import com.silvia_valdez.hackathonapp.helpers.FontHelper;
import com.silvia_valdez.hackathonapp.helpers.RequestPermissionsHelper;
import com.silvia_valdez.hackathonapp.helpers.UtilHelper;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment implements Step, OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int ZOOM = 14;

    private static Context mContext;
    private static View rootView;
    private GoogleMap mMap;
    private Marker mFirstLocationMarker;
    private OnFragmentInteractionListener mListener;

    public RouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RouteFragment newInstance(Context context) {
        mContext = context;
        RouteFragment fragment = new RouteFragment();
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
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_route, container, false);
        }
        verifyLocationPermissions();
        setUpFonts(rootView);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.route_fragment_map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rootView = null;
    }

    private boolean verifyLocationPermissions() {
        RequestPermissionsHelper permissionsHelper = RequestPermissionsHelper.getInstance(getActivity());
        return permissionsHelper.verifyLocationPermissions();
    }

    private void setUpFonts(View rootView) {
        // Setup Fonts.
        FontHelper fontHelper = new FontHelper(mContext);

        Typeface medium = fontHelper.getRobotoMedium();

        TextView textInstructions = (TextView) rootView.findViewById(R.id.route_text_info);
        textInstructions.setTypeface(medium);
    }

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
     * ******************* OVERRIDE METHODS ********************
     * <p>
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            setUpMap();

            if (!UtilHelper.isGpsEnabled(mContext)) {
//                getDataWithFixedLocation();
            }
        }
    }

    private void setUpMap() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        mMap.getMyLocation();
        mMap.getMaxZoomLevel();

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                if (mFirstLocationMarker == null) {
                    // Get all the captures done by me and by others
//                    getData(arg0.getLatitude(), arg0.getLongitude());

                    // Get the actual location through GPS and do zoom to it
                    zoomToLocation(arg0.getLatitude(), arg0.getLongitude());

                    // Set up the new marker (it allows us to drag the map without returning to the actual location)
                    LatLng coordinate = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    mFirstLocationMarker = mMap.addMarker(new MarkerOptions()
                            .position(coordinate)
                            .title(getString(R.string.default_im_here)));
                    mFirstLocationMarker.setVisible(false);

                    // Save the last known location in shared preferences
                    ArrayList<Double> lastLocationArray = new ArrayList<>();
                    lastLocationArray.add(arg0.getLatitude());
                    lastLocationArray.add(arg0.getLongitude());
//                    mSessionManager.saveLastLocation(lastLocationArray);
                }
            }
        });
    }

    private void zoomToLocation(double latitude, double longitude) {
        LatLng coordinate = new LatLng(latitude, longitude);
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinate, ZOOM);

        // Move the camera to the actual location
        mMap.animateCamera(myLocation);
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
