package com.pangaj.shruthi.newprojectimportsetup.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by Pan on 9/29/2017.
 */
public class NPSettingsFragment extends NPBaseFragment {
    public static NPSettingsFragment newInstance() {
        return new NPSettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.np_fragment_settings, container, false);
    }
}