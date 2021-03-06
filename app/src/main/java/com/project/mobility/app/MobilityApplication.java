package com.project.mobility.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.google.firebase.messaging.FirebaseMessaging;
import com.project.mobility.BuildConfig;
import com.project.mobility.module.toothpick.ToothpickModule;
import com.project.mobility.storage.persistence.room.AppDatabase;
import com.project.mobility.util.notification.NotificationsHelper;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class MobilityApplication extends Application {

    private static MobilityApplication instance;
    private AppDatabase appDatabase;

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

        Stetho.initializeWithDefaults(this);
        appDatabase = AppDatabase.getInstance(this);

        NotificationsHelper.createNotificationChannel(this);
        FirebaseMessaging.getInstance().subscribeToTopic("test");
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

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static MobilityApplication getInstance() {
        return instance;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        AppDatabase.destroyInstance();
    }
}
