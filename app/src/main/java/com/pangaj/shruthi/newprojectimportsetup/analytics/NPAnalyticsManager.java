package com.pangaj.shruthi.newprojectimportsetup.analytics;

import com.pangaj.shruthi.newprojectimportsetup.NPApplication;

/**
 * Created by pangaj on 24/08/17.
 */
public class NPAnalyticsManager {
    // Category
    private static final String LOGIN = "CategoryLogin";

    public static void trackLogin(String action, String label) {
        NPApplication.getInstance().trackEvent(LOGIN, action, label);
    }
}