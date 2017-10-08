package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.pangaj.shruthi.newprojectimportsetup.FAB.NPFloatingActionButton;
import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by pangaj on 21/09/17.
 */
public class NPMapFragment extends NPBaseFragment implements OnMapReadyCallback, View.OnClickListener {
    private static MaterialSheetFab materialSheetFab;
    private Context mContext;
    private GoogleMap mGoogleMap;

    public static NPMapFragment newInstance() {
        return new NPMapFragment();
    }

    public static boolean checkOnBackPressListener() {
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.np_fragment_maps, container, false);
        mContext = getActivity().getApplicationContext();
        ImageButton ibCurrentLocation = rootView.findViewById(R.id.ib_current_location);
        initializeMap(this);
        ibCurrentLocation.setOnClickListener(this);

        //https://github.com/gowong/material-sheet-fab - used for FAB button
        NPFloatingActionButton fab = rootView.findViewById(R.id.fab);
        View sheetView = rootView.findViewById(R.id.fab_sheet);
        View overlay = rootView.findViewById(R.id.overlay);
        int sheetColor = ContextCompat.getColor(mContext, R.color.white);
        int fabColor = ContextCompat.getColor(mContext, R.color.accent);

        // Initialize material sheet FAB
        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);

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