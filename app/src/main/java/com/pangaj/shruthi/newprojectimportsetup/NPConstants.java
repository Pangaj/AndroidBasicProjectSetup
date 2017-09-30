package com.pangaj.shruthi.newprojectimportsetup;

import android.Manifest;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by pangaj on 24/08/17.
 */
public class NPConstants {
    public static final int LANGUAGE_ENGLISH = 1;
    //    facebook
    public static final Collection<String> facebookPermission = Arrays.asList("public_profile", "email", "user_friends");
    //    location
    public static final String LOCATION = "location";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    //    location permission
    public static final String[] LOCATION_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static double LATITUDE_VALUE = 1000;
    public static double LONGITUDE_VALUE = 1000;

}