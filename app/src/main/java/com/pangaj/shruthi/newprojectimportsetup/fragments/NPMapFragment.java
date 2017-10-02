package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.pangaj.shruthi.newprojectimportsetup.FAB.NPFloatingActionButton;
import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by pangaj on 21/09/17.
 */

public class NPMapFragment extends NPBaseFragment implements OnMapReadyCallback, View.OnClickListener {
    private Context mContext;
    private GoogleMap mGoogleMap;
    private LatLng markerLatLng;
    private Activity mActivity;

    public static NPMapFragment newInstance() {
        return new NPMapFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.np_fragment_maps, container, false);
        mContext = getActivity().getApplicationContext();
        ImageButton ibCurrentLocation = rootView.findViewById(R.id.ib_current_location);
        initializeMap(this);
        ibCurrentLocation.setOnClickListener(this);

        NPFloatingActionButton fab = rootView.findViewById(R.id.fab);
        View sheetView = rootView.findViewById(R.id.fab_sheet);
        View overlay = rootView.findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.white);
        int fabColor = getResources().getColor(R.color.accent);

        // Initialize material sheet FAB
        MaterialSheetFab materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

        return rootView;
    }

    private void initializeMap(OnMapReadyCallback callback) {
        if (mGoogleMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.fragment_maps_location);
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (mGoogleMap != null) {
            mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.map_style_silver));
            setMapListeners();
        }
    }

    private void setMapListeners() {
        UiSettings mapUiSettings = mGoogleMap.getUiSettings();
        mapUiSettings.setMyLocationButtonEnabled(false);
        mapUiSettings.setRotateGesturesEnabled(false);
        mapUiSettings.setTiltGesturesEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_current_location:
                break;
        }
    }
}
