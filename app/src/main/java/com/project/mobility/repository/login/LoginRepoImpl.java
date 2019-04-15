package com.project.mobility.repository.login;

import com.project.mobility.di.injection.Injection;
import com.project.mobility.model.login.provider.AuthProvider;
import com.project.mobility.storage.Preferences;

import javax.inject.Inject;

public class LoginRepoImpl implements LoginRepo {

    @Inject Preferences preferences;

    @Inject
    public LoginRepoImpl() {
        Injection.inject(this);
    }

    @Override
    public boolean login(AuthProvider authProvider) {
        return false;
    }

    @Override
    public boolean register(AuthProvider authProvider) {
        if (authProvider != null) {
            authProvider.authenticate();
            preferences.setBoolean(Preferences.KEY_AUTH_IS_SPLASHSCREEN_LAUNCHED, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean logout(AuthProvider authProvider) {
        return authProvider.logout();
    }

    private void addUser(String firebaseId, String email, String displayName) {

    }
}
