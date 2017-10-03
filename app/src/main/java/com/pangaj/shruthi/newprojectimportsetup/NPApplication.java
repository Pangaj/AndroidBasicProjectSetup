package com.pangaj.shruthi.newprojectimportsetup;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.maps.GeoApiContext;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.pangaj.shruthi.newprojectimportsetup.analytics.NPAnalyticsTrackers;
import com.pangaj.shruthi.newprojectimportsetup.utils.NPLog;

/**
 * Created by manikandan on 02/05/17.
 */
public class NPApplication extends Application {
    /**
     * Summary
     */
    private static String TAG = "NPApplication";
    private static NPApplication m_instance;
    //    private NPAPIManager mApiManager;
    private NPPreferences mPreferences;
    private DisplayImageOptions profileImageOptions;
    private DisplayImageOptions defaultImageOptions;
    private DisplayImageOptions placeHolderImageOptions;
    private GeoApiContext geoApiContext;

    /**
     * Method to get the instance
     *
     * @return Return the instance
     */
    public static NPApplication getInstance() {
        return m_instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        NPAnalyticsTrackers.initialize(this);
        NPAnalyticsTrackers.getInstance().get(NPAnalyticsTrackers.Target.APP);
        m_instance = this;
        initInstance();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initInstance() {
        mPreferences = new NPPreferences(this);
//        mApiManager = new NPAPIManager();
        initImageLoader();
    }

    /**
     * Method to get the API manager instance
     *
     * @return The API manager instance
     */
    /*public NPAPIManager getAPIManager() {
        return mApiManager;
    }*/
    public synchronized Tracker getGoogleAnalyticsTracker() {
        NPAnalyticsTrackers analyticsTrackers = NPAnalyticsTrackers.getInstance();
        return analyticsTrackers.get(NPAnalyticsTrackers.Target.APP);
    }

    /**
     * Method to get the preferences instance
     *
     * @return The preferences instance
     */
    public NPPreferences getPrefs() {
        return mPreferences;
    }

    /**
     * Initialise universal image loader
     */
    private void initImageLoader() {
        profileImageOptions = setDefaultImageOptions(R.drawable.ic_app_icon);
        defaultImageOptions = setDefaultImageOptions(R.color.accent);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .writeDebugLogs() //Uncomment if needed
                .build();
        ImageLoader.getInstance().init(config);
    }

    private DisplayImageOptions setDefaultImageOptions(int drawableImage) {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(drawableImage)
                .showImageForEmptyUri(drawableImage)
                .showImageOnFail(drawableImage)
                .bitmapConfig(Bitmap.Config.RGB_565)    //consume 2 times less memory than ARGB_8888
                .build();
    }

    /**
     * Method to get profile image options
     */
    public DisplayImageOptions getProfileImageOptions() {
        return profileImageOptions;
    }

    /**
     * Method to get default image options for categoryList and so on.
     */
    public DisplayImageOptions getDefaultImageOptions() {
        return defaultImageOptions;
    }

    /**
     * Method to get default image options for place holder in favorite list and so on.
     */
    public DisplayImageOptions getPlaceHolderImageOptions() {
        return placeHolderImageOptions;
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        // Set screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    /***
     * Tracking exception
     *
     * @param throwable exception to be tracked
     */
    public void trackException(Throwable throwable) {
        if (throwable != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), throwable))
                    .setFatal(false)
                    .build()
            );
        }
    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    /**
     * Method that returns the instance to do geo coding.
     *
     * @return The GeoApiContext instance used for geo coding
     */
    public GeoApiContext getGeoApiInstance() {
        NPLog.d(TAG, "getGeoApiInstance: " + BuildConfig.GEOCODING_SERVER_KEY);
        if (geoApiContext == null) {
            geoApiContext = new GeoApiContext.Builder()
                    .apiKey(BuildConfig.GEOCODING_SERVER_KEY)
                    .build();
        }
        return geoApiContext;
    }
}