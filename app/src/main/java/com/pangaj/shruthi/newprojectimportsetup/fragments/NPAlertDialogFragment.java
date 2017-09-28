package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by pangaj on 23/08/17.
 */
public class NPAlertDialogFragment extends DialogFragment {
    private static final String BUNDLE_KEY_TITLE = "title";
    private static final String BUNDLE_KEY_MESSAGE = "message";
    private static DialogInterface.OnClickListener positiveButtonClickListener;
    private static DialogInterface.OnClickListener negativeButtonClickListener;
    private static boolean isNegativeRequired;
    private static String positiveButtonTitle, negativeButtonTitle;

    /**
     * Use this factory method to create a new instance of
     * this fragment
     *
     * @return A new instance of fragment NPAlertDialogFragment.
     */
    public static NPAlertDialogFragment newInstance(String title, String message, boolean isCancelNeeded, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener,
                                                    String negativeButton, String positiveButton) {
        NPAlertDialogFragment frag = new NPAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_KEY_TITLE, title);
        args.putString(BUNDLE_KEY_MESSAGE, message);
        positiveButtonTitle = positiveButton;
        negativeButtonTitle = negativeButton;
        isNegativeRequired = isCancelNeeded;
        positiveButtonClickListener = positiveClickListener;
        negativeButtonClickListener = negativeClickListener;
        frag.setArguments(args);
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(BUNDLE_KEY_TITLE);
        String message = getArguments().getString(BUNDLE_KEY_MESSAGE);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        this.setCancelable(false);
        alertDialog.setCancelable(false);
        if (!TextUtils.isEmpty(positiveButtonTitle)) {
            alertDialog.setPositiveButton(positiveButtonTitle, positiveButtonClickListener);//Passing null is handled in the AlertController.java class
        } else {
            alertDialog.setPositiveButton(R.string.alert_dialog_ok, positiveButtonClickListener);//Passing null is handled in the AlertController.java class
        }
        if (isNegativeRequired) {
            if (!TextUtils.isEmpty(negativeButtonTitle)) {
                alertDialog.setNegativeButton(negativeButtonTitle, negativeButtonClickListener);
            } else {
                alertDialog.setNegativeButton(R.string.alert_dialog_cancel, negativeButtonClickListener);
            }
        }
        return alertDialog.create();
    }
}