package com.pangaj.shruthi.newprojectimportsetup;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pangaj on 23/08/17.
 */
public class NPPreferences {
    //Keys for user data
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_EMAIL = "login_email";
    private static final String KEY_PASSWORD = "login_password";
    //user profile
    private static final String PREFS = "RatingsApp";

    private final SharedPreferences mPrefsRead;
    private final SharedPreferences.Editor mPrefsWrite;

    NPPreferences(Context context) {
        mPrefsRead = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mPrefsWrite = mPrefsRead.edit();
    }

    public boolean getIsLoggedIn() {
        return mPrefsRead.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        mPrefsWrite.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        mPrefsWrite.commit();
    }

    public String getLoginEmail() {
        return mPrefsRead.getString(KEY_EMAIL, null);
    }

    public void setLoginEmail(String emailText) {
        mPrefsWrite.putString(KEY_EMAIL, emailText);
        mPrefsWrite.commit();
    }

    public String getLoginPassword() {
        return mPrefsRead.getString(KEY_PASSWORD, null);
    }

    public void setLoginPassword(String passwordText) {
        mPrefsWrite.putString(KEY_PASSWORD, passwordText);
        mPrefsWrite.commit();
    }

    public void clearData() {
        //Erase everything
        mPrefsWrite.clear();
        mPrefsWrite.commit();
    }
}