package com.project.mobility.app;

import android.app.Application;

import com.project.mobility.BuildConfig;
import com.project.mobility.di.module.ToothpickModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class MobilityApplication extends Application {

    private static MobilityApplication instance;

    @Override
    public void onCreate() { 
        super.onCreate();
        instance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        initToothpick();
        initTimber();
    }

    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initToothpick() {
        Scope appScope = Toothpick.openScope(this);
        appScope.installModules(new SmoothieApplicationModule(this), new ToothpickModule(this));
    }

    public static MobilityApplication getInstance() {
        return instance;
    }
}
