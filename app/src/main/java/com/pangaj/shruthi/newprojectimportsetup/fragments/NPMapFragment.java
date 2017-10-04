package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.pangaj.shruthi.newprojectimportsetup.FAB.NPFloatingActionButton;
import com.pangaj.shruthi.newprojectimportsetup.NPConstants;
import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.interfaces.NPFusedLocationUpdateListener;
import com.pangaj.shruthi.newprojectimportsetup.locations.NPFusedLocationManager;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by pangaj on 21/09/17.
 */
@RuntimePermissions
public class NPMapFragment extends NPBaseFragment implements OnMapReadyCallback, View.OnClickListener, NPFusedLocationUpdateListener {
    private static MaterialSheetFab materialSheetFab;
    private Context mContext;
    private Activity mActivity;
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
        mActivity = getActivity();
        mContext = mActivity.getApplicationContext();
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
            mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext, R.raw.custom_map_style));
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
                NPMapFragmentPermissionsDispatcher.getLocationWithPermissionCheck(NPMapFragment.this);
                break;
        }
    }

    private void animateMap(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
    }

    //RunTime Permissions
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void getLocation() {
        //PermissionsDispatcher : Permission Granted
        NPFusedLocationManager.getCurrentLocation(mContext, this);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI or disabling certain functionality
        //PermissionsDispatcher : Permission Denied
        Toast.makeText(mContext, R.string.need_location_permission_to_proceed_further, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(PermissionRequest request) {
        showRationaleDialog(R.string.location_permission_fetch_current_location, request);
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationNeverAskAgain() {
        //PermissionsDispatcher : Never Ask Again
        showAlertDialog(getString(R.string.info), getString(R.string.location_permission_needed), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, NPConstants.REQUEST_CODE_LOCATION_PERMISSION);
                Toast.makeText(mActivity, getString(R.string.grant_location_permission), Toast.LENGTH_LONG).show();
            }
        }, null);
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        showAlertDialog(getString(R.string.info), getString(messageResId), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                request.proceed();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                request.cancel();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
//        RAMapsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void fusedLocationUpdate(Location currentLocation) {
        if (currentLocation != null) {
            animateMap(currentLocation.getLatitude(), currentLocation.getLongitude());
        }
    }
}
