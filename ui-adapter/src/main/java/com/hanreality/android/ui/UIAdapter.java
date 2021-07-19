package com.hanreality.android.ui;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

public class UIAdapter {
    private static float sNonCompatDensity;

    private static float sNonCompatScaleDensity;

    private static UIConfig uiConfig = new UIConfig();

    public static void registerUIConfig(@NonNull UIConfig config) {
        uiConfig = config;
    }

    public static void delegateSystemDensity(@NonNull Activity activity) {
        final DisplayMetrics appDisplayMetrics = activity.getApplication().getResources().getDisplayMetrics();
        if (sNonCompatDensity == 0) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity;
            activity.getApplication().registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(@NonNull Configuration newConfig) {
                    if (newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = activity.getApplication().getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        final float targetDensity = appDisplayMetrics.widthPixels / uiConfig.designWidth;
        final float targetScaleDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity);
        final int targetDensityDpi = (int) (uiConfig.designWidth * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaleDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }
}
