package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.pangaj.shruthi.newprojectimportsetup.R;
import com.pangaj.shruthi.newprojectimportsetup.activities.NPBaseActivity;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;

/**
 * Created by pangaj on 24/08/17.
 */
public class NPBaseFragment extends Fragment {
    private static final String ALERT_DIALOG_TAG = "alert_dialog";
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onStart() {
        NPLog.d(TAG, "onStart: called");
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NPLog.d(TAG, "onCreate: called");
    }

    @Override
    public void onStop() {
        NPLog.d(TAG, "onStop: called");
        super.onStop();
    }

    @Override
    public void onPause() {
        NPLog.d(TAG, "onPause: called");
        super.onPause();
    }

    @Override
    public void onResume() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        NPLog.d(TAG, "onResume: called");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        NPLog.d(TAG, "onDestroy: called");
        super.onDestroy();
    }

    /**
     * Method to get base activity.
     *
     * @return Returns activity.
     */
    protected NPBaseActivity getBaseActivity() {
        return (NPBaseActivity) getActivity();
    }

    protected void dismissKeyboard() {
        Activity activity = getActivity();
        if (activity != null) {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * Method to replace the fragments
     *
     * @param activity          The activity hosting the fragments
     * @param fragmentToReplace The fragment to replace
     * @param addToBackStack    Whether to add the fragment to backstack or not
     * @param containerId       The frame layout containing the fragment
     */
    protected void replaceFragment(FragmentActivity activity, Fragment fragmentToReplace, boolean addToBackStack, int containerId) {
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
     * Method to show progress dialog from all fragments
     */
    protected void showProgress() {
        if (getBaseActivity() != null) {
            getBaseActivity().showProgress();
        }
    }

    /**
     * Method to dismiss the progress dialog
     */
    protected void dismissProgress() {
        if (getBaseActivity() != null) {
            getBaseActivity().dismissProgress();
        }
    }

    /**
     * method to alert the user on no network connection.
     */
    protected void noNetworkAlert() {
        Toast.makeText(getActivity(), getString(R.string.internet_connection_offline), Toast.LENGTH_LONG).show();
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

    protected void showAlertDialog(String title, String message, boolean isCancelButtonNeeded, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener) {
        showAlertDialog(title, message, isCancelButtonNeeded, positiveButtonClickListener, negativeButtonClickListener, null, null);
    }

    protected void showAlertDialog(String title, String message, boolean isCancelNeeded, DialogInterface.OnClickListener positiveButtonClickListener, DialogInterface.OnClickListener negativeButtonClickListener, String negativeButtonTitle, String positiveButtonTitle) {
        try {
            NPAlertDialogFragment alertDialogFragment = NPAlertDialogFragment.newInstance(title, message, isCancelNeeded, positiveButtonClickListener, negativeButtonClickListener, negativeButtonTitle, positiveButtonTitle);
            alertDialogFragment.show(getActivity().getSupportFragmentManager(), ALERT_DIALOG_TAG);
        } catch (Exception e) {
            NPLog.e(TAG, "showAlertDialog : Caught exception : " + e.getMessage(), e);
        }
    }

    protected void yetToImplementToast() {
        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.yet_to_implement), Toast.LENGTH_SHORT).show();
    }

    protected void goToActivity(Activity fromActivity, Class<? extends Activity> toActivity) {
        startActivity(new Intent(fromActivity, toActivity));
    }
}