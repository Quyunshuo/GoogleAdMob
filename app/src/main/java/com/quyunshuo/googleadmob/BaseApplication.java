package com.quyunshuo.googleadmob;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class BaseApplication extends Application {

    Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        initAdMob();
    }

    /**
     * 初始化AdMob SDK
     */
    private void initAdMob() {
        MobileAds.initialize(application, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
    }
}
