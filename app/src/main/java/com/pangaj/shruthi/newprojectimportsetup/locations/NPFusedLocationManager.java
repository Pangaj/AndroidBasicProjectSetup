package com.pangaj.shruthi.newprojectimportsetup.locations;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPConstants;
import com.pangaj.shruthi.newprojectimportsetup.interfaces.NPFusedLocationUpdateListener;
import com.pangaj.shruthi.newprojectimportsetup.interfaces.NPMapCompleteAddress;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;

/**
 * Created by pangaj on 28/09/17.
 */
public class NPFusedLocationManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, NPFusedLocationUpdateListener {
    private static final String TAG = NPFusedLocationManager.class.getSimpleName();
    private static final float MIN_LAST_READ_ACCURACY = 500.0f;
    private static final long ONE_MIN = 1000 * 60;
    private static final long FIVE_MIN = ONE_MIN * 5;
    private static GoogleApiClient mGoogleApiClient;
    private static Location mCurrentLocation = null;
    private static GeocodingApiRequest geoCodingApiRequest;
    private Context mContext;

    public NPFusedLocationManager(Context context) {
        this.mContext = context;
        buildGoogleApiClient();
        connect();
    }

    public static void getCurrentLocation(Context mContext, final NPFusedLocationUpdateListener locationListener) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = new LocationRequest();
            // Use high accuracy
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            // Set the update interval to 5 seconds
            locationRequest.setInterval(5000);
            // Set the fastest update interval to 1 second
            locationRequest.setFastestInterval(1000);

            mCurrentLocation = bestLastKnownLocation(mContext, MIN_LAST_READ_ACCURACY, FIVE_MIN);
            if (mCurrentLocation == null && mGoogleApiClient != null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        NPLog.d(TAG, "onLocationChanged : updated");
                        NPLog.d(TAG, "Latitude : " + location.getLatitude() + "");
                        NPLog.d(TAG, "Longitude : " + location.getLatitude() + "");
                        mCurrentLocation = location;
                    }
                });
            } else {
                if (locationListener != null) {
                    locationListener.fusedLocationUpdate(mCurrentLocation);
                }
            }
        }
    }

    private static Location bestLastKnownLocation(Context mContext, float minAccuracy, long minTime) {
        Location bestResult = null;
        float bestAccuracy = Float.MAX_VALUE;
        long bestTime = Long.MIN_VALUE;

        // Get the best most recent location currently available
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGoogleApiClient != null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            }
        }

        if (mCurrentLocation != null) {
            float accuracy = mCurrentLocation.getAccuracy();
            long time = mCurrentLocation.getTime();

            if (accuracy < bestAccuracy) {
                bestResult = mCurrentLocation;
                bestAccuracy = accuracy;
                bestTime = time;
            }
        }

        // Return best reading or null
        if (bestAccuracy > minAccuracy || bestTime < minTime) {
            return null;
        } else {
            return bestResult;
        }
    }

    public static void setCompleteAddress(double latitude, double longitude, final NPMapCompleteAddress completeAddress) {
        // TODO: 11/09/17 the below 2 lines are needed for FavoriteFragment - will be update once the Location updates are changed using feasible location or smart location
        NPConstants.LATITUDE_VALUE = latitude;
        NPConstants.LONGITUDE_VALUE = longitude;
        getCompleteAddressString(latitude, longitude, new GeoCodingCallback() {
            @Override
            public void OnGeoCodeSuccess(String address) {
                completeAddress.onSuccessCompleteListener(address);
            }
        });
    }

    /**
     * To get the complete address for the given latitude and longitude
     *
     * @param latitude  The latitude
     * @param longitude The longitude
     * @param callback  The GeoCoding callback to return back address to caller
     */
    private static void getCompleteAddressString(final double latitude, final double longitude, final GeoCodingCallback callback) {
        if (geoCodingApiRequest != null) {
            geoCodingApiRequest.cancel();
            geoCodingApiRequest = null;
        }
        geoCodingApiRequest = GeocodingApi.reverseGeocode(NPApplication.getInstance().getGeoApiInstance(), getGeoCodingLatLng(latitude, longitude));// TODO: 18/09/17 Need to look for cancelling the existing request
        geoCodingApiRequest.setCallback(new com.google.maps.PendingResult.Callback<GeocodingResult[]>() {
            @Override
            public void onResult(final GeocodingResult[] result) {
                NPLog.d(TAG, "onResult: formattedAddress: " + result[0].formattedAddress);
                new Handler(Looper.getMainLooper()).post(new Runnable() {//In order to fix the Only the original thread that created a view hierarchy can touch its views issue.
                    @Override
                    public void run() {
                        callback.OnGeoCodeSuccess(result[0].formattedAddress);
                    }
                });
            }

            @Override
            public void onFailure(Throwable exception) {
                NPLog.e(TAG, exception.getMessage(), exception);
                NPLog.e(TAG, "onFailure: Caught exception while geo coding: " + exception.getMessage(), exception);
            }
        });
    }

    public static LatLng getGeoCodingLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void connect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        NPLog.d(TAG, "Google API client connected");
        getCurrentLocation(mContext, null);
    }

    @Override
    public void onConnectionSuspended(int i) {
        NPLog.d(TAG, "Google API client connection suspended");
        connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        NPLog.d(TAG, "Google API client connection failed: " + connectionResult.getErrorMessage());
    }

    //FusedLocation listener
    @Override
    public void fusedLocationUpdate(Location currentLocation) {

    }

    interface GeoCodingCallback {
        void OnGeoCodeSuccess(String address);
    }
}