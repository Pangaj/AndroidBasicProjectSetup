package com.pangaj.shruthi.newprojectimportsetup.helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPUtilities;

import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by pangaj on 06/09/17.
 */
public class NPLocationManager implements LocationListener {
    private static final String TAG = "NPLocationManager";
    private static NPLocationManager instance;
    private static Location bestCurrentLocation;
    private static LocationManager mLocationManager;

    private NPLocationManager() {
    }

    public static synchronized NPLocationManager getInstance() {
        if (instance == null) {
            instance = new NPLocationManager();
        }
        return instance;
    }

    public boolean checkPermission(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get the current location of the user
     *
     * @param context The context to be used by the location manager
     * @return The current location
     */
    @SuppressWarnings("MissingPermission")
    public Location getCurrentLocation(Context context) {
        if (context != null) {
            mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            List<String> providers = mLocationManager.getProviders(true);
            String lastKnownProvider = null;
            for (String provider : providers) {
                lastKnownProvider = provider;
                Location lastKnownLocation = mLocationManager.getLastKnownLocation(provider);
                if (lastKnownLocation == null) {
                    continue;
                }
                NPLog.d(TAG, "getCurrentLocation: " + provider + " " + lastKnownLocation.getAccuracy() + ", Location: " + lastKnownLocation);
                bestCurrentLocation = lastKnownLocation;
            }
            if (bestCurrentLocation == null) {
                if (lastKnownProvider != null) {
                    mLocationManager.requestLocationUpdates(lastKnownProvider, 0, 0, NPLocationManager.this, null);
                } else {
                    NPLog.d(TAG, "getCurrentLocation: no last know provider found");
                }
            }
        } else {
            NPLog.d(TAG, "getCurrentLocation: context is null");
        }
        NPLog.d(TAG, "getCurrentLocation: " + bestCurrentLocation);
        return bestCurrentLocation;
    }

    /**
     * To get the complete address for the given latitude and longitude
     *
     * @param context   The context to be used by the GeoCoder
     * @param latitude  The latitude
     * @param longitude The longitude
     * @param callback  The GeoCoding callback to return back address to caller
     */
    public void getCompleteAddressString(final Context context, final double latitude, final double longitude, final GeoCodingCallback callback) {
        if (NPUtilities.checkNetworkConnection()) {
            new AsyncTask<Void, Void, String>() {

                @Override
                protected String doInBackground(Void... params) {
                    String address = "";
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0) {
                            Address returnedAddress = addresses.get(0);
                            StringBuilder strReturnedAddress = new StringBuilder("");
                            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                                strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ");
                            }
                            if (strReturnedAddress.length() > 0) {
                                strReturnedAddress.delete(strReturnedAddress.lastIndexOf(", "), strReturnedAddress.length() - 1);
                            }
                            address = strReturnedAddress.toString();
                            NPLog.d(TAG, "" + strReturnedAddress.toString());
                        } else {
                            NPLog.d(TAG, "No Address returned!");
                        }
                    } catch (Exception e) {
                        NPLog.e(TAG, "Cannot get Address: " + e.getMessage(), e);
                    }
                    return address;
                }

                @Override
                protected void onPostExecute(String address) {
                    callback.OnGeoCodeSuccess(address);
                }
            }.execute();
        }
    }

    /**
     * Check the location settings
     *
     * @param context The context required for getting location service
     * @return Whether the location is enabled or not
     */
    public boolean checkLocationProviderAvailability(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        return providers != null && providers.size() > 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            bestCurrentLocation = location;
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(NPLocationManager.this);
            }
            NPLog.d(TAG, "onLocationChanged: called: location: " + location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public interface GeoCodingCallback {
        void OnGeoCodeSuccess(String address);
    }
}