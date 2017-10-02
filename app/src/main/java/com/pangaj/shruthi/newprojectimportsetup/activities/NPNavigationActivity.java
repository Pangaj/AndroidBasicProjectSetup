package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPConstants;
import com.pangaj.shruthi.newprojectimportsetup.NPPreferences;
import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPFamilyFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPHomeFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPMapFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPSettingsFragment;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPWorkFragment;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by pangaj on 23/09/17.
 */
@RuntimePermissions
public class NPNavigationActivity extends NPBaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks {
    final static int REQUEST_LOCATION = 199;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int selectedMenuItemId;
    private NPPreferences mPref;
    private Context mContext;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.np_activity_navigation);

        setToolBar();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {    //may produce null pointer exception, if the actionBar is null
            actionBar.setDisplayHomeAsUpEnabled(false);     //hide the home icon - back button
        }
        mContext = getApplicationContext();
        mPref = NPApplication.getInstance().getPrefs();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //set navigation header view
        View headerView = navigationView.inflateHeaderView(R.layout.np_menu_header);
        TextView tvUserEmail = headerView.findViewById(R.id.tv_email);
        tvUserEmail.setText(mPref.getLoginEmail());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //initialise batch notification
        TextView tvPressureMeter = (TextView) navigationView.getMenu().getItem(1).getActionView();
        tvPressureMeter.setGravity(Gravity.CENTER_VERTICAL);
        tvPressureMeter.setTypeface(null, Typeface.BOLD);
        tvPressureMeter.setTextColor(ContextCompat.getColor(mContext, R.color.accent));
        tvPressureMeter.setText(R.string.ninety_nine_plus);

        navigationView.setNavigationItemSelectedListener(this);
        loadInitialFragment();
    }

    private void loadInitialFragment() {
        MenuItem navHome = navigationView.getMenu().getItem(0);
        selectedMenuItemId = navHome.getItemId();
        navHome.setChecked(true);
        Fragment fragment = NPHomeFragment.newInstance();
        replaceFragment(NPNavigationActivity.this, fragment, false, R.id.fragment_container);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        if (!(item.getItemId() == R.id.nav_logout || item.getItemId() == R.id.nav_maps)) {
            selectedMenuItemId = item.getItemId();
            drawerLayout.closeDrawers();
        }

        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new NPHomeFragment();
                break;
            case R.id.nav_work:
                fragment = new NPWorkFragment();
                break;
            case R.id.nav_family:
                fragment = new NPFamilyFragment();
                break;
            case R.id.nav_maps:
                NPNavigationActivityPermissionsDispatcher.getLocationWithPermissionCheck(NPNavigationActivity.this);
                return true;
            case R.id.nav_settings:
                fragment = new NPSettingsFragment();
                break;
            case R.id.nav_logout:
                showAlertDialog(getString(R.string.logout), getString(R.string.logout_msg), true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        mPref.clearData();
                        goToActivityAsNew(NPNavigationActivity.this, NPLoginScreenActivity.class);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        navigationView.setCheckedItem(selectedMenuItemId);
                    }
                });
                return true;
            default:
                return true;
        }
        replaceFragment(NPNavigationActivity.this, fragment, false, R.id.fragment_container);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //RunTime Permissions
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void getLocation() {
        //PermissionsDispatcher : Permission Granted
        selectedMenuItemId = navigationView.getMenu().getItem(3).getItemId();
        drawerLayout.closeDrawers();
        replaceFragment(NPNavigationActivity.this, NPMapFragment.newInstance(), false, R.id.fragment_container);
//        noLocation();
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationDenied() {
        // NOTE: Deal with a denied permission, e.g. by showing specific UI or disabling certain functionality
        //PermissionsDispatcher : Permission Denied
        navigationView.setCheckedItem(selectedMenuItemId);
        Toast.makeText(mContext, R.string.need_location_permission_to_proceed_further, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(@NonNull PermissionRequest request) {
        showRationaleDialog(R.string.location_permission_fetch_current_location, request);
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationNeverAskAgain() {
        //PermissionsDispatcher : Never Ask Again
        showAlertDialog(getString(R.string.info), getString(R.string.location_permission_needed), true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, NPConstants.REQUEST_CODE_LOCATION_PERMISSION);
                Toast.makeText(NPNavigationActivity.this, getString(R.string.grant_location_permission), Toast.LENGTH_LONG).show();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                navigationView.setCheckedItem(selectedMenuItemId);
            }
        });
    }


    // TODO: 10/1/2017 need to check the below

    private void showRationaleDialog(@StringRes int messageResId, @NonNull final PermissionRequest request) {
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
        NPNavigationActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // check whether gps is enabled
    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();
            enableLoc();
            return true;
        }
        return false;
    }


    private void enableLoc() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            NPLog.v(TAG, "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(NPNavigationActivity.this, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        finish();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}