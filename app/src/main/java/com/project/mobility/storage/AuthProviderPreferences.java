package com.project.mobility.storage;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class AuthProviderPreferences {
    private static final String KEY_AUTH_PROVIDER_NAME = "key_authProviderName";
    private static final String KEY_AUTH_PROVIDER_EMAIL = "key_authProviderEmail";
    private static final String KEY_AUTH_PROVIDER_DISPLAY_NAME = "key_authProviderDisplayName";
    private static final String KEY_AUTH_PROVIDER_PHONE_NUMBER = "key_authProviderPhoneNumber";
    private static final String KEY_AUTH_PROVIDER_DELIVERY_ADDRESS = "key_authProviderDeliveryAddress";
    private static final String KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED = "key_authSplashScreenLaunched";
    private final SharedPreferences sharedPreferences;

    @Inject
    public AuthProviderPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setProviderName(String authProviderName) {
        sharedPreferences.edit().putString(KEY_AUTH_PROVIDER_NAME, authProviderName).apply();
    }

    public String getProviderName() {
        return sharedPreferences.getString(KEY_AUTH_PROVIDER_NAME, null);
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString(KEY_AUTH_PROVIDER_EMAIL, email).apply();
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_AUTH_PROVIDER_EMAIL, null);
    }

    public void setDisplayName(String displayName) {
        sharedPreferences.edit().putString(KEY_AUTH_PROVIDER_DISPLAY_NAME, displayName).apply();
    }

    public String getDisplayName() {
        return sharedPreferences.getString(KEY_AUTH_PROVIDER_DISPLAY_NAME, null);
    }

    public void setPhoneNumber(String phoneNumber) {
        sharedPreferences.edit().putString(KEY_AUTH_PROVIDER_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        return sharedPreferences.getString(KEY_AUTH_PROVIDER_PHONE_NUMBER, null);
    }

    public void setDeliverAddress(String deliverAddress) {
        sharedPreferences.edit().putString(KEY_AUTH_PROVIDER_DELIVERY_ADDRESS, deliverAddress).apply();
    }

    public String getDeliverAddress() {
        return sharedPreferences.getString(KEY_AUTH_PROVIDER_DELIVERY_ADDRESS, null);
    }

    public void setSplashScreenLaunched(boolean launched) {
        sharedPreferences.edit().putBoolean(KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED, launched).apply();
    }

    public boolean getSplashScreenLaunched() {
        return sharedPreferences.getBoolean(KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED, false);
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_AUTH_PROVIDER_NAME);
        editor.remove(KEY_AUTH_PROVIDER_EMAIL);
        editor.remove(KEY_AUTH_PROVIDER_DISPLAY_NAME);
        editor.remove(KEY_AUTH_PROVIDER_PHONE_NUMBER);
        editor.remove(KEY_AUTH_PROVIDER_DELIVERY_ADDRESS);
        editor.remove(KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED);
        editor.apply();
    }
}
