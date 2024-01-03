package com.tencent.tcccsdk.tcccdemo;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

public class DemoApplication extends Application {
    private static final String TAG = DemoApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        super.onCreate();
        setAppLocale();
    }

    private void setAppLocale() {
        Locale currentLocale = getResources().getConfiguration().locale;
        Log.i(TAG, "currentLocale ="+currentLocale.getCountry());

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(currentLocale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
