package com.silvia_valdez.ressi.views.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.silvia_valdez.ressi.R;
import com.silvia_valdez.ressi.helpers.TypefaceHelper;
import com.silvia_valdez.ressi.helpers.RequestPermissionsHelper;
import com.silvia_valdez.ressi.helpers.UtilHelper;
import com.silvia_valdez.ressi.services.HttpClientService;
import com.silvia_valdez.ressi.services.RideRequestsService;
import com.silvia_valdez.ressi.services.delegates.IRideRequestsServiceDelegate;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RouteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RouteFragment extends Fragment implements Step, OnMapReadyCallback, IRideRequestsServiceDelegate,
        GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final float MY_CAPTURES_COLOR = BitmapDescriptorFactory.HUE_AZURE;
    private static final float THEIR_CAPTURES_COLOR = BitmapDescriptorFactory.HUE_RED;

    private static final int ZOOM = 14;

    private static Context mContext;
    private static View rootView;
    private GoogleMap mMap;
    private Marker mFirstLocationMarker;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Marker> mCollectionCenters;

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
        mCollectionCenters = new ArrayList<Marker>();

        return rootView;
    }

    private void getData(double latitude, double longitude) {
        HttpClientService httpClientService = new HttpClientService(mContext);

        if (httpClientService.isNetworkAvailable()) {
            RideRequestsService mapCapturesService = new RideRequestsService(RouteFragment.this);
            mapCapturesService.getRideRequests(latitude, longitude);
        } else {
            UtilHelper.showToast(mContext, getString(R.string.error_no_internet_connection));
        }
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
        TypefaceHelper typefaceHelper = new TypefaceHelper(getActivity());

        Typeface medium = typefaceHelper.getRobotoMedium();

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
        mMap.setOnMarkerClickListener(this);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                if (mFirstLocationMarker == null) {
                    // Get all the captures done by me and by others
                    getData(arg0.getLatitude(), arg0.getLongitude());

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

    @Override
    public void onRideRequestsSuccess(final JSONObject requests) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Marker marker;
                    Bitmap mineIcon = resizeMapIcon(R.mipmap.ic_collection_center, 230, 230);
                    Bitmap theirsIcon = resizeMapIcon(R.mipmap.ic_collection_request, 250, 250);

                    if (requests.optJSONArray("done_by_me") != null) {
                        JSONArray doneByMe = requests.getJSONArray("done_by_me");

                        for (int i = 0; i < doneByMe.length(); i++) {
                            JSONObject capture = doneByMe.getJSONObject(i);
                            double latitude = capture.getDouble("latitude");
                            double longitude = capture.getDouble("longitude");

                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                                    .icon(BitmapDescriptorFactory.fromBitmap(mineIcon)));
                            mCollectionCenters.add(marker);

//                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
//                                    .icon(BitmapDescriptorFactory.defaultMarker(MY_CAPTURES_COLOR)));
                        }
                    }
                    if (requests.optJSONArray("done_by_others") != null) {
                        JSONArray doneByOthers = requests.getJSONArray("done_by_others");

                        for (int i = 0; i < doneByOthers.length(); i++) {
                            JSONObject capture = doneByOthers.getJSONObject(i);
                            double latitude = capture.getDouble("latitude");
                            double longitude = capture.getDouble("longitude");

                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                                    .icon(BitmapDescriptorFactory.fromBitmap(theirsIcon

                                    )));

//                            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
//                                    .icon(BitmapDescriptorFactory.defaultMarker(THEIR_CAPTURES_COLOR)));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRideRequestsFail(String error) {

    }

    private void showDetail(View v, ViewGroup parent) {
//        int entryId = (int) v.getTag();
//        getComment(entryId);

        // Popup.
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.partial_request_detail, null);
        View parentView = (View) parent.getParent();

        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());     // null

        FrameLayout layoutClose = (FrameLayout) popupView.findViewById(R.id.request_detail_frame_close);
        FrameLayout layoutCollect = (FrameLayout) popupView.findViewById(R.id.request_detail_frame_collect);

        layoutClose.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        layoutCollect.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

//        mTextHeader = (TextView) popupView.findViewById(R.id.partial_daily_brief_text_header);
//        setUpFonts(mTextHeader, "medium");
//
//        mTextSource = (TextView) popupView.findViewById(R.id.partial_daily_brief_text_link);
//        setUpFonts(mTextSource, "italic");
//
//        mTextComment = (TextView) popupView.findViewById(R.id.partial_daily_brief_text_comment);
//        setUpFonts(mTextComment, "italic");

        popupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ViewGroup layout = (ViewGroup) rootView.findViewById(R.id.fragment_route_layout);
        if (mCollectionCenters.contains(marker)) {
            // Proceed with Collection Center
            Bitmap mineIcon = resizeMapIcon(R.mipmap.ic_collection_center_selected, 230, 230);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(mineIcon));
        } else {
            // Proceed with Collection Request
            showDetail(layout, layout);
            Bitmap mineIcon = resizeMapIcon(R.mipmap.ic_collection_request_selected, 250, 250);
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(mineIcon));
        }
        return false;
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

    // Just in case
    public Bitmap resizeMapIcon(int resource, int width, int height) {
        BitmapDrawable bitmapDraw = (BitmapDrawable) getResources().getDrawable(resource);
        Bitmap bitmap = bitmapDraw.getBitmap();
        return Bitmap.createScaledBitmap(bitmap, width, height, false);
    }
}
