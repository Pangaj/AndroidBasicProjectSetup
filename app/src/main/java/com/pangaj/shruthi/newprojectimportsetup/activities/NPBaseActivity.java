package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.fragments.NPAlertDialogFragment;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPUtilities;

/**
 * Created by pangaj on 23/08/17.
 */
public class NPBaseActivity extends AppCompatActivity {
    private static final String ALERT_DIALOG_TAG = "alert_dialog";
    protected final String TAG = this.getClass().getSimpleName();
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NPLog.d(TAG, "onCreate Activity");
    }

    public void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {    //may produce null pointer exception, if the actionBar is null
            actionBar.setDisplayShowTitleEnabled(false);    //hide the activity name
        }
    }

    /**
     * remove the existing dialog shown
     */
    private void removeExistingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        android.app.Fragment fragment = fragmentManager.findFragmentByTag(ALERT_DIALOG_TAG);
        if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    /**
     * Method to show progress dialog from all activities
     */
    public void showProgress() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                dismissProgress();
            }
            if (mProgressDialog == null) {
                mProgressDialog = NPUtilities.createProgressDialog(this);
            } else {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            NPLog.e(TAG, "Caught exception in show progress dialog: " + e.getMessage(), e);
        }
    }

    /**
     * Method to dismiss the progress dialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    protected void dismissKeyboard(Activity activity) {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Method to alert the user on no network
     */
    protected void noNetworkAlert() {
        Toast.makeText(getApplicationContext(), getString(R.string.internet_connection_offline), Toast.LENGTH_LONG).show();
    }

    /**
     * Method to replace fragments
     *
     * @param activity          The activity containing the fragment
     * @param fragmentToReplace The fragment to replace
     * @param addToBackStack    Whether to add the fragment to backstack or not
     * @param containerId       The id of the layout that holds the fragment
     */
    protected void replaceFragment(FragmentActivity activity, Fragment fragmentToReplace, boolean addToBackStack, int containerId) {
        try {
            FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragmentToReplace);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
        } catch (Exception e) {
            NPLog.e(TAG, "replaceFragment: Caught exception:  " + e.getMessage(), e);
        }
    }

    /**
     * Method to handle device back button
     */
    public void popFragment(final FragmentActivity activity) {
        if (activity != null) {
            android.support.v4.app.FragmentManager supportFragmentManager = activity.getSupportFragmentManager();
            if (supportFragmentManager.getBackStackEntryCount() > 0) {
                NPLog.d(TAG, String.valueOf(supportFragmentManager.getBackStackEntryCount()));
                supportFragmentManager.popBackStack();
            } else {
                finish();
            }
        } else {
            NPLog.d(TAG, "popFragment: activity is null");
        }
    }

    /**
     * Shows alert dialog with message
     *
     * @param message The message to be displayed in the alert
     */
    protected void showAlertDialog(String message) {
        showAlertDialog(null, message);
    }

    /**
     * Shows alert dialog with title and message
     *
     * @param title   The title to be displayed in the alert
     * @param message The message to be displayed in the alert
     */
    protected void showAlertDialog(String title, String message) {
        showAlertDialog(title, message, false, null, null);
    }

    /**
     * Shows alert dialog with title and message and also handles positive and negative button click events
     *
     * @param title                       The title to be displayed in the alert
     * @param message                     The message to be displayed in the alert
     * @param positiveButtonClickListener The listener for positive button click event
     * @param negativeButtonClickListener The listener for negative button click event
     */
    protected void showAlertDialog(String title, String message, boolean isCancelNeeded, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        showAlertDialog(title, message, isCancelNeeded, positiveButtonClickListener, negativeButtonClickListener, null, null);
    }

    protected void showAlertDialog(String title, String message, boolean isCancelNeeded, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener, String negativeButtonTitle, String positiveButtonTitle) {
        try {
            NPAlertDialogFragment alertDialogFragment = NPAlertDialogFragment.newInstance(title, message, isCancelNeeded, positiveButtonClickListener, negativeButtonClickListener, negativeButtonTitle, positiveButtonTitle);
            alertDialogFragment.show(this.getSupportFragmentManager(), ALERT_DIALOG_TAG);
        } catch (Exception e) {
            NPLog.e(TAG, "Caught exception in show alert dialog with custom ok cancel buttons: " + e.getMessage(), e);
        }
    }

    protected void yetToImplementToast() {
        Toast.makeText(this, getString(R.string.yet_to_implement), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        //used this code to hide keyboard, tried with "android:configChanges="keyboardHidden" in Manifest.xml and it didn't achieve because of custom layout
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        super.onResume();
    }

    public void goToActivity(Activity fromActivity, Class<? extends Activity>  toActivity) {
        Intent intent = new Intent(fromActivity, toActivity);
        startActivity(intent);
    }
}