package com.pangaj.shruthi.newprojectimportsetup.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.maps.model.LatLng;
import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPPreferences;
import com.pangaj.shruthi.newprojectimportsetup.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pangaj on 23/08/17.
 */
public class NPUtilities {
    public static final String TAG = "NPUtilities";
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 16;
    private static final NPPreferences mPref = NPApplication.getInstance().getPrefs();

    // isValidEmailAddress: Check the email address is OK
    public static boolean isValidEmailAddress(String emailAddress) {
        if (TextUtils.isEmpty(emailAddress)) {
            return false;
        }
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress.trim());
        return matcher.find();
    }

    /**
     * Method to validate password minimum length.
     */
    private static boolean validatePasswordMinimumLength(String password) {
        return password != null && password.length() < PASSWORD_MIN_LENGTH;
    }

    /**
     * Method to validate password maximum length.
     */
    private static boolean validatePasswordMaximumLength(String password) {
        return password != null && password.length() > PASSWORD_MAX_LENGTH;
    }

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        try {
            if (!((Activity) context).isFinishing()) {
                dialog.show();
            }
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            dialog.setContentView(R.layout.np_progress_loader);
            ProgressBar pbLoading = dialog.findViewById(R.id.pb_loading);
            pbLoading.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.accent), PorterDuff.Mode.MULTIPLY);
            WindowManager.LayoutParams layoutParams;
            if (window != null) {
                layoutParams = window.getAttributes();
                layoutParams.dimAmount = 0.7f;
            }
        } catch (WindowManager.BadTokenException exception) {
            NPLog.d(TAG, "Caught Exception" + exception.getMessage());
        }
        return dialog;
    }

    /**
     * Method to check network connectivity status
     *
     * @return Whether connected or not
     */
    public static boolean checkNetworkConnection() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) NPApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE); //Using application instance as context
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                    return true;
            }
        } catch (Exception exception) {
            NPLog.e(TAG, "checkNetworkConnection: Exception: ", exception);
        }
        return false;
    }

    public static boolean validatePassword(String passwordText) {
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(passwordText);
        return matcher.matches();
    }

    /**
     * Method to replace the fragments
     *
     * @param activity          The activity hosting the fragments
     * @param fragmentToReplace The fragment to replace
     * @param addToBackStack    Whether to add the fragment to backstack or not
     * @param containerId       The frame layout containing the fragment
     */
    private static void replaceFragment(FragmentActivity activity, Fragment fragmentToReplace, boolean addToBackStack, int containerId) {
        if (activity != null) {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragmentToReplace);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        }
    }

    /**
     * Method to handle device back button
     */
    private static void popFragment(final FragmentActivity activity) {
        if (activity != null) {
            android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (supportFragmentManager.getBackStackEntryCount() > 0) {
                NPLog.d(TAG, String.valueOf(supportFragmentManager.getBackStackEntryCount()));
                supportFragmentManager.popBackStack();
            } else {
                activity.finish();
            }
        } else {
            NPLog.d(TAG, "popFragment: activity is null");
        }
    }

    /*public static void setCompleteAddress(Activity mActivity, double latitude, double longitude, final NPMapCompleteAddress completeAddress) {
        // TODO: 11/09/17 the below 2 lines are needed for FavoriteFragment - will be update once the Location updates are changed using feasible location or smart location
        NPConstants.LATITUDE_VALUE = latitude;
        NPConstants.LONGITUDE_VALUE = longitude;
        RALocationManager.getInstance().getCompleteAddressString(mActivity, latitude, longitude, new RALocationManager.GeoCodingCallback() {
            @Override
            public void OnGeoCodeSuccess(String address) {
                completeAddress.onSuccessCompleteListener(address);
            }
        });
    }*/

    public static LatLng getGeoCodingLatLng(double latitude, double longitude) {
        return new LatLng(latitude, longitude);
    }
}