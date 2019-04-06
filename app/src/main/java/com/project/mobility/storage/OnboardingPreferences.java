package com.project.mobility.storage;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class OnboardingPreferences {
    public static final String KEY_ONBOARDING_COMPLETE = "key_OnboardingComplete";
    private final SharedPreferences sharedPreferences;

    @Inject
    public OnboardingPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setOnboardingComplete(boolean firstTime) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETE, firstTime).apply();
    }

    public boolean isOnboardingComplete() {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false);
    }
}
