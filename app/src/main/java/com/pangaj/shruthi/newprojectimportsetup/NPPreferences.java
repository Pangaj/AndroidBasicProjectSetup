package com.pangaj.shruthi.newprojectimportsetup;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by pangaj on 23/08/17.
 */
public class NPPreferences {
    //Keys for user data
    private static final String KEY_TOKEN = "token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_IS_FIRST_TIME = "first_time";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_IS_SOCIAL_LOGIN = "is_social_login";
    private static final String KEY_DEFAULT_LANGUAGE = "default_language";
    //user profile
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_REGISTERED_USING = "registered_using";
    private static final String KEY_PROFILE_STATUS = "user_profile_status";
    private static final String KEY_IMAGE_ORIGINAL = "user_image_original";
    private static final String KEY_IMAGE_THUMBNAIL = "user_image_thumbnail";
    private static final String KEY_IMAGE_SMALL = "user_image_small";
    private static final String PREFS = "RatingsApp";

    private final SharedPreferences mPrefsRead;
    private final SharedPreferences.Editor mPrefsWrite;

    public NPPreferences(Context context) {
        mPrefsRead = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        mPrefsWrite = mPrefsRead.edit();
    }

    public String getToken() {
        return mPrefsRead.getString(KEY_TOKEN, null);
    }

    public void setToken(String token) {
        mPrefsWrite.putString(KEY_TOKEN, token);
        mPrefsWrite.commit();
    }

    public String getRefreshToken() {
        return mPrefsRead.getString(KEY_REFRESH_TOKEN, null);
    }

    public void setRefreshToken(String refreshToken) {
        mPrefsWrite.putString(KEY_REFRESH_TOKEN, refreshToken);
        mPrefsWrite.commit();
    }

    public boolean getIsFirstTime() {
        return mPrefsRead.getBoolean(KEY_IS_FIRST_TIME, true);
    }

    public void setIsFirstTime(boolean isFirstTime) {
        mPrefsWrite.putBoolean(KEY_IS_FIRST_TIME, isFirstTime);
        mPrefsWrite.commit();
    }

    public boolean getIsLoggedIn() {
        return mPrefsRead.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        mPrefsWrite.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        mPrefsWrite.commit();
    }

    public boolean getIsSocialLogin() {
        return mPrefsRead.getBoolean(KEY_IS_SOCIAL_LOGIN, false);
    }

    public void setIsSocialLogin(boolean isSocialLogin) {
        mPrefsWrite.putBoolean(KEY_IS_SOCIAL_LOGIN, isSocialLogin);
        mPrefsWrite.commit();
        //when social login is true, Always the login is true
        setIsLoggedIn(true);
    }

    public int getDefaultLanguage() {
        return mPrefsRead.getInt(KEY_DEFAULT_LANGUAGE, 0);
    }

    public void setDefaultLanguage(int defaultLanguage) {
        mPrefsWrite.putInt(KEY_DEFAULT_LANGUAGE, defaultLanguage);
        mPrefsWrite.commit();
    }

    public int getUserId() {
        return mPrefsRead.getInt(KEY_ID, 0);
    }

    public void setUserId(int userId) {
        mPrefsWrite.putInt(KEY_ID, userId);
        mPrefsWrite.commit();
    }

    public String getUserName() {
        return mPrefsRead.getString(KEY_NAME, null);
    }

    public void setUserName(String userName) {
        mPrefsWrite.putString(KEY_NAME, userName);
        mPrefsWrite.commit();
    }

    public String getUserEmail() {
        return mPrefsRead.getString(KEY_EMAIL, null);
    }

    public void setUserEmail(String userEmail) {
        mPrefsWrite.putString(KEY_EMAIL, userEmail);
        mPrefsWrite.commit();
    }

    public String getAccountType() {
        return mPrefsRead.getString(KEY_REGISTERED_USING, null);
    }

    public void setAccountType(String registeredUsing) {
        mPrefsWrite.putString(KEY_REGISTERED_USING, registeredUsing);
        mPrefsWrite.commit();
    }

    public String getUserProfileStatus() {
        return mPrefsRead.getString(KEY_PROFILE_STATUS, null);
    }

    public void setUserProfileStatus(String userProfileStatus) {
        mPrefsWrite.putString(KEY_PROFILE_STATUS, userProfileStatus);
        mPrefsWrite.commit();
    }

    public String getUserOriginalImage() {
        return mPrefsRead.getString(KEY_IMAGE_ORIGINAL, null);
    }

    public void setUserOriginalImage(String userOriginalImage) {
        mPrefsWrite.putString(KEY_IMAGE_ORIGINAL, userOriginalImage);
        mPrefsWrite.commit();
    }

    public String getUserThumbnailImage() {
        return mPrefsRead.getString(KEY_IMAGE_THUMBNAIL, null);
    }

    public void setUserThumbnailImage(String userThumbnailImage) {
        mPrefsWrite.putString(KEY_IMAGE_THUMBNAIL, userThumbnailImage);
        mPrefsWrite.commit();
    }

    public String getUserSmallImage() {
        return mPrefsRead.getString(KEY_IMAGE_SMALL, null);
    }

    public void setUserSmallImage(String userSmallImage) {
        mPrefsWrite.putString(KEY_IMAGE_SMALL, userSmallImage);
        mPrefsWrite.commit();
    }

    public void clearData() {
        //keep only default language and erase everything else
        int defaultLanguage = getDefaultLanguage();
        mPrefsWrite.clear();
        mPrefsWrite.commit();
        setDefaultLanguage(defaultLanguage);
    }
}