package com.project.mobility.di.module;

import android.app.Application;
import android.content.Context;

import com.facebook.CallbackManager;
import com.google.gson.Gson;
import com.project.mobility.repository.login.LoginRepo;
import com.project.mobility.repository.login.LoginRepoImpl;
import com.project.mobility.repository.onboarding.OnboardingCategoryRepo;
import com.project.mobility.repository.onboarding.OnboardingCategoryRepoImpl;

import toothpick.config.Module;

public class ToothpickModule extends Module {
    public ToothpickModule(Application application) {
        bind(Context.class).toInstance(application);
        bind(LoginRepo.class).to(LoginRepoImpl.class);
        bind(Gson.class).toInstance(new Gson());
        bind(CallbackManager.class).toInstance(CallbackManager.Factory.create());
        bind(OnboardingCategoryRepo.class).to(OnboardingCategoryRepoImpl.class);
    }
}
