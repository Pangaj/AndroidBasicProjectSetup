package com.pangaj.shruthi.newprojectimportsetup.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.pangaj.shruthi.newprojectimportsetup.NPApplication;
import com.pangaj.shruthi.newprojectimportsetup.NPPreferences;
import com.pangaj.shruthi.newprojectimportsetup.R;

/**
 * Created by pangaj on 24/08/17.
 */
public class NPSplashActivity extends NPBaseActivity {
    private NPPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.np_activity_splash);
        mPref = NPApplication.getInstance().getPrefs();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (mPref.getIsLoggedIn()) {
                    intent = new Intent(getApplicationContext(), NPNavigationActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), NPLoginScreenActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}