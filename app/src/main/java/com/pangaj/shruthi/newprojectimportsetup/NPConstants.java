package com.pangaj.shruthi.newprojectimportsetup;

import android.Manifest;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by pangaj on 24/08/17.
 */
public class NPConstants {
    public static final String AUTHORISATION_KEY = "AuthorisationKey";
    public static final int LANGUAGE_ENGLISH = 1;
    public static final int LANGUAGE_ARABIC = 2;
    public static final String BEARER_COLON = "Bearer : ";
    public static final Collection<String> facebookPermission = Arrays.asList("public_profile", "email", "user_friends");
    public static final String EXPERIENCE_NAME = "experienceName";
    public static final String STRAIGHT_TO_EXPERIENCE_RATING = "straightToExperienceRating";
    public static final String IS_SIGN_UP_SCREEN = "goingToSigUpScreen";
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String GOOGLE = "GOOGLE";
    public static final String FACEBOOK = "FACEBOOK";
    //TabLayoutDetails
    public static final String CATEGORY_ID = "categoryId";
    public static final String EXPERIENCE_LIST = "experienceList";
    public static final String TAB_EXPERIENCE_NAME = "tabExperienceName";
    public static final String TAB_EXPERIENCE_LOCATION = "tabExperienceLocation";
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 111;
    public static final int SEARCH_EXPERIENCE_DELAY = 1200;
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static boolean TO_EXPERIENCE_RATING = false;
    public static boolean IN_RATE_EXPERIENCE_FRAGMENT = false;
    public static double LATITUDE_VALUE = 1000;
    public static double LONGITUDE_VALUE = 1000;
}