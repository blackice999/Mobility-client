package com.project.mobility.di.injection;

import com.project.mobility.app.MobilityApplication;
import com.project.mobility.di.module.ToothpickModule;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public final class Injection {

    private Injection() {

    }

    public static void inject(Object object) {
        Scope scope = Toothpick.openScopes(MobilityApplication.getInstance(), object);
        scope.installModules(new SmoothieApplicationModule(MobilityApplication.getInstance()), new ToothpickModule(MobilityApplication.getInstance()));
        Toothpick.inject(object, scope);
    }
}

