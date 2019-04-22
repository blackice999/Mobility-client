package com.project.mobility.storage;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class Preferences {
    public static final String KEY_AUTH_PROVIDER_NAME = "key_authProviderName";
    public static final String KEY_AUTH_PROVIDER_EMAIL = "key_authProviderEmail";
    public static final String KEY_AUTH_PROVIDER_DISPLAY_NAME = "key_authProviderDisplayName";
    public static final String KEY_AUTH_PROVIDER_PHONE_NUMBER = "key_authProviderPhoneNumber";
    public static final String KEY_AUTH_PROVIDER_DELIVERY_ADDRESS = "key_authProviderDeliveryAddress";
    public static final String KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED = "key_authSplashScreenLaunched";
    public static final String KEY_ONBOARDING_COMPLETE = "key_OnboardingComplete";
    public static final int PREFERENCE_TYPE_AUTH = 0;
    public static final int PREFERENCE_TYPE_ONBOARDING = 1;

    public static final String KEY_FCM_TOKEN = "key_fcmToken";
    public static final String KEY_LAST_SHOWN_FRAGMENT = "key_lastShownFragment";
    private final SharedPreferences sharedPreferences;

    @Inject
    public Preferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key, 0).apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void setString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clearPreferences(int preferenceType) {
        switch (preferenceType) {
            case PREFERENCE_TYPE_AUTH:
                clearAuthPreferences();
                break;

            case PREFERENCE_TYPE_ONBOARDING:
                clearOnboardingPreferences();
                break;
        }
    }

    private void clearAuthPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_PROVIDER_NAME);
        editor.remove(KEY_AUTH_PROVIDER_EMAIL);
        editor.remove(KEY_AUTH_PROVIDER_DISPLAY_NAME);
        editor.remove(KEY_AUTH_PROVIDER_PHONE_NUMBER);
        editor.remove(KEY_AUTH_PROVIDER_DELIVERY_ADDRESS);
        editor.remove(KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED);
        editor.apply();
    }

    private void clearOnboardingPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_ONBOARDING_COMPLETE);
        editor.apply();
    }
}
